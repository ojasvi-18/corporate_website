<%@page import="com.zillious.corporate_website.ui.beans.UIBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@page import="com.zillious.corporate_website.ui.beans.I18NBean"%>
<%@page import="com.zillious.corporate_website.ui.beans.WebsiteBean"%>
<%@page import="com.zillious.corporate_website.i18n.Language"%>
<%@page import="com.zillious.corporate_website.utils.HtmlUtility"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
	WebsiteActions currentAction = UIBean.getCurrentUIAction(secureRequest);
	String defLanguage = null; 
	  defLanguage = I18NBean.getSelectedLanguage(secureRequest); 
	if (defLanguage == null) {
	  defLanguage = Language.ENGLISH.getCode(); 
	}
	secureRequest.setAttribute("defLanguage", defLanguage);
	Map<String, String> languageMap = Language.getLanguageMapForSelection(defLanguage);
%>
<header class="app-header" id="app-header">
    <div class="container">
        <div class="header-wrapper">
            <!-- Logo -->
            <div class="logo" id="logo">
                <!-- image logo -->
                <a href="<%= WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true) %>" class="image-logo">
                    <img src="/static/images/logo.png" alt="Zillious Solutions" />
                </a>
            </div>
            <!-- Main-Nav -->
            <nav class="main-nav">
                <ul>
                    <li <%if (currentAction == WebsiteActions.HOME) {%>class="active"<%} %>><a href="<%= WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-home"></i>&nbsp; <z:message key="corp.nav.home">Home</z:message></a></li>
                    <li <%if (currentAction == WebsiteActions.ABOUT) {%>class="active"<%} %>><a href="<%= WebsiteActions.ABOUT.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-sitemap"></i>&nbsp; <z:message key="corp.nav.about">About</z:message></a></li>
                    <li <%if (currentAction == WebsiteActions.PRODUCTS) {%>class="active"<%} %>><a href="<%= WebsiteActions.PRODUCTS.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-cubes"></i>&nbsp; <z:message key="corp.nav.products">Products</z:message></a></li>
                    <li <%if (currentAction == WebsiteActions.SERVICES) {%>class="active"<%} %>><a href="<%= WebsiteActions.SERVICES.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-cogs"></i>&nbsp; <z:message key="corp.nav.services">Services</z:message></a></li>
                    <li <%if (currentAction == WebsiteActions.CAREERS) {%>class="active"<%} %>><a href="<%= WebsiteActions.CAREERS.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-briefcase"></i>&nbsp; <z:message key="corp.nav.careers">Careers</z:message></a></li>
                    <li <%if (currentAction == WebsiteActions.CONTACT) {%>class="active"<%} %>><a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-envelope"></i>&nbsp; <z:message key="corp.nav.contact">Contact</z:message></a></li>

<%-- 					<% if(WebsiteBean.isUserLoggedIn(secureRequest)) {%>
						<li class="dropdown">
							<a href="#">Administer</a>
							<ul style="display: none;" class="dropdown-menu bold">
				    	
				    	<%	if(WebsiteBean.isUIActionAllowed(secureRequest, WebsiteActions.ATTENDANCETRACKER)){ %>      
        					<li><a href="<%= WebsiteActions.ATTENDANCETRACKER.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-envelope"></i>&nbsp; <z:message key="corp.nav.attendancetracker">Track Attendance</z:message></a></li>
						<%} %>
				    	<%	if(WebsiteBean.isUIActionAllowed(secureRequest, WebsiteActions.MANAGE_USERS)){ %>      
        					<li><a href="<%= WebsiteActions.MANAGE_USERS.getActionURL(secureRequest, secureResponse, null, true) %>"><i class="fa fa-envelope"></i>&nbsp; <z:message key="corp.nav.attendancetracker">Manage Users</z:message></a></li>
						<%} %>
					 </ul>
					 </li>
					<%
						}
				    %>                     --%>
                    <li>
					<% 
						int i = 0;
						for(String key : languageMap.keySet()) { %>
						<%--<option value="<%=key%>" <% if(key.equals(defLanguage)) {%> selected <%} %>><%=languageMap.get(key) %></option> --%>
							<a href="<%=HtmlUtility.encodeForHTMLAttribute(WebsiteActions.CHANGE_LOCALE.getActionURL(secureRequest,secureResponse, new String[] {key}, true))%>"><%=languageMap.get(key)%></a>
					<% 
							if(i < languageMap.keySet().size() -1) { 
					%>
								|
					<%
								}
								i++;
							} 
					%>
                    </li>
                </ul>
                <%--
                <div class="icon-round-lrg-plain search-toggle">
                    <i class="fa fa-search"></i>
                </div>
                --%>
            </nav>
        </div>
    </div>
</header>
