<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page
	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
    ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Browser not supported - Zillious</title>
<meta name="description"
	content="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually.">
<meta name="viewport" content="width=device-width, initial-scale=0.85">
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<!-- Icons & favicons -->
<link rel="icon" href="/static/images/favicon.png">
<!--[if IE]>
        <link rel="shortcut icon" href="/static/images/favicon.ico">
    <![endif]-->
    <!-- Stylesheet -->
    <link rel="stylesheet" href="/static/css/browser-min.css">
</head>
<body>
<div id="wrapper">
	<div id="header"> 
				<!-- Logo -->
		<div class="logo" id="logo">
			<!-- image logo -->
			<a href="<%=WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true)%>" class="image-logo"> <img src="/static/images/logo.png" alt="Zillious Solutions" />
			</a>
		</div>
	</div>
	<div id="content">
		<!-- SITE CONTENT -->
		<center>
				<div id="site-content" class="browser">
			<div><z:message key="corp.browser.text1"><div>Sorry! But your Browser doesn't seem to be on the same page with us. But don't lose heart. You can check out these awesome browsers instead:</div></z:message></div>
			<br />
			<br />
			<div class="icon">
				<a href="http://www.opera.com/" target="_blank" title="<z:message key="corp.browser.operabrowser">Download Opera Browser</z:message>"><img src="/static/images/icons/opera.png"/></a>
			</div>
			<div class="icon">
				<a href="https://www.google.com/chrome/"  target="_blank" title="<z:message key="corp.browser.chromebrowser">Download Chrome Browser</z:message>"><img src="/static/images/icons/chrome.png"/></a>
			</div>
			<div class="icon">
				<a href="https://www.mozilla.org/firefox/new/" target="_blank" title="<z:message key="corp.browser.firefoxbrowser">Download Firefox Browser</z:message>"><img src="/static/images/icons/firefox.png"/></a>
			</div class="icon">
			<div class="icon">
				<a href="https://support.apple.com/downloads/safari"  target="_blank" title="<z:message key="corp.browser.safaribrowser">Download Safari Browser</z:message>"><img src="/static/images/icons/safari.png"/></a>
			</div>
		</div>
		</center>

	
	 </div>
	<div id="footer">
			<div class="bottom-bar">
				<div class="container">
					<div class="footer-wrapper">
						<!-- Copyright Section -->
						<div class="copyright" id="copyright">
							&copy; 2015,
							<z:message key="corp.common.zillious">Zillious Solutions Pvt Ltd (India)</z:message>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
<%--<div class="app-header" id="app-header">
		<div class="container">
			<div class="header-wrapper">
				<!-- Logo -->
				<div class="logo" id="logo">
					<!-- image logo -->
					<a
						href="<%=WebsiteActions.HOME.getActionURL(secureRequest, secureResponse, null, true)%>"
						class="image-logo"> <img src="/static/images/logo.png"
						alt="Zillious Solutions" />
					</a>
				</div>
			</div>
		</div>
	</div>
	<!-- SITE CONTENT -->
	<div id="site-content" class="browser">
		<div><z:message key="corp.browser.text1">Sorry! But your Browser doesn't seem to be on the same page with us.</z:message></div>
		<br />
		<div><z:message key="corp.browser.text2">But don't lose heart. You can check out these awesome browsers instead:</z:message></div>
		<br />
		<br />
		<div>
			<a href="http://www.opera.com/" target="_blank" title="<z:message key="corp.browser.operabrowser">Download Opera Browser</z:message>"><img src="/static/images/icons/opera.png"/></a>
		</div>
		<div>
			<a href="https://www.google.com/chrome/"  target="_blank" title="<z:message key="corp.browser.chromebrowser">Download Chrome Browser</z:message>"><img src="/static/images/icons/chrome.png"/></a>
		</div>
		<div>
			<a href="https://www.mozilla.org/firefox/new/" target="_blank" title="<z:message key="corp.browser.firefoxbrowser">Download Firefox Browser</z:message>"><img src="/static/images/icons/firefox.png"/></a>
		</div>
		<div>
			<a href="https://support.apple.com/downloads/safari"  target="_blank" title="<z:message key="corp.browser.safaribrowser">Download Safari Browser</z:message>"><img src="/static/images/icons/safari.png"/></a>
		</div>
	</div>
	<div class="app-footer">
	    <div class="bottom-bar">
	        <div class="container">
	            <div class="footer-wrapper">
	                <!-- Copyright Section -->
	                <div class="copyright" id="copyright">
	                    &copy; 2015, <z:message key="corp.common.zillious">Zillious Solutions Pvt Ltd (India)</z:message>
	                </div>
	            </div>
	        </div>
    </div>
	</div> --%>

	
</body>
</html>
