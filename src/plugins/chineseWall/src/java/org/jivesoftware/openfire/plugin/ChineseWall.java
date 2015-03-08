package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.logging.*;
import java.util.*;
import java.sql.*;
import org.xmpp.packet.*;
import org.xmpp.packet.Message.Type;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.event.*;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.muc.*;
import org.jivesoftware.openfire.plugin.ChineseWallUtil;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.Log;

public class ChineseWall implements Plugin, PacketInterceptor, MUCEventListener {	
	
	private InterceptorManager interceptorManager;
	private MUCEventDispatcher MUC;
	private ChineseWallUtil cw = new ChineseWallUtil();
	private Storage db = new Storage();
	
	public ChineseWall(){
		interceptorManager = interceptorManager.getInstance();		
	}
	
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        Log.info("Chinese Wall Plugin loaded...");
        interceptorManager.addInterceptor(this);
        MUC.addListener(this);
	}    
    
    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
        MUC.removeListener(this);
    }

	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {    	
		// @ TODO Factor in  Message, Presence and IQ
		// Create new message from Admin with error Message  and send that (wont breach CW)
		// Rename to ChineseWallPlugin, Can rename other Class to ChineseWall.java
		// Clean up sys.out streams
		// Clean up Logging		
		 if (packet instanceof Presence || packet instanceof Message){
		    	JID jidTo = packet.getTo();
	    		String toJID = jidTo.toBareJID();
	            String to = toJID.split("@")[0];
	            String toOrg = db.getOrg(to);
	            
	            JID jidFrom = packet.getFrom();
	        	String fromJID = jidFrom.toBareJID();
	        	String from = fromJID.split("@")[0];
	        	String fromOrg = db.getOrg(from);  
	        	if(toOrg != "" && fromOrg != ""){
	        		if(packet instanceof Message){
	        			// @ TODO This be done in one if statement? 
	        			if(db.isTransitive(toOrg)){
	        	    		// Update Tables
	        	    		db.updateConflict(toOrg,fromOrg);
	        	    	}
	            		if(db.isTransitive(fromOrg)){
	        	    		// Update Tables
	        	    		db.updateConflict(fromOrg,toOrg);
	        	    	}
	        		}
	        		if (db.checkConflict(toOrg,fromOrg)){
	    	    		System.out.println("CHINESEWALL : Dropping from "+from+" to "+to+".  Reason : "+fromOrg+" conflicts with "+toOrg+".");
	    	    		Log.info("Chinese Wall : Packet from "+from+" to "+to+" was intercepted.");
	    	    		PacketRejectedException rejectMessage = new PacketRejectedException();
	    	    		// String not working
	    	    		rejectMessage.setRejectionMessage("Message dropped due to Chinese Wall");
	    	    		//throw rejectMessage?? 
	    			}
	        	}
		 } 
    }
	
	
	
	// Deals with users who join group chat
	@Override
	public void occupantJoined(JID roomJID, JID user, String nickname)   {
		MUCRoom room = cw.getRoom(roomJID);
		List<String> members = cw.getMembers(roomJID);
		// Check for conflict
		String userOrg = db.getOrg(nickname);
		
		// Need someone responsible for kick
		JID admin = new JID("admin","admin","");
		
		// Foreach Member, get their org. If conflict, kick user that joined.
		for (String member : members){
			String memberOrg = db.getOrg(member);
			
			if (db.checkConflict(userOrg,memberOrg)){
				MUCRole role = cw.getRole(nickname, room);
				
				// Kick User & Update Presence
				try{
					Presence presence = room.kickOccupant(user, admin, "Kicked!");
					// Any need for this?? 
					role.setPresence(presence);
				} catch( NotAllowedException naw){
    	       		naw.printStackTrace();
    	       	}
			}
		}
	}
	
	@Override
	public void roomSubjectChanged(JID roomJID,JID user,String newSubject) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void messageReceived(JID roomJID, JID user, String nickname, Message message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void nicknameChanged(JID roomJID,JID user, String oldNickname, String newNickname)  {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void occupantLeft(JID roomJID, JID user)  {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void roomDestroyed(JID roomJID)   {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void roomCreated(JID roomJID)    {
		// TODO Auto-generated method stub
	}
}