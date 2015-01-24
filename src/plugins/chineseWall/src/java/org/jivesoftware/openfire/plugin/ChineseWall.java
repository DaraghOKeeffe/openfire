package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.*;
import java.sql.*;
import org.xmpp.packet.*;
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
	
	
	public ChineseWall(){
		interceptorManager = interceptorManager.getInstance();		
	}
	
	private InterceptorManager interceptorManager;
	private MUCEventDispatcher MUC;
	private ChineseWallUtil cw = new ChineseWallUtil();
	private persistentStorage db = new persistentStorage();
	
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
    	Message msg = (Message)packet;
    	if (msg.getType() == Message.Type.chat){
    		JID jidTo = packet.getTo();
    		String toJID = jidTo.toBareJID();
            String to = toJID.split("@")[0];
            String toOrg = db.getOrg(to);
            
            JID jidFrom = packet.getFrom();
        	String fromJID = jidFrom.toBareJID();
        	String from = fromJID.split("@")[0];
        	String fromOrg = db.getOrg(from);    
        	
    		System.out.println("Packet from "+fromJID+" to "+toJID);
	    	//check rules    
	    	if (db.checkConflict(toOrg,fromOrg)){
	    		//System.out.println("CHINESEWALL : Dropping from "+from+" to "+to+" : "+msg.getBody()+". Reason : "+fromOrg+" conflicts with "+toOrg+".");
	    		Log.info("Chinese Wall : Packet from "+from+" to "+to+" was intercepted.");
	    		throw new PacketRejectedException();
			}
    	}
    }
	
	@Override
	public void occupantJoined(JID roomJID, JID user, String nickname)   {
		//System.out.println("User "+nickname+" joined room "+roomJID);
		MUCRoom room = cw.getRoom(roomJID);
		//Check for conflict
		String userOrg = db.getOrg(nickname);
		List<String> members = cw.getMembers(roomJID);
		JID admin = new JID("admin","admin","");
		for (String member : members){
			String memberOrg = db.getOrg(member);
			if (db.checkConflict(userOrg,memberOrg)){
				MUCRole role = cw.getRole(nickname, room);
				//kick user
				try{
					//@TODO alter presence after kick!
					Presence presence = room.kickOccupant(user, admin, "Kicked!");
					//This doesnt work. Common problem with openfire
					role.setPresence(presence);
					Log.info("Chinese Wall : Removing User "+user);
					//System.out.println("Presence:"+role.getPresence());
					//System.out.println("CHINESEWALL : Removing user "+user);
					//System.out.println("CHINESEWALL : Reason : "+userOrg+" conflicts with "+memberOrg);
				} catch( NotAllowedException n){
    	       		n.printStackTrace();
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