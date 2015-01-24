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
	
	public ChineseWallUtil(){
		
	}
	
	// Returns list of Chatrooms
	public List getRooms(){
		XMPPServer server = XMPPServer.getInstance();
		List<MUCRoom> rooms = new ArrayList<MUCRoom>();	
		List<MultiUserChatService> chatServices = server.getMultiUserChatManager().getMultiUserChatServices();
	    for (MultiUserChatService chatService : chatServices){
	    	rooms.addAll(chatService.getChatRooms());
	    }
	    return rooms;
	}
	
	// Returns all members of room
	public List getMembers(JID roomJID){
	   	ArrayList<String> members = new ArrayList<String>();
	   	List <MUCRoom> rooms = this.getRooms();    	
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
	   	return members;
	}
	
	// Gets Role of user in room
	public static MUCRole getRole(String nick, MUCRoom room){
		MUCRole role = null;
		Collection <MUCRole> occupants = room.getOccupants();
		for(MUCRole occupant : occupants){
			System.out.println(nick+" == "+occupant.getNickname());
			if(nick == occupant.getNickname()){
				role = occupant;
			}
		}
		return role;
	}
	
	// Returns chatroom given room JID
	public MUCRoom getRoom(JID roomJID){
		List <MUCRoom> rooms = this.getRooms();
		MUCRoom MUCreturnRoom = null;
		for (MUCRoom room : rooms){
			if (room.getJID().equals(roomJID)){
				MUCreturnRoom = room;
			}
		}
		return MUCreturnRoom;
	}
	
}
