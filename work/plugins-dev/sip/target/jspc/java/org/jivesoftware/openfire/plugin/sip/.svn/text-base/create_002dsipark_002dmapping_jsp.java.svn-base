package org.jivesoftware.openfire.plugin.sip;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.openfire.sip.sipaccount.SipAccount;
import org.jivesoftware.openfire.sip.sipaccount.SipAccountDAO;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.LocaleUtils;
import org.jivesoftware.util.Log;
import org.jivesoftware.util.ParamUtils;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.openfire.sip.tester.stack.SIPTest;

public final class create_002dsipark_002dmapping_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_fmt_message_key_nobody;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_fmt_message_key_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_fmt_message_key_nobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");

    boolean save = request.getParameter("save") != null;
    boolean test = request.getParameter("test") != null;
    SIPTest.Result testResult = null;
    String node = request.getParameter("node");
    String username = ParamUtils.getParameter(request, "sipusername");
    String authusername = ParamUtils.getParameter(request, "authusername");
    String displayname = ParamUtils.getParameter(request, "displayname");
    String password = ParamUtils.getParameter(request, "sippassword");
    String server = ParamUtils.getParameter(request, "server");
    boolean enabled = ParamUtils.getBooleanParameter(request, "enabled");
    String voicemail = ParamUtils.getParameter(request, "voicemail");
    String outboundproxy = ParamUtils.getParameter(request, "outboundproxy");
    boolean promptCredentials = ParamUtils.getBooleanParameter(request,"promptCredentials",false);

    // Handle a cancel
    if (request.getParameter("cancel") != null) {
        response.sendRedirect("sipark-user-summary.jsp?username=" + URLEncoder.encode(node, "UTF-8"));
        return;
    }

    SipAccount sipAccount = null;
    Map<String, String> errors = new HashMap<String, String>();
    String errorMessage = null;

    if (save || test) {
        // Validate params
        if (node == null || "".equals(node) || node.contains("@")) {
            errors.put("node", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.xmpp-node", "sip");
        } else if ((username == null || "".equals(username)) && !promptCredentials) {
            errors.put("username", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.sip-username", "sip");
        } else if ((authusername == null || "".equals(authusername)) && !promptCredentials) {
            errors.put("authusername", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.authusername", "sip");
        } else if (displayname == null || "".equals(displayname)) {
            errors.put("displayname", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.displayname", "sip");
        } else if ((password == null || "".equals(password)) && !promptCredentials) {
            errors.put("password", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.password", "sip");
        } else if (server == null || "".equals(server)) {
            errors.put("server", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.server", "sip");
        } else if (voicemail == null || "".equals(voicemail)) {
            errors.put("voicemail", "");
            errorMessage = LocaleUtils.getLocalizedString("sipark.user.create.valid.voicemail", "sip");
        } else if(outboundproxy == null){
            outboundproxy = "";
        }

        if (errors.isEmpty()) {
            sipAccount = new SipAccount(node);
            sipAccount.setSipUsername(username);
            sipAccount.setAuthUsername(authusername);
            sipAccount.setDisplayName(displayname);
            sipAccount.setPassword(password);
            sipAccount.setServer(server);
            sipAccount.setEnabled(enabled);
            sipAccount.setVoiceMailNumber(voicemail);
            sipAccount.setOutboundproxy(outboundproxy);
            sipAccount.setPromptCredentials(promptCredentials);

            if (test) {
                try {

                    InetAddress localAddress = InetAddress.getByName(JiveGlobals.getProperty("xmpp.domain",
                            JiveGlobals.getXMLProperty("network.interface", "localhost")));

                    SIPTest sipTest = new SIPTest(localAddress, sipAccount);

                    if (sipTest != null) {
                        sipTest.test(5000, 2);
                        testResult = sipTest.getResult();
                    } else
                        testResult = SIPTest.Result.NetworkError;

                } catch (Exception e) {
                    testResult = SIPTest.Result.NetworkError;
                    Log.error(e);
                }

                if (node != null && !"".equals(node)) {
                    sipAccount = new SipAccount(node);
                    SipAccount sp = SipAccountDAO.getAccountByUser(sipAccount.getUsername());

                    if (sp != null) {
                        sipAccount = sp;
                    }
                }
            } else {

                try {

                    if (SipAccountDAO.getAccountByUser(sipAccount.getUsername()) != null) {
                        SipAccountDAO.update(sipAccount);
                    } else {
                        SipAccountDAO.insert(sipAccount);
                    }

                    response.sendRedirect("sipark-user-summary.jsp?username=" + URLEncoder.encode(node, "UTF-8"));
                    return;

                } catch (SQLException e) {
                    errors.put("saveSettings", e.getMessage());
                    Log.error(e);
                }
            }
        }

    } else {
        if (node != null && !"".equals(node)) {
            sipAccount = new SipAccount(node);
            SipAccount sp = SipAccountDAO.getAccountByUser(sipAccount.getUsername());

            if (sp != null) {
                sipAccount = sp;
            }
        }
        if (sipAccount == null) {
            enabled = true;
            server = JiveGlobals.getProperty("phone.sipServer", "");
            voicemail = JiveGlobals.getProperty("phone.voiceMail", "");
        } else {
            username = sipAccount.getSipUsername();
            authusername = sipAccount.getAuthUsername();
            displayname = sipAccount.getDisplayName();
            password = sipAccount.getPassword();
            server = sipAccount.getServer();
            enabled = sipAccount.isEnabled();
            voicemail = sipAccount.getVoiceMailNumber();
            outboundproxy = sipAccount.getOutboundproxy();
            promptCredentials = sipAccount.isPromptCredentials();
        }
    }

      out.write("\r\n\r\n\r\n<html>\r\n<head>\r\n    <title>");
 if (sipAccount == null) { 
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_0(_jspx_page_context))
        return;
      out.write("\r\n        ");
 } else { 
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_1(_jspx_page_context))
        return;
      out.write("\r\n        ");
 } 
      out.write("</title>\r\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"/style/global.css\">\r\n    <meta name=\"pageID\" content=\"sipark-user-summary\"/>\r\n    <script src=\"/js/prototype.js\" type=\"text/javascript\"></script>\r\n    <script src=\"/js/scriptaculous.js\" type=\"text/javascript\"></script>\r\n    <style type=\"text/css\">\r\n        .div-border {\r\n            border: 1px;\r\n            border-color: #ccc;\r\n            border-style: dotted;\r\n        }\r\n    </style>\r\n</head>\r\n\r\n<body>\r\n\r\n");
 if (errors.size() > 0) { 
      out.write("\r\n<div class=\"error\">\r\n    ");
      out.print( errorMessage );
      out.write("\r\n</div>\r\n<br/>\r\n");
 } 
      out.write("\r\n\r\n\r\n");

    if (testResult != null) {
        if (SIPTest.Result.Successfully.equals(testResult)) { 
      out.write("\r\n<div class=\"success\">\r\n    SIP Account Successfully Tested.\r\n</div>\r\n<br>\r\n");
 } else { 
      out.write("\r\n<div class=\"error\">\r\n    SIP Account problem: ");
      out.print(testResult.toString());
      out.write("\r\n    <i>\r\n        ");

            if (testResult.equals(SIPTest.Result.Forbidden)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_2(_jspx_page_context))
        return;
      out.write("\r\n        ");

        } else if (testResult.equals(SIPTest.Result.NetworkError)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_3(_jspx_page_context))
        return;
      out.write("\r\n        ");

        } else if (testResult.equals(SIPTest.Result.Timeout)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_4(_jspx_page_context))
        return;
      out.write("\r\n        ");


        } else if (testResult.equals(SIPTest.Result.WrongUser)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_5(_jspx_page_context))
        return;
      out.write("\r\n        ");


        } else if (testResult.equals(SIPTest.Result.WrongAuthUser)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_6(_jspx_page_context))
        return;
      out.write("\r\n        ");


        } else if (testResult.equals(SIPTest.Result.WrongPass)) {
        
      out.write("\r\n        ");
      if (_jspx_meth_fmt_message_7(_jspx_page_context))
        return;
      out.write("\r\n        ");

            }
        
      out.write("\r\n    </i>\r\n\r\n</div>\r\n<br>\r\n");
 }
} 
      out.write("\r\n\r\n<p>\r\n    ");
      if (_jspx_meth_fmt_message_8(_jspx_page_context))
        return;
      out.write("\r\n</p>\r\n\r\n\r\n<form id=\"urlForm\" name=\"urlForm\" action=\"create-sipark-mapping.jsp\" method=\"post\">\r\n<table class=\"div-border\" cellpadding=\"3\">\r\n\r\n    ");
 if (sipAccount == null) { 
      out.write("\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_9(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"node\" value=\"");
      out.print( (node != null ? node : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n    ");
 } else {
      out.write("\r\n    <input type=\"hidden\" name=\"node\" value=\"");
      out.print( node );
      out.write("\">\r\n    ");
 } 
      out.write("\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_10(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"sipusername\"\r\n                   value=\"");
      out.print( (username != null ? username : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_11(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"authusername\"\r\n                   value=\"");
      out.print( (authusername != null ? authusername : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_12(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"displayname\"\r\n                   value=\"");
      out.print( (displayname != null ? displayname : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_13(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"password\" size=\"20\" maxlength=\"100\" name=\"sippassword\"\r\n                   value=\"");
      out.print( (password != null ? password : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_14(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"server\"\r\n                   value=\"");
      out.print( (server != null ? server : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_15(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"outboundproxy\"\r\n                   value=\"");
      out.print( (outboundproxy != null ? outboundproxy : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_16(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"text\" size=\"20\" maxlength=\"100\" name=\"voicemail\"\r\n                   value=\"");
      out.print( (voicemail != null ? voicemail : "") );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n\r\n    ");
 if (sipAccount != null) { 
      out.write("\r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_17(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"checkbox\" size=\"20\" maxlength=\"100\" name=\"promptCredentials\" ");
      out.print( promptCredentials ? "checked" : "" );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n    \r\n    <tr>\r\n        <td align=\"left\" width=\"150\">\r\n            ");
      if (_jspx_meth_fmt_message_18(_jspx_page_context))
        return;
      out.write("\r\n            :&nbsp\r\n        </td>\r\n        <td><input type=\"checkbox\" size=\"20\" maxlength=\"100\" name=\"enabled\" ");
      out.print( enabled ? "checked" : "" );
      out.write("\">\r\n        </td>\r\n    </tr>\r\n    ");
 } else {
      out.write("\r\n    <input type=\"hidden\" name=\"enabled\" value=\"");
      out.print( enabled );
      out.write("\">\r\n    ");
 } 
      out.write("\r\n\r\n    <tr>\r\n        <td></td>\r\n        <td><input type=\"submit\" name=\"save\"\r\n                   value=\"");
      out.print( sipAccount != null ? LocaleUtils.getLocalizedString("sipark.user.create.save.changes", "sip") : LocaleUtils.getLocalizedString("create", "sip")  );
      out.write("\"/>\r\n            &nbsp;<input type=\"submit\" name=\"cancel\" value=\"");
      if (_jspx_meth_fmt_message_19(_jspx_page_context))
        return;
      out.write("\">\r\n            ");
 if (sipAccount != null || test) { 
      out.write("\r\n            &nbsp;<input type=\"submit\" name=\"test\" value=\"");
      if (_jspx_meth_fmt_message_20(_jspx_page_context))
        return;
      out.write("\">\r\n            ");
}
      out.write("\r\n        </td>\r\n    </tr>\r\n\r\n</table>\r\n\r\n</form>\r\n\r\n</body>\r\n</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_message_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_0.setParent(null);
    _jspx_th_fmt_message_0.setKey("sipark.user.create.title");
    int _jspx_eval_fmt_message_0 = _jspx_th_fmt_message_0.doStartTag();
    if (_jspx_th_fmt_message_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_0);
    return false;
  }

  private boolean _jspx_meth_fmt_message_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_1.setParent(null);
    _jspx_th_fmt_message_1.setKey("sipark.user.update.title");
    int _jspx_eval_fmt_message_1 = _jspx_th_fmt_message_1.doStartTag();
    if (_jspx_th_fmt_message_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_1);
    return false;
  }

  private boolean _jspx_meth_fmt_message_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_2.setParent(null);
    _jspx_th_fmt_message_2.setKey("sip.test.error.forbidden");
    int _jspx_eval_fmt_message_2 = _jspx_th_fmt_message_2.doStartTag();
    if (_jspx_th_fmt_message_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_2);
    return false;
  }

  private boolean _jspx_meth_fmt_message_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_3.setParent(null);
    _jspx_th_fmt_message_3.setKey("sip.test.error.networkerror");
    int _jspx_eval_fmt_message_3 = _jspx_th_fmt_message_3.doStartTag();
    if (_jspx_th_fmt_message_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_3);
    return false;
  }

  private boolean _jspx_meth_fmt_message_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_4.setParent(null);
    _jspx_th_fmt_message_4.setKey("sip.test.error.timeout");
    int _jspx_eval_fmt_message_4 = _jspx_th_fmt_message_4.doStartTag();
    if (_jspx_th_fmt_message_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_4);
    return false;
  }

  private boolean _jspx_meth_fmt_message_5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_5.setParent(null);
    _jspx_th_fmt_message_5.setKey("sip.test.error.wronguser");
    int _jspx_eval_fmt_message_5 = _jspx_th_fmt_message_5.doStartTag();
    if (_jspx_th_fmt_message_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_5);
    return false;
  }

  private boolean _jspx_meth_fmt_message_6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_6 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_6.setParent(null);
    _jspx_th_fmt_message_6.setKey("sip.test.error.wrongauthuser");
    int _jspx_eval_fmt_message_6 = _jspx_th_fmt_message_6.doStartTag();
    if (_jspx_th_fmt_message_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_6);
    return false;
  }

  private boolean _jspx_meth_fmt_message_7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_7 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_7.setParent(null);
    _jspx_th_fmt_message_7.setKey("sip.test.error.wronpass");
    int _jspx_eval_fmt_message_7 = _jspx_th_fmt_message_7.doStartTag();
    if (_jspx_th_fmt_message_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_7);
    return false;
  }

  private boolean _jspx_meth_fmt_message_8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_8 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_8.setParent(null);
    _jspx_th_fmt_message_8.setKey("sipark.user.create.description");
    int _jspx_eval_fmt_message_8 = _jspx_th_fmt_message_8.doStartTag();
    if (_jspx_th_fmt_message_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_8);
    return false;
  }

  private boolean _jspx_meth_fmt_message_9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_9 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_9.setParent(null);
    _jspx_th_fmt_message_9.setKey("sip.account.xmpp.username");
    int _jspx_eval_fmt_message_9 = _jspx_th_fmt_message_9.doStartTag();
    if (_jspx_th_fmt_message_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_9);
    return false;
  }

  private boolean _jspx_meth_fmt_message_10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_10 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_10.setParent(null);
    _jspx_th_fmt_message_10.setKey("sip.account.sip.username");
    int _jspx_eval_fmt_message_10 = _jspx_th_fmt_message_10.doStartTag();
    if (_jspx_th_fmt_message_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_10);
    return false;
  }

  private boolean _jspx_meth_fmt_message_11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_11 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_11.setParent(null);
    _jspx_th_fmt_message_11.setKey("sip.account.authusername");
    int _jspx_eval_fmt_message_11 = _jspx_th_fmt_message_11.doStartTag();
    if (_jspx_th_fmt_message_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_11);
    return false;
  }

  private boolean _jspx_meth_fmt_message_12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_12 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_12.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_12.setParent(null);
    _jspx_th_fmt_message_12.setKey("sip.account.displayname");
    int _jspx_eval_fmt_message_12 = _jspx_th_fmt_message_12.doStartTag();
    if (_jspx_th_fmt_message_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_12);
    return false;
  }

  private boolean _jspx_meth_fmt_message_13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_13 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_13.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_13.setParent(null);
    _jspx_th_fmt_message_13.setKey("sip.account.password");
    int _jspx_eval_fmt_message_13 = _jspx_th_fmt_message_13.doStartTag();
    if (_jspx_th_fmt_message_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_13);
    return false;
  }

  private boolean _jspx_meth_fmt_message_14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_14 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_14.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_14.setParent(null);
    _jspx_th_fmt_message_14.setKey("sip.account.server");
    int _jspx_eval_fmt_message_14 = _jspx_th_fmt_message_14.doStartTag();
    if (_jspx_th_fmt_message_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_14);
    return false;
  }

  private boolean _jspx_meth_fmt_message_15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_15 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_15.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_15.setParent(null);
    _jspx_th_fmt_message_15.setKey("sip.account.outboundproxy");
    int _jspx_eval_fmt_message_15 = _jspx_th_fmt_message_15.doStartTag();
    if (_jspx_th_fmt_message_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_15);
    return false;
  }

  private boolean _jspx_meth_fmt_message_16(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_16 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_16.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_16.setParent(null);
    _jspx_th_fmt_message_16.setKey("sip.account.voicemail");
    int _jspx_eval_fmt_message_16 = _jspx_th_fmt_message_16.doStartTag();
    if (_jspx_th_fmt_message_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_16);
    return false;
  }

  private boolean _jspx_meth_fmt_message_17(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_17 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_17.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_17.setParent(null);
    _jspx_th_fmt_message_17.setKey("sip.account.promptCredentials");
    int _jspx_eval_fmt_message_17 = _jspx_th_fmt_message_17.doStartTag();
    if (_jspx_th_fmt_message_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_17);
    return false;
  }

  private boolean _jspx_meth_fmt_message_18(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_18 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_18.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_18.setParent(null);
    _jspx_th_fmt_message_18.setKey("sip.account.enabled");
    int _jspx_eval_fmt_message_18 = _jspx_th_fmt_message_18.doStartTag();
    if (_jspx_th_fmt_message_18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_18);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_18);
    return false;
  }

  private boolean _jspx_meth_fmt_message_19(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_19 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_19.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_19.setParent(null);
    _jspx_th_fmt_message_19.setKey("cancel");
    int _jspx_eval_fmt_message_19 = _jspx_th_fmt_message_19.doStartTag();
    if (_jspx_th_fmt_message_19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_19);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_19);
    return false;
  }

  private boolean _jspx_meth_fmt_message_20(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_message_20 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _jspx_tagPool_fmt_message_key_nobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_message_20.setPageContext(_jspx_page_context);
    _jspx_th_fmt_message_20.setParent(null);
    _jspx_th_fmt_message_20.setKey("test");
    int _jspx_eval_fmt_message_20 = _jspx_th_fmt_message_20.doStartTag();
    if (_jspx_th_fmt_message_20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_20);
      return true;
    }
    _jspx_tagPool_fmt_message_key_nobody.reuse(_jspx_th_fmt_message_20);
    return false;
  }
}
