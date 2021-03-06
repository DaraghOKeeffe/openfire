<%@ page import="java.util.*,
                 org.jivesoftware.openfire.XMPPServer,
                 org.jivesoftware.util.*,
                 ie.ucc.plugin.trustManagement.TrustManagementPlugin"
    errorPage="error.jsp"
%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<%-- Define Administration Bean --%>
<jsp:useBean id="admin" class="org.jivesoftware.util.WebManager"  />
<c:set var="admin" value="${admin.manager}" />
<% admin.init(request, response, session, application, out ); %>

<%  // Get parameters
    boolean save = request.getParameter("save") != null;
    boolean success = request.getParameter("success") != null;
    String password = ParamUtils.getParameter(request, "password");
    boolean enabled = ParamUtils.getBooleanParameter(request, "enabled");

    TrustManagementPlugin plugin = (TrustManagementPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("trustmanagement");

    
    

//    out.println("Debug, plugins: \n"+XMPPServer.getInstance().getPluginManager().getPluginNames());
    // Handle a save
    Map errors = new HashMap();
    if (save) {
    	if (errors.size() == 0) {
            plugin.setEnabled(enabled);
            plugin.setPassword(password);
            response.sendRedirect("trustmanagement.jsp?success=true");
            return;
    	}
    }

    password = plugin.getPassword();
    enabled = plugin.isEnabled();
%>

<html>
    <head>
        <title>Trust Management Properties</title>
        <meta name="pageID" content="trustManagement"/>
    </head>
    <body>


<p>
Use the form below to enable or disable the Trust Management plugin and set the password.
By default the Trust Management plugin is disabled.
</p>

<%  if (success) { %>

    <div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
    <tbody>
        <tr><td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0"></td>
        <td class="jive-icon-label">
            Trust Management properties edited successfully.
        </td></tr>
    </tbody>
    </table>
    </div><br>
<% } %>

<form action="trustmanagement.jsp?save" method="post">

<fieldset>
    <legend>Trust Management</legend>
    <div>
    <p>
    A service that allows potential servers to present a set of credentials proving that they should be allowed access.
    If they prove they should be allowed access, the ACLs of this server will be updated.
    </p>

    <p>To use the service users must provide a password which can be set below.
    </p>
    <ul>
        <input type="radio" name="enabled" value="true" id="rb01"
        <%= ((enabled) ? "checked" : "") %>>
        <label for="rb01"><b>Enabled</b> - Trust management requests will be processed.</label>
        <br>
        <input type="radio" name="enabled" value="false" id="rb02"
         <%= ((!enabled) ? "checked" : "") %>>
        <label for="rb02"><b>Disabled</b> - Trust management requests will be ignored.</label>
        <br><br>

        <label for="text_password">Password:</label>
        <input type="text" name="password" value="<%= password %>" id="text_password">
        <br><br>
    </ul>
    </div>
</fieldset>

<br><br>

<input type="submit" value="Save Settings">
</form>


</body>
</html>