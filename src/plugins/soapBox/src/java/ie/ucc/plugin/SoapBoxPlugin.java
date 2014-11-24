package ie.ucc.plugin;

import java.io.File;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.server.RemoteServerConfiguration;
import org.jivesoftware.openfire.server.RemoteServerManager;

/**
 * 
 * @author Wayne Mac Adams
 * Basic plugin that adds SoapBox.com to the s2s whitelist
 */
public class SoapBoxPlugin implements Plugin
{



	String soapBox = "SoapBox.com";
	public void addSoapBox()
	{
		RemoteServerConfiguration rsc = new RemoteServerConfiguration(soapBox);
		RemoteServerManager.allowAccess(rsc);
	}
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void destroyPlugin()
	{
		// TODO Auto-generated method stub
		
	}

}
