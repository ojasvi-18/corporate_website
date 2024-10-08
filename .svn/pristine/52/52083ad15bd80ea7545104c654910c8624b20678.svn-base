<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType"%>
<%@page
	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
    ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
    WebsiteActions action = WebsiteActions.deserializeUIAction(secureRequest.getParameter("The action that is currently active right now", "website_action", ZilliousSecurityRequestType.DEFAULT_SAFE_STRING));
%>


<nav class="breadcrumb">
	<div class="container">
		<ul>
			<li class="home"><a
				href="<%= WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true) %>"><i
					class="fa fa-home"></i></a></li>
			<li><a
				href="<%= WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.home">Home</z:message></a></li>
			<% switch(action) { 
                    
                    case ABOUT:
                    %>
			<li class="current"><a
				href="<%= WebsiteActions.ABOUT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.breadcrumb.about.text">About Zillious</z:message></a></li>
			<% 
	                    break;
                    case CAREERS:
                    %>
			<li class="current"><a
				href="<%= WebsiteActions.CAREERS.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.careers">Careers</z:message></a></li>
			<% 
	                    break;
                    case CONTACT:
                    %>
			<li class="current"><a
				href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.contact">Contact</z:message></a></li>
			<% 
	                    break;
                    case SERVICES:
                    %>
			<li class="current"><a
				href="<%= WebsiteActions.ABOUT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.services">Services</z:message></a></li>
			<% 
	                    break;
                    case TERMS:
                    %>
			<li class="current"><a
				href="<%= WebsiteActions.TERMS.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.terms">Terms</z:message></a></li>
			<% 
	                    break;
                    case PRIVACY:
                    %>
            <li class="current"><a
				href="<%= WebsiteActions.PRIVACY.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.privacy">Privacy</z:message></a></li>
			<% 
	                    break;
                    case PRODUCTS:
                    %>
                    <li class="current"><a
				href="<%= WebsiteActions.PRODUCTS.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message
						key="corp.nav.products">Products</z:message></a></li>
			<% 
	                    break;
                   default:
			}
                    %>
		</ul>
	</div>
</nav>