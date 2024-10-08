<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>

<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
    <jsp:param name="title" value="Privacy Policy"/>
    <jsp:param name="meta_description" value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually."/>
</jsp:include>

<body class="further contact">
    <%-- SITE CONTENT --%>
    <div id="site-content">

        <%-- Header --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/header_mainnav.jsp" />

        <%-- Sub-Header --%>
        <section class="hero sub-header">
            <div class="container inactive">
                <div class="sh-title-wrapper">
                    <h1>Privacy Policy</h1>
                    <p>For further details or concerns please contact us.</p>
                    <a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>" class="button border round cta">Contact Us</a>
                </div>
            </div> 
        </section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.PRIVACY.getDisplayName() %>" name="website_action"/></jsp:include>

        <%-- Main Content --%>
        <section class="section primary contact" id="s-contact">
            <div class="container">
                <p>Last updated: 14th November, 2015</p>
                <p>Zillious ("us", "we", or "our") operates http://www.mysite.com (change this) (the "Site"). This page informs you of our policies regarding the collection, use and disclosure of Personal Information we receive from users of the Site.</p>
                <p>We use your Personal Information only for providing and improving the Site. By using the Site, you agree to the collection and use of information in accordance with this policy.</p>

                <hr class="stripes" />
                <h2>Information Collection And Use</h2>
                <p>While using our Site, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you. Personally identifiable information may include, but is not limited to your name ("Personal Information").</p>

                <hr class="stripes" />
                <h2>Log Data</h2>
                <p>Like many site operators, we collect information that your browser sends whenever you visit our Site ("Log Data").</p>
                <p>This Log Data may include information such as your computer's Internet Protocol ("IP") address, browser type, browser version, the pages of our Site that you visit, the time and date of your visit, the time spent on those pages and other statistics.</p>
                <p>In addition, we may use third party services such as Analytics tools that collect, monitor and analyze this data.</p>

                <hr class="stripes" />
                <h2>Communications</h2>
                <p>We may use your Personal Information to contact you with newsletters, marketing or promotional materials and other information that may be revelant.</p>

                <hr class="stripes" />
                <h2>Cookies</h2>
                <p>Cookies are files with small amount of data, which may include an anonymous unique identifier. Cookies are sent to your browser from a web site and stored on your computer's hard drive.</p>
                <p>Like many sites, we use "cookies" to collect information. You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Site.</p>

                <hr class="stripes" />
                <h2>Security</h2>
                <p>The security of your Personal Information is important to us, but remember that no method of transmission over the Internet, or method of electronic storage, is 100% secure. While we strive to use commercially acceptable means to protect your Personal Information, we cannot guarantee its absolute security.</p>

                <hr class="stripes" />
                <h2>Changes To This Privacy Policy</h2>
                <p>This Privacy Policy is effective as of 14th November, 2015 and will remain in effect except with respect to any changes in its provisions in the future, which will be in effect immediately after being posted on this page.</p>
                <p>We reserve the right to update or change our Privacy Policy at any time and you should check this Privacy Policy periodically. Your continued use of the Service after we post any modifications to the Privacy Policy on this page will constitute your acknowledgment of the modifications and your consent to abide and be bound by the modified Privacy Policy.</p>
                <p>If we make any material changes to this Privacy Policy, we will notify you either through the email address you have provided us, or by placing a prominent notice on our website.</p>

                <hr class="stripes" />
                <h2>Contact Us</h2>
                <p>If you have any questions about this Privacy Policy, please <a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse) %>">contact us</a>.</p>
            </div>
        </section>

        <%-- Google Map --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/googlemap.jsp" />
        
        <%-- Footer --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/footer.jsp" />
    </div>
    <%-- end #site-content --%>


    <%-- TOOLS/UTILITIES --%>
    <jsp:directive.include file="/WEB-INF/pages/includes/page_tools.jsp" />
    
    <%-- SCRIPTS --%>
    <jsp:directive.include file="/WEB-INF/pages/includes/page_js.jsp" />
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
