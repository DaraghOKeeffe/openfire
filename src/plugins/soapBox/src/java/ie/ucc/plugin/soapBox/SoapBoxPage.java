package ie.ucc.plugin.soapBox;

import ie.ucc.plugin.SoapBoxPlugin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.util.Log;

/**
 * 
 * @author Wayne Mac Adams
 *
 */
public class SoapBoxPage extends HttpServlet
{
	private SoapBoxPlugin plugin;


    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        plugin = (SoapBoxPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("soapbox");
 
        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("soapBox/soapbox");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Printwriter for writing out responses to browser
        PrintWriter out = response.getWriter();
        this.plugin.addSoapBox();
        replyMessage("Success", response, out);
    }

    private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<result>" + message + "</result>");
        out.flush();
    }


    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
        super.destroy();
        // Release the excluded URL
        AuthCheckFilter.removeExclude("soapBox/soapbox");
    }
}
