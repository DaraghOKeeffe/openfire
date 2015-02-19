<%@ page import="java.util.*,
				 org.jivesoftware.openfire.group.*,
				 org.jivesoftware.openfire.group.GroupManager,
                 org.jivesoftware.openfire.XMPPServer,
                 org.jivesoftware.openfire.user.*,
				 org.jivesoftware.openfire.plugin.ChineseWall,
				 org.jivesoftware.openfire.plugin.Storage,
                 org.jivesoftware.util.*,
                 
                 java.sql.Connection,
                 java.sql.DriverManager,
                 java.sql.ResultSet,
                 java.sql.SQLException,
                 java.sql.Statement"
%>
<html>
  	<head>
    	<title>Chinese Wall Administration</title>
		<meta name="pageID" content="ChineseWall"/>
   	</head>
   	<body>
   	<%	if (request.getParameter("firstgroup") != null){
   			out.println("Conflict between "+request.getParameter("firstgroup")+" and "+request.getParameter("secondgroup")+" has been added.<br><br>");
   			updateTable(request.getParameter("firstgroup"),request.getParameter("secondgroup"));
   		}
   	%>
   		<b><i>Users can be added to groups under the "Users/Groups" tab</i></b><br><br>
   		Please Enter conflicting groups below.<br><br>
    	<form action="chinesewall.jsp">
    	
    		<% 
    			GroupManager groupManager=GroupManager.getInstance();
    			Collection<Group> groups = groupManager.getGroups();
    		 %>
    		
    		
			First Group: <select name="firstgroup">
						 	<%  for(Group group:groups){
						 			out.println("<option value=\""+group+"\">"+group+"</option>");
						 		} 
							%>
						 </select>
			<br><br>
			Second Group:<select name="secondgroup">
						 	<%  for(Group group:groups){
						 			out.println("<option value=\""+group+"\">"+group+"</option>");
						 		} 
							%>
						 </select>
			<br><input type="submit" value="Submit">
		</form>
   </body>
</html>
<%!
	public boolean updateTable(String first, String second){
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		String DB_URL = "jdbc:mysql://localhost:3306/openfire";
		String USER = "root";
		String PASS = "openfire123";
		Connection conn = null;
		Statement stmt = null;
		Boolean results = null;
		String sql1 = "INSERT INTO conflict (org,conflictsWith) VALUES (\""+first+"\",\""+second+"\")"; 
		String sql2 = "INSERT INTO conflict (org,conflictsWith) VALUES (\""+second+"\",\""+first+"\")"; 
		
		try{
			Class.forName(JDBC_DRIVER);
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	stmt = conn.createStatement();
	    	stmt.execute(sql1);
	    	results = stmt.execute(sql2);
		} catch (SQLException se){
    		se.printStackTrace();
    		return false;
    	} catch (Exception e){
    		e.printStackTrace();
    		return false;
    	}
		return results;
	}
%>