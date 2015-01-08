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
            String toOrg = cw.getOrg(to);
            
            JID jidFrom = packet.getFrom();
        	String fromJID = jidFrom.toBareJID();
        	String from = fromJID.split("@")[0];
        	String fromOrg = cw.getOrg(from);    
        	
    		System.out.println("Packet from "+fromJID+" to "+toJID);
	    	//check rules    
	    	if (cw.checkConflict(toOrg,fromOrg)){
	    		System.out.println("CHINESEWALL : Dropping from "+from+" to "+to+" : "+msg.getBody());
	    		System.out.println("CHINESEWALL : Reason : "+fromOrg+" conflicts with "+toOrg+".");
	    		Log.info("Chinese Wall : Packet from "+from+" to "+to+" was intercepted.");
	    		throw new PacketRejectedException();
			}
    	}
    }

	
	//move these to another Class
	
	/*//Returns org of user
	public String getOrg(String username){
			String org = "";
	    	try{
		    	Class.forName("com.mysql.jdbc.Driver");
		    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
		    	stmt = conn.createStatement();
		    	String sql = "select groupName from ofGroupUser where username = \""+username+"\"";
		    	ResultSet rs = stmt.executeQuery(sql);
		    	while(rs.next()){
		    		org = rs.getString("groupName");
		    	}
		    	rs.close();
		    	stmt.close();
		    	conn.close();
	    	} catch (SQLException se){
	    		se.printStackTrace();
	    	} catch (Exception e){
	    		e.printStackTrace();
	    	}
	    	return org;
	 }*/
	 	 
	/*//Checks conflict between two orgs
	public boolean checkConflict(String org1,String org2){
		 boolean conflict = false;
		 try{
		    Class.forName("com.mysql.jdbc.Driver");
		    conn = DriverManager.getConnection(DB_URL,USER,PASS);
		    stmt = conn.createStatement();
		    //return org and check all against conflictsWith??
		    String sql = "select org from conflict where org = \""+org1+"\" and conflictsWith = \""+org2+"\"";
	    	ResultSet rs = stmt.executeQuery(sql);
	    	while(rs.next()){
	    		if(rs.getString("org") != null){
	    			conflict = true;
	    		}
	    	}
	    	rs.close();
	    	stmt.close();
	    	conn.close();
    	} catch (SQLException se){
    		se.printStackTrace();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return conflict;	    
	 }*/
	 
	/*//returns chatroom given room JID
	public MUCRoom getRoom(JID roomJID){
		List <MUCRoom> rooms = getRooms();
		MUCRoom MUCreturnRoom = null;
		for (MUCRoom room : rooms){
			if (room.getJID().equals(roomJID)){
				MUCreturnRoom = room;
			}
		}
		return MUCreturnRoom;
	}*/
	
	/*//returns list of chatrooms
	public List getRooms(){
		XMPPServer server = XMPPServer.getInstance();
	    MultiUserChatService m = server.getMultiUserChatManager().getMultiUserChatService("conference");
	    List <MUCRoom> rooms = m.getChatRooms();
	    return rooms;
	}*/
	 
	/*//Returns all members of room
	public List getMembers(JID roomJID){
    	ArrayList<String> members = new ArrayList<String>();
    	List <MUCRoom> rooms = getRooms();    	
    	for (MUCRoom room : rooms){
    		if (room.getJID().equals(roomJID)){
	    		Collection <MUCRole> occupants = room.getOccupants();
	    		for(MUCRole occupant : occupants){
	    			String nick = occupant.getNickname();
	    			if(nick.contains("@")){
	    				nick = nick.split("@")[0];
	    			}
	    			members.add(nick);
	    		}
    		}
    	}
    	System.out.println("Members : "+members);
    	return members;
    }*/
	
	@Override
	public void occupantJoined(JID roomJID, JID user, String nickname)   {
		System.out.println("User "+nickname+" joined room "+roomJID);
		MUCRoom room = cw.getRoom(roomJID);
		//Check for conflict
		String userOrg = cw.getOrg(nickname);
		List<String> members = cw.getMembers(roomJID);
		JID admin = new JID("admin","admin","");
		for (String member : members){
			String memberOrg = cw.getOrg(member);
			if (cw.checkConflict(userOrg,memberOrg)){
				//kick user
				try{
					//@TODO alter presence after kick!
					room.kickOccupant(user, admin, "Kicked!");
					System.out.println("CHINESEWALL : Removing user "+user);
					System.out.println("CHINESEWALL : Reason : "+userOrg+" conflicts with "+memberOrg);
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
		System.out.println("User "+user+" has left.");
		
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