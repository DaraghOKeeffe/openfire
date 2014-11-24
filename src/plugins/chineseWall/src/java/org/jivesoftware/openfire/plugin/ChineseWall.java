package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.*;
import java.sql.*;
import org.xmpp.packet.*;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.muc.*;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.Log;

public class ChineseWall implements Plugin, PacketInterceptor{	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/openfire";
	static final String USER = "root";
	static final String PASS = "openfire123";
	Connection conn = null;
	Statement stmt = null;
	
	public ChineseWall(){
		interceptorManager = interceptorManager.getInstance();		
	}
	
	private InterceptorManager interceptorManager;

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        Log.info("Chinese Wall Plugin loaded...");
        interceptorManager.addInterceptor(this);
    }

    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
    }

	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {    	
    	Message msg = (Message)packet;
    	if (msg.getType() == Message.Type.chat){
    		JID jidTo = packet.getTo();
    		String toJID = jidTo.toBareJID();
            String to = toJID.split("@")[0];
            String toOrg = getOrg(to);
            
            JID jidFrom = packet.getFrom();
        	String fromJID = jidFrom.toBareJID();
        	String from = fromJID.split("@")[0];
        	String fromOrg = getOrg(from);    
        	
    		System.out.println("Packet from "+fromJID+" to "+toJID);
	    	//check rules    
	    	if (checkConflict(toOrg,fromOrg)){
	    		System.out.println("CHINESEWALL : Dropping from "+from+" to "+to+" : "+msg.getBody());
	    		System.out.println("CHINESEWALL : Reason : "+fromOrg+" conflicts with "+toOrg+".");
	    		Log.info("Chinese Wall : Packet from "+from+" to "+to+" was intercepted.");
	    		throw new PacketRejectedException();
			}
    	}else if(msg.getType() == Message.Type.groupchat){
    		//need to change that people without conflicts can still send messages.
    		//look at packet .getFrom() maybe
    		//System.out.println("Groupchat message : "+msg.getBody());
    		JID jidFrom = packet.getFrom();
        	String fromJID = jidFrom.toBareJID();
        	String from = fromJID.split("@")[0];
        	String fromOrg = getOrg(from);
        	//System.out.println("From : "+from);
        	MUCRoom currentRoom;
        	MUCRole role;
    		List<String> members = getMembers();
    		for (String a : members){
				//get orgs
				String memberOrg = getOrg(a);
				//if conflict between any member and the sender
    			if(checkConflict(fromOrg,memberOrg)){
    				//reject user from groupchat.
    				//get room 
    				List <MUCRoom> rooms = getRooms();
    	        	for (MUCRoom room : rooms){
    	        		//check which room user is in, and kick him from this room
    	        		if(room.hasOccupant(a)){
    	        			try{
    	        				role = room.getOccupant(a);
    	        				//need to alter presence after kick?
    	        				room.kickOccupant(jidFrom, jidFrom, "Kicked!");
    	        				System.err.println("CHINESEWALL : JID : "+jidFrom+"GROUP Dropping from "+from+" to "+a+" : "+msg.getBody());
    	        	    		System.out.println("CHINESEWALL : GROUP Reason : "+memberOrg+" conflicts with "+fromOrg+".");
    	        	    		Log.info("Chinese Wall : Packet from "+from+" to "+a+" was intercepted.");
    	        				throw new PacketRejectedException();
    	        			}catch( UserNotFoundException e){
    	        	       		e.printStackTrace();
    	        	       	}catch( NotAllowedException n){
    	        	       		n.printStackTrace();
    	        	       	}
    	        			
    	        		}
    	        	} 
    				
    			}
    		}
    		
    	}
    }

	
	//move these to another Class
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
	 }
	 	 
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
	 }
	 
	//returns members of chatroom where packet is being sent
	public List getRooms(){
		XMPPServer server = XMPPServer.getInstance();
	    MultiUserChatService m = server.getMultiUserChatManager().getMultiUserChatService("conference");
	    List <MUCRoom> rooms = m.getChatRooms();
	    return rooms;
	 }
	 
	public List getMembers(){
    	ArrayList<String> members = new ArrayList<String>();
    	List <MUCRoom> rooms = getRooms();
    	for (MUCRoom room : rooms){
    		Collection <MUCRole> occupants = room.getOccupants();
    		for(MUCRole occupant : occupants){
    			String nick = occupant.getNickname();
    			if(nick.contains("@")){
    				nick = nick.split("@")[0];
    			}
    			members.add(nick);
    		}
        	
    	}
    	//System.out.println("Members : "+members);
    	return members;
	 }

}