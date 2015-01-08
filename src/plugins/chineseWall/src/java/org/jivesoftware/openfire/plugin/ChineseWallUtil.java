package org.jivesoftware.openfire.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.xmpp.packet.JID;


public class ChineseWallUtil {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/openfire";
	static final String USER = "root";
	static final String PASS = "openfire123";
	Connection conn = null;
	Statement stmt = null;
	
	public ChineseWallUtil(){
		
	}
	//returns list of chatrooms
	public List getRooms(){
		XMPPServer server = XMPPServer.getInstance();
	    MultiUserChatService m = server.getMultiUserChatManager().getMultiUserChatService("conference");
	    List <MUCRoom> rooms = m.getChatRooms();
	    return rooms;
	}
	
	//Returns all members of room
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
	}
	
	//returns chatroom given room JID
	public MUCRoom getRoom(JID roomJID){
		List <MUCRoom> rooms = getRooms();
		MUCRoom MUCreturnRoom = null;
		for (MUCRoom room : rooms){
			if (room.getJID().equals(roomJID)){
				MUCreturnRoom = room;
			}
		}
		return MUCreturnRoom;
	}
	
	//Returns org of user
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
	
	//Checks conflict between two orgs
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
}
