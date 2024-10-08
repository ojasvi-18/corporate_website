<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@page import="com.zillious.corporate_website.ui.resources.QueryTypes"%>
<%@page import="java.util.Map"%>
<%@page import="com.zillious.corporate_website.ui.beans.I18NBean"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
	Map<String, String> contactUsQueryTypesMap = QueryTypes.getContactUsPageQueryTypes(secureRequest);
%>
<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
    <jsp:param name="title" value="Contact"/>
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
                    <h1><z:message key="corp.contact.headingtitle">Contact Us</z:message></h1>
                    <p><z:message key="corp.contact.headingtext">We will be glad to hear from you! If you are an existing customer, please contact your SPOC or raise a support ticket.</z:message></p>
                    <a href="https://zillious.atlassian.net" target="blank" class="button border round cta"><z:message key="corp.common.supportickettext">Support Ticket</z:message></a>
                </div>
            </div> 
        </section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.CONTACT.getDisplayName() %>" name="website_action"/></jsp:include>

        <%-- Main Content --%>
        <section class="section primary contact" id="s-contact">
            <jsp:include flush="false" page="/WEB-INF/pages/includes/messages.jsp" />
            <div class="container">
                <div class="row contact-widgets">
                    <div class="span-4 widget">
                        <div class="widget-content">
                            <header>
                                <i class="livicon" data-n="globe" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.common.ouraddresstext">Our Address</z:message></h4>
									<h5><z:message key="corp.common.newdelhiheadofficetext">New Delhi - Head Office</z:message></h5>
                                </div>
                            </header>
                            <p>
                                JP House, 172 Khasra #300, Lane 2, FF<br />
                                West End Marg, Saidullajab,<br/> 
                                New Delhi - 110030,<br />
                                INDIA<br />
                            </p>
                        </div>
                    </div>
                    <div class="span-4 widget">
                        <div class="widget-content">
                            <header>
                                <i class="livicon" data-n="phone" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.common.getintouchtext">Get in Touch</z:message></h4>
									<h5><z:message key="corp.common.happytoheartext">Happy to hear from you!</z:message></h5>
                                </div>
                            </header>
                            <ul>
                              	<li><z:message key="corp.footer.locationlabel">Location</z:message>: <z:message key="corp.footer.locationnewdelhiindiatimezone">New Delhi, India (GMT +5:30)</z:message></li>
								<li><z:message key="corp.footer.phonelabel">Phone</z:message>: <z:message key="corp.footer.phonenumber">+91 (011) 6900 0863</z:message></li>
                                <li><z:message key="corp.common.salestext">Sales</z:message>: <a href="mailto:sales@zillious.com">sales@zillious.com</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="span-4 widget">
                        <div class="widget-content">
                            <header>
                                <i class="livicon" data-n="clock" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.common.openinghourstext">Opening Hours</z:message></h4>
                                    <h5><i><z:message key="corp.contact.emergencycontacttext">For emergencies, call your assigned SPOC any-time!</z:message></i></h5>
                                </div>
                            </header>
                            <ul>
                                <li><z:message key="corp.common.monday">Monday</z:message> - <z:message key="corp.common.friday">Friday</z:message>: 10:00 - 19:00</li>
                                <li><z:message key="corp.common.saturday">Saturday</z:message>: 11:00 - 17:00</li>
                                <li><z:message key="corp.common.sundayandholiday">Sunday & Holidays</z:message>: <z:message key="corp.common.closedtext">Closed</z:message></li>
                                <li><z:message key="corp.common.emergencytext">Emergency</z:message>: <z:message key="corp.contact.emergencycontacttext2">Contact assigned SPOC</z:message></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <hr class="stripes" />
					<div>
						<div id="errorDiv"></div>
						<div id="messageDiv"><z:message key="corp.contact.formmessage">Please fill the form below(in only English Characters) to contact us</z:message></div>                        
						<form class="h5-valid" id="contactUsForm" method="post">
	                    <fieldset>
	                        <div class="form-element">
	                            <select name="query" class="box" required>
	                            <option value="-"></option>
	                            <% for(String type : contactUsQueryTypesMap.keySet()) { %>
	                            <option value="<%=type %>"><%=contactUsQueryTypesMap.get(type)%></option>
	                            <%} %>
	                            </select>
	                            <label for="query"><z:message key="corp.contact.querylabel">Type of Query</z:message></label>
	                        </div>
	                        <div class="form-element">
	                            <input type="text" name="name" class="box" required maxlength="50">
	                            <label><i class="fa fa-user"></i>&nbsp; <z:message key="corp.contact.namelabel">Name</z:message></label>
	                        </div>
	                        <div class="form-element">
	                            <input type="tel" name="phone" class="box" maxlength="20">
	                            <label><i class="fa fa-phone"></i>&nbsp; <z:message key="corp.contact.phonelabel">Phone Number</z:message></label>
	                        </div>
	                        <div class="form-element">
	                            <input type="email" name="email" class="box" required maxlength="50">
	                            <label><i class="fa fa-envelope"></i>&nbsp; <z:message key="corp.footer.newsletter.emailaddress">Email Address</z:message></label>
	                        </div>
	                    </fieldset>
	                    <div class="form-element">
	                        <textarea class="box" name="message" placeholder="<z:message key="corp.contact.entermessagetext">Enter your message</z:message>" required></textarea>
	                    </div>
	                    <div class="form-element">
		                    <center>
			                    <div class="g-recaptcha" data-sitekey="6Lcl5BETAAAAAITxUya6hdkOHBl-DEZd7nUkqiB8"></div>
		                    </center>
	                    </div>
	                    <button type="submit" class="button large full-width brand-1"><z:message key="corp.contact.submitcontacttickettext">Submit Contact Request</z:message></button>
	                </form>
                </div>
            </div>
        </section>

        <%-- Suppliers --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/suppliers.jsp" />

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

    <%-- Page Specific Scripts --%>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	var contact = new contactJS($("#contactUsForm"), "<%=WebsiteActions.CONTACT.getActionURL(secureRequest,secureResponse, new String[] {"addContactRequest"}, true)%>");
	    });
    </script>
	<script src='https://www.google.com/recaptcha/api.js?hl=<%=I18NBean.getSelectedLanguage(secureRequest)%>'></script>

</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
