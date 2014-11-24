package ie.ucc.plugin.trustManagement;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringEscapeUtils;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.server.RemoteServerConfiguration;
import org.jivesoftware.openfire.server.RemoteServerManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.jivesoftware.util.StringUtils;

/**
 * 
 * @author Wayne Mac Adams
 *
 */
public class TrustManagementPlugin implements Plugin, PropertyEventListener
{
	private String password;
    private boolean enabled;
    //24 hours, 60 minutes * 24
    public static final int DEFAULT_VALIDITY_PERIOD=60*24;
	
	public void initializePlugin(PluginManager manager, File pluginDirectory)
	{
        password = JiveGlobals.getProperty("plugin.trustmanagement.password", "");
        // If no secret key has been assigned to the user service yet, assign a random one.
        if (password.equals("")){
            password = StringUtils.randomString(8);
            setPassword(password);
        }
        
        // See if the service is enabled or not.
        enabled = JiveGlobals.getBooleanProperty("plugin.trustmanagement.enabled", false);

//        // Listen to system property events
        PropertyEventDispatcher.addListener(this);


	}
	
	public void destroyPlugin()
	{
		
		// Stop listening to system property events
        PropertyEventDispatcher.removeListener(this);

	}
	
	public void addIP(String IP, int port, int validityPeriod)
	{
		RemoteServerConfiguration rsc = new RemoteServerConfiguration(IP);
		rsc.setRemotePort(port);
		RemoteServerManager.allowAccess(rsc);
		
		// schedule the IP config to be removed after the validityPeriod
		Calendar expiryTime= Calendar.getInstance();
    	expiryTime.add(Calendar.MINUTE, validityPeriod);
    	
		Timer timer = new Timer();
		timer.schedule(new RemoveIPConfig(IP), expiryTime.getTime());
		
	}
	
    /**
     * Returns the password that only valid requests should know.
     *
     * @return the secret key.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password that grants permission to use the trust management.
     *
     * @param secret the secret key.
     */
    public void setPassword(String password) {
        JiveGlobals.setProperty("plugin.trustmanagement.password", password);
        this.password = password;
    }

    /**
     * Returns true if the trust management service is enabled. If not enabled, it will not accept
     * requests to create new accounts.
     *
     * @return true if the trust management service is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Enables or disables the trust management service. If not enabled, it will not accept
     * requests to create new accounts.
     *
     * @param enabled true if the trust management service should be enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        JiveGlobals.setProperty("plugin.trustmanagement.enabled",  enabled ? "true" : "false");
    }
    

	public void propertyDeleted(String property, Map<String, Object> params)
	{
        if (property.equals("plugin.trustmanagement.password")) {
            this.password = "";
        }
        else if (property.equals("plugin.trustmanagement.enabled")) {
            this.enabled = false;
        }
		
	}



	public void propertySet(String property, Map<String, Object> params)
	{
        if (property.equals("plugin.trustmanagement.password")) {
            this.password = (String)params.get("value");
        }
        else if (property.equals("plugin.trustmanagement.enabled")) {
            this.enabled = Boolean.parseBoolean((String)params.get("value"));
        }
		
	}


	@Override
	public void xmlPropertyDeleted(String property, Map<String, Object> params)
	{
		// TODO Auto-generated method stub
		
	}

 
	@Override
	public void xmlPropertySet(String property, Map<String, Object> params)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Add a the string contentFilter to the ContentFilter Plugin
	 * @param contentFilter
	 */
	public void addContentFilter(String contentFilter)
	{
		String urlString = 
			"http://127.0.0.1:9090/plugins/contentFilter/contentfilter?" +
			"contentFilter="+StringEscapeUtils.escapeHtml(contentFilter);

		try {
			URL url = new URL(urlString);
			System.out.println(url);
			url.openStream();
			url=null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addUserID (String userID, int validityPeriod ){
		String urlString = 
			"http://127.0.0.1:9090/plugins/packetFilter/packetfilter?" +
			"userID="+StringEscapeUtils.escapeHtml(userID)+
			"&validityPeriod="+validityPeriod;

		try {
			URL url = new URL(urlString);
			System.out.println(url);
			url.openStream();
			url=null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * A TimerTask that removes the configuration of the constructor
	 * specified IP address from the XMPP server.
	 */
	private final class RemoveIPConfig extends TimerTask{
		public String IP;
		
		public RemoveIPConfig(String IP)
		{
			this.IP = IP;
		}
		public void run()
		{
			RemoteServerManager.deleteConfiguration(IP);
		}
		
	}

}
