<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>
<section class="section secondary more-info" id="s-more-info">
    <div class="container">
        <div class="row">
            <div class="span-6 info-video">
                <div class="auto-resizable-iframe">
                    <div>
                        <a href="/static/downloads/Zillious-Travolution_Brochure.pdf" target="_blank"><img src="/static/images/brochure.jpg" alt="" /></a>
                    </div>
                </div>
            </div>
            <div class="span-6 more-info-meta">
                <div class="section-title">
                    <h2><z:message key="corp.moreinfo.title">More Info</z:message></h2>
                    <h3><i><z:message key="corp.moreinfo.subtitle">Learn More About Us</z:message></i></h3>
                </div>
                <p><z:message key="corp.moreinfo.text1">Read more about us in</z:message> <a href="/static/downloads/Zillious-Travolution_Brochure.pdf" target="_blank"><z:message key="corp.common.ourbrochure">our brochure</z:message></a>.</p>             
                <ul class="bullet-list min-bp2">
                    <li><z:message key="corp.moreinfo.bullets.productoverviewkeyfeatures">Product overview &amp; Key features</z:message></li>
                    <li><z:message key="corp.moreinfo.bullets.channelwisemidoffice">Channel-wise &amp; Mid office highlights</z:message></li>
                    <li><z:message key="corp.moreinfo.bullets.ourmodulararch">Our Modular Architecture</z:message></li>
                </ul>
                <p class="button-set">
                    <a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>" class="button round brand-1"><z:message key="corp.common.getintouch">Get in Touch</z:message></a>
                </p>
            </div>
        </div>
    </div>
</section>
