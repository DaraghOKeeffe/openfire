package org.jivesoftware.openfire.plugin.servlets;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.plugin.PacketFilterPlugin;
import org.jivesoftware.util.Log;
/**
 * 
 * @author Wayne Mac Adams
 *
 */
public class PacketFilterServlet extends HttpServlet {

	//24 hours
	public static final int DEFAULT_VALIDITY_PERIOD=60*24;
	/**
	 * Auto generated serial UID
	 */
	private static final long serialVersionUID = -1677252382935588080L;
	private PacketFilterPlugin packetFilterPlugin;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        packetFilterPlugin = (PacketFilterPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("packetfilter");

        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("packetFilter/packetfilter");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	Log.debug("Content Filter Servlet Invoked");
        // Printwriter for writing out responses to browser
        PrintWriter out = response.getWriter();

        String userID = request.getParameter("userID");
        String validityPeriod = request.getParameter("validityPeriod");
     // Add IP for validity period if it is provided or add
        // for default validity period of 24 hours.
        int valPeriod = DEFAULT_VALIDITY_PERIOD;
        if(validityPeriod!=null){
    		valPeriod = Integer.parseInt(validityPeriod);
    	}
        if(userID!= null){
        	this.packetFilterPlugin.addRuleViaTMAgent(userID, valPeriod);
        	
        	this.replyMessage("UserID: \""+StringEscapeUtils.escapeXml(userID)+ "\" passed", response, out);
        }else{
        	this.replyError("No userID provided", response, out);
        }
    }

    private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<result>" + message + "</result>");
        out.flush();
    }

    private void replyError(String error,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<error>" + error + "</error>");
        out.flush();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
        super.destroy();
        // Release the excluded URL
        AuthCheckFilter.removeExclude("packetFilter/packetfilter");
    }
}

