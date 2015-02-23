package org.jivesoftware.openfire.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jivesoftware.util.Log;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Cache;
import net.sf.ehcache.ObjectExistsException;

public class Storage {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/openfire";
	private static final String USER = "root";
	private static final String PASS = "openfire123";
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet results = null;
	private Cache orgCache = this.createCache("Orgs");
	private Cache confCache = this.createCache("Conflicts");
	
	// Creates Cache
	public Cache createCache(String name){
		Cache cache = null;
		try{
			CacheManager manager = CacheManager.getInstance();
			manager.addCache(name);
			cache = manager.getCache(name);
		} catch (CacheException cacheErr){
			cacheErr.printStackTrace();
		} catch (IllegalStateException e){
			e.printStackTrace();
		} catch (ClassCastException cce){
			cce.printStackTrace();
		}
		Log.info("Cache "+name+" created.");
        return cache;
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
	
	private void update(String sql){
		try{
			Class.forName("com.mysql.jdbc.Driver");
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	stmt = conn.createStatement();
	    	stmt.executeUpdate(sql);
		} catch (SQLException se){
    		se.printStackTrace();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
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
		if (orgCache.get(username) != null){	// if in cache
			try{
				org = orgCache.get(username).getObjectValue().toString();
			} catch (CacheException e){
				e.printStackTrace();
			}
    	} else {	// else if not in cache 
    		String sql = "select groupName from ofGroupUser where username = \""+username+"\"";
		    ResultSet rs = this.query(sql);
		    try{
			    while(rs.next()){
			    	org = rs.getString("groupName");
			    	orgCache.put(new Element(username,org));
			    }
		    } catch (SQLException se){
	    		se.printStackTrace();
		    }
		    this.closeConn(conn,stmt,results);  
		}
    	return org;
	}
	
	// Checks conflict between two orgs
	public boolean checkConflict(String org1,String org2){
		boolean conflict = false;
		if(confCache.get(org1) != null && confCache.get(org1).getObjectValue().toString() == org2){
			conflict = true;
		} else {
			String sql = "select org from conflict where org = \""+org1+"\" and conflictsWith = \""+org2+"\"";
	    	ResultSet rs = this.query(sql);
	    	try {
		    	while(rs.next()){
		    		if(rs.getString("org") != null){
		    			conflict = true;
		    			confCache.put(new Element(org1,org2));
		    		}
		    	}
	    	} catch (SQLException se){
	    		se.printStackTrace();
		   	}
	    	this.closeConn(conn,stmt,results);
		}	    
	   	return conflict;	    
	}
	
	public boolean isTransitive(String org){
		boolean transitive = false;
		String sql = "select transitive from ofGroup where groupName = \""+org+"\"";
    	ResultSet rs = this.query(sql);
    	try {
	    	while(rs.next()){
	    		if(rs.getBoolean("transitive") == true ){
	    			transitive = true;
	    		}
	    	}
    	} catch (SQLException se){
    		se.printStackTrace();
	   	}
    	this.closeConn(conn,stmt,results);
    	return transitive;
	}	    
	
	public void updateConflict(String org1, String org2){
		// Get all orgs that conflict with org1
		// Returns AIB This time
		String sql1 = "select org, conflictsWith from conflict where org = \""+org1+"\"";
		String sql2 = "select org, conflictsWith from conflict where conflictsWith = \""+org1+"\"";
    	ResultSet rs1 = this.query(sql1);
    	ResultSet rs2 = this.query(sql2);
    	ResultSet updated = null;
    	try {
	    	while(rs1.next()){
	    		
	    		// If org and conflictsWith are different
	    		if(!(rs1.getString("org").equals(org2))){
	    			System.out.println("org1:"+org1);
	    			System.out.println("org2:"+org2);
	    			String checkOrg = rs1.getString("conflictsWith");
	    			System.out.println("CheckOrg:"+checkOrg);
	    			System.out.println("originalOrg:"+rs1.getString("org"));
	    			// Check that conflicts are not in DB already
	    			if(this.checkConflict(org2,checkOrg) || (org2.equals(checkOrg))){
	    				System.out.println("Exists Already");
	    			} else {
	    				System.out.println("Adding to conflicts");
	    				String update1 = "Insert into conflict (org, conflictsWith) values (\""+org2+"\",\""+checkOrg+"\")";
		    			//System.out.println(update1);
	    				this.update(update1);
		    			String update2 = "Insert into conflict (org, conflictsWith) values (\""+checkOrg+"\",\""+org2+"\")";
		    			//System.out.println(update2);
		    			this.update(update2);	    			}
	    		}
	    	}
    	} catch (SQLException se){
    		se.printStackTrace();
	   	}
    	
   	
	}
}
