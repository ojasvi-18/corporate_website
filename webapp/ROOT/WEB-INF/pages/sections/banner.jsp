<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>
<section class="section banner alt">
    <div class="container">
        <div class="content">
            <h4><z:message key="corp.banner.title">Start your <b>new project</b> with a <i>fresh</i> approach...</z:message></h4>
            <p><z:message key="corp.banner.subtitle1">leave your customers with an experience to remember.</z:message> <a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.common.contactus">Contact us</z:message></a> <z:message key="corp.common.now">now</z:message>.</p>
        </div>
        <div class="meta">
        </div>
    </div>
</section>
