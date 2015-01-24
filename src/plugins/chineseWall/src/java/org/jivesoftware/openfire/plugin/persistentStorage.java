package org.jivesoftware.openfire.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.ehcache.*;
public class persistentStorage {
	// TODO Cache Resultss
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/openfire";
	private static final String USER = "root";
	private static final String PASS = "openfire123";
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet results = null;
	
	// Creates Cache
	public static Cache createCache(){
		CacheManager cchm = CacheManager.getInstance();
		cchm.addCache("DBCache");
        Cache cch = cchm.getCache("DBCache");
        return cch;
	}
	
	private ResultSet query(String sql){
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	stmt = conn.createStatement();
	    	results = stmt.executeQuery(sql);
	    	
		} catch (SQLException se){
    		se.printStackTrace();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		return results;
	}
	
	private void closeConn (Connection conn, Statement stmt, ResultSet results){
		try {
			results.close();
			stmt.close();
			conn.close();
		} catch (SQLException se){
    		se.printStackTrace();
    	}
	}
	
	// Returns org of user
	public String getOrg(String username){
		String org = "";
    	String sql = "select groupName from ofGroupUser where username = \""+username+"\"";
	    ResultSet rs = this.query(sql);
	    try{
		    while(rs.next()){
		    	org = rs.getString("groupName");
		    }
	    } catch (SQLException se){
    		se.printStackTrace();
	    }
	    this.closeConn(conn,stmt,results);
    	return org;
	}
	
	// Checks conflict between two orgs
	public boolean checkConflict(String org1,String org2){
		 boolean conflict = false;
	    // Return Org and check all against conflictsWith??
	    String sql = "select org from conflict where org = \""+org1+"\" and conflictsWith = \""+org2+"\"";
    	ResultSet rs = this.query(sql);
    	try {
	    	while(rs.next()){
	    		if(rs.getString("org") != null){
	    			conflict = true;
	    		}
	    	}
    	} catch (SQLException se){
    		se.printStackTrace();
	   	}
    	this.closeConn(conn,stmt,results);
	   	return conflict;	    
	}
}
