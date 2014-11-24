package org.jivesoftware.openfire.plugin.servlets;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.plugin.ContentFilterPlugin;
import org.jivesoftware.util.Log;
import org.apache.commons.lang.StringEscapeUtils;
/**
 * 
 * @author Wayne Mac Adams
 *
 */
public class ContentFilterServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2888849748801671270L;
	/**
	 * Auto generated serial UID
	 */
	private ContentFilterPlugin contentFilterPlugin;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        contentFilterPlugin = (ContentFilterPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("contentfilter");

        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("contentFilter/contentfilter");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	// Printwriter for writing out responses to browser
        PrintWriter out = response.getWriter();

        String contentFilter = request.getParameter("contentFilter");
        
        if(contentFilter!= null){
        	this.contentFilterPlugin.appendContentFilter(contentFilter);
        	this.replyMessage("ContentFilter: \""+StringEscapeUtils.escapeXml(contentFilter)+ "\" appended", response, out);
        }else{
        	this.replyError("No content filter string provided", response, out);
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
        AuthCheckFilter.removeExclude("contentFilter/contentfilter");
    }
}

