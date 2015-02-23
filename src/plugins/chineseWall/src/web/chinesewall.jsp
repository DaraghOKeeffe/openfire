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
   		} else if (request.getParameter("transitive") != null){
   			out.println("Group "+request.getParameter("transitive")+" is now transitive.<br><br>");	
   			updateTransitive(request.getParameter("transitive"),request.getParameter("boole"));
   		}
   	%>
   	<% 
    	GroupManager groupManager=GroupManager.getInstance();
    	Collection<Group> groups = groupManager.getGroups();
	%>
   		<b><i>Users can be added to groups under the "Users/Groups" tab</i></b><br><br>
   		<h2>Please Enter conflicting groups below.</h2><br><br>
    	<form action="chinesewall.jsp">
    	
    		
    		
    		
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
		<br><br>
		<h2>Transitive property for Groups</h2>
		<form action="chinesewall.jsp">
			<select name="transitive">
				<%  for(Group group:groups){
						out.println("<option value=\""+group+"\">"+group+"</option>");
					} 
				%>
			</select>
			<select name="boole">
				<option value="1">True</option>
				<option value="0">False</option>
			</select>
			<br><input type="submit" value="Submit">
		</form>
   </body>
</html>
<%!
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost:3306/openfire";
	String USER = "root";
	String PASS = "openfire123";
	Connection conn = null;
	Statement stmt = null;

	
	public boolean updateTable(String first, String second){
		Boolean results = null;
		String insert1 = "INSERT INTO conflict (org,conflictsWith) VALUES (\""+first+"\",\""+second+"\")"; 
		String insert2 = "INSERT INTO conflict (org,conflictsWith) VALUES (\""+second+"\",\""+first+"\")"; 
		
		try{
			Class.forName(JDBC_DRIVER);
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	stmt = conn.createStatement();
	    	stmt.execute(insert1);
	    	results = stmt.execute(insert2);
		} catch (SQLException se){
    		se.printStackTrace();
    		return false;
    	} catch (Exception e){
    		e.printStackTrace();
    		return false;
    	}
		return results;
	}
	
	public boolean updateTransitive(String group,String boole){
		String sql = "update ofGroup set transitive = \""+boole+"\" where groupName = \""+group+"\"";
		Boolean results = null;
		try{
			Class.forName(JDBC_DRIVER);
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	stmt = conn.createStatement();
	    	results = stmt.execute(sql);
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