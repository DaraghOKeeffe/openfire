package ie.ucc.plugin.trustManagement.servlets;


import ie.ucc.plugin.trustManagement.TrustManagementPlugin;

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
import org.jivesoftware.util.Log;

/**
 * 
 * @author Wayne Mac Adams
 *
 */
public class TrustManagementServlet extends HttpServlet {

    /**
	 * Auto generated serial UID
	 */
	private static final long serialVersionUID = 352793198789395949L;
	private TrustManagementPlugin trustManagementPlugin;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        trustManagementPlugin = (TrustManagementPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("trustmanagement");

        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("trustManagement/trustmanagement");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Printwriter for writing out responses to browser
        PrintWriter out = response.getWriter();
        
        String IP = request.getParameter("IP");
        String password = request.getParameter("password");
        String port = request.getParameter("port");
        String contentFilter = request.getParameter("contentFilter");
        String validityPeriod = request.getParameter("validityPeriod");
        String userID = request.getParameter("userID");
//        Log.debug(userID+" , "+contentFilter);
       // Check that our plugin is enabled.
        if (!trustManagementPlugin.isEnabled()) {
            Log.warn("Trust mamanagement plugin is disabled: " + request.getQueryString());
            replyError("TrustManagementDisabled",response, out);
            return;
        }
       
        // Check this request is authorised
        if (password == null || !password.equals(trustManagementPlugin.getPassword())){
            Log.warn("An unauthorised trustManagement request was received: " + request.getQueryString());
            replyError("RequestNotAuthorised",response, out);
            return;
         }
        // Add IP for validity period if it is provided or add
        // for default validity period of 24 hours.
        int valPeriod = TrustManagementPlugin.DEFAULT_VALIDITY_PERIOD;
        if(IP!=null)
        {
        	if(validityPeriod!=null){
        		valPeriod = Integer.parseInt(validityPeriod);
        	}
        	if(port!=null)
        		this.trustManagementPlugin.addIP(IP, Integer.parseInt(port), valPeriod);
        	else
        		this.trustManagementPlugin.addIP(IP,
        				XMPPServer.getInstance().getConnectionManager().getServerListenerPort(),
        				valPeriod);
        	this.replyMessage("IP: \""+IP+ "\" for Validity Period of "+valPeriod+ 
        			" added", response, out);
        }
        
        if(contentFilter!= null){
        	this.trustManagementPlugin.addContentFilter(contentFilter);
        	this.replyMessage("ContentFilter: \""+StringEscapeUtils.escapeXml(contentFilter)+ "\" added", response, out);
        }
        if (userID !=null){	
        	
        	this.trustManagementPlugin.addUserID(userID, valPeriod);
            this.replyMessage("UserID: \"" + StringEscapeUtils.escapeXml(userID)+ "\"added", response, out);
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
        AuthCheckFilter.removeExclude("trustManagement/trustmanagement");
    }
}

