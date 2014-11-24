<%@ page import="org.jivesoftware.openfire.XMPPServer,
                 org.jivesoftware.openfire.group.Group"
        %>
<%@ page import="org.jivesoftware.openfire.plugin.component.ComponentList" %>
<%@ page import="org.jivesoftware.openfire.plugin.rules.*" %>
<%@ page import="org.jivesoftware.openfire.user.UserManager" %>
<%@ page import="org.jivesoftware.util.ParamUtils" %>
<%@ page import="org.xmpp.component.Component" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.jivesoftware.openfire.RoutingTable" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<jsp:useBean id="webManager" class="org.jivesoftware.util.WebManager"/>
<%
    webManager.init(request, response, session, application, out);
    Collection<Group> groups = webManager.getGroupManager().getGroups();


    ComponentList cList = ComponentList.getInstance();
    RuleManager rm = new RuleManagerProxy();
    Rule rule = null;
    //Get Action
    boolean create = request.getParameter("create") != null;
    boolean cancel = request.getParameter("cancel") != null;

    //Get data
    String packetAction = ParamUtils.getParameter(request, "packetAction");
    String disable = ParamUtils.getParameter(request, "disable");
    String packetType = ParamUtils.getParameter(request, "packetType");
    String source = ParamUtils.getParameter(request, "source");
    String destination = ParamUtils.getParameter(request, "destination");
    String log = ParamUtils.getParameter(request, "log");
    String description = ParamUtils.getParameter(request, "description");
    String order = ParamUtils.getParameter(request, "order");


    Rule.SourceDestType[] type = Rule.SourceDestType.values();

    Collection<String> userList = UserManager.getInstance().getUsernames();
    String serverName = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
    Collection<String> components = cList.getComponentDomains();
    
    Map<String, String> errors = new HashMap<String, String>();
    String sourceJID = "admin";
    String destJID = "";


            rule = new Pass();
       
            rule.setDescription("testRule");
            rule.setDisplayName("MyRule");
            
            rule.setPacketType(Rule.PacketType.Message);
           
            rule.setSource(sourceJID);
            rule.setSourceType(Rule.SourceDestType.Any);
            rule.isDisabled(false);
            rule.doLog(true);
            
            //rule.setDestination(destination);
            rule.setDestType(Rule.SourceDestType.Any);


            rm.addRule(rule);
            response.sendRedirect("pf-main.jsp");



%>
<html>

</html>

