<%@page import="com.zillious.corporate_website.ui.beans.MessageBean"%>
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%
ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	String success = MessageBean.getMessage(secureRequest, MessageBean.MessageType.MSG_SUCCESS);
	String error = MessageBean.getMessage(secureRequest, MessageBean.MessageType.MSG_ERROR);
%>
<% if (success != null) { %><center><div class="msg"><%=success%></div></center><br /><% } %>
<% if (error != null) { %><center><div class="msg"><%=error%></div></center><br /><% } %>
