package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.plugin.rules.Pass;
import org.jivesoftware.openfire.plugin.rules.Rule;
import org.jivesoftware.openfire.plugin.rules.RuleManager;
import org.jivesoftware.openfire.plugin.rules.RuleManagerProxy;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.util.Log;
import org.xmpp.packet.Packet;

public class PacketFilterPlugin implements Plugin, PacketInterceptor {

    private static PluginManager pluginManager;

    public PacketFilterPlugin() {
        interceptorManager = InterceptorManager.getInstance();
    }

    //Packet Filter
    private PacketFilter pf;

    //Hook for intercpetorn
    private InterceptorManager interceptorManager;


    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        Log.info("Packet Filter loaded...");
        interceptorManager.addInterceptor(this);
        this.pluginManager = manager;
        pf = PacketFilter.getInstance();
        RuleManager ruleManager = new RuleManagerProxy();
        pf.setRuleManager(ruleManager);
        
    }

    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
    }


    public String getName() {
        return "packetFilter";
        
    }
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {

        if (processed) {
            return;
        }
        
        Rule rule = pf.findMatch(packet);

        if (rule != null) {
            rule.doAction(packet);
        }
    }

	public void addRuleViaTMAgent(String userID, int validityPeriod)
	{
    	RuleManager rm = new RuleManagerProxy();
    	Rule rule = null;
    	rule = new Pass();

    	rule.setDescription("Rule added via TM Plugin");
//    	rule.setDisplayName("MyRule");

    	rule.setPacketType(Rule.PacketType.Any);

    	rule.setSource(userID);
    	rule.setSourceType(Rule.SourceDestType.User);
    	rule.isDisabled(false);
    	rule.doLog(true);

    	rule.setDestination(Rule.SourceDestType.Any.toString());
    	rule.setDestType(Rule.SourceDestType.Any);
    	// order of 1
    	rm.addRule(rule);
    	rm.moveRuleOrder(Integer.parseInt(rule.getRuleId()), 0);
    	
    	Calendar expiryTime= Calendar.getInstance();
    	expiryTime.add(Calendar.MINUTE, validityPeriod);
    	
    	Timer timer = new Timer();
		timer.schedule(new RemoveRule(rule.getRuleId()), expiryTime.getTime());
		
	}
	
	/**
	 * A TimerTask that removes the configuration of the constructor
	 * specified IP address from the XMPP server.
	 * @author Wayne Mac Adams
	 */
	private final class RemoveRule extends TimerTask{
		public String ruleID;
		private RuleManager rm;
		
		public RemoveRule(String ruleID)
		{
			this.ruleID = ruleID;
		}
		public void run()
		{
			rm = new RuleManagerProxy();
			rm.deleteRule(ruleID);
			rm = null;
		}
		
	}
}       
