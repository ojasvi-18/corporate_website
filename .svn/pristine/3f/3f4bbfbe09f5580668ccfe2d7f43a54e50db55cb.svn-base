<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page
	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean
					.getRequest(request);
			ZilliousSecurityWrapperResponse secureResponse = SecurityBean
					.getResponse(response);
%>
<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
	<jsp:param name="title" value="Login for advanced control" />
	<jsp:param name="meta_description"
		value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually." />
</jsp:include>

<body class="further contact">
	<%-- SITE CONTENT --%>
	<div id="site-content">

		<%-- Header --%>
		<jsp:include flush="false"
			page="/WEB-INF/pages/includes/header_mainnav.jsp" />

		<%-- Sub-Header --%>
		<section class="hero sub-header">
			<div class="container inactive">
				<div class="sh-title-wrapper">
					<h1>Login Page</h1>
					<p>Please login using your user id/email and password</p>
				</div>
			</div>
		</section>

		<%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param
				value="<%=WebsiteActions.LOGIN.getDisplayName()%>"
				name="website_action" /></jsp:include>

		<%-- Main Content --%>
		<section class="section primary contact" id="s-contact">
			<jsp:include flush="false"
				page="/WEB-INF/pages/includes/messages.jsp" />
			<div class="container">
				<div>
					<div id="messageDiv">Please fill the form below(in only
						English Characters) to Log-in into the website</div>
					<form class="h5-valid" id="loginForm" method="post">
						<div class="form-element">
							<input type="email" name="email" class="box" required
								maxlength="50"> <label>Email-ID</label>
						</div>
						<div class="form-element">
							<input type="password" name="password" class="box" maxlength="20">
							<label>Password</label>
						</div>
						<button type="submit" class="button large full-width brand-1">Login</button>
					</form>
				</div>
			</div>
		</section>

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
	    	var loginObj = new LoginJS($("#loginForm"), "<%=WebsiteActions.LOGIN.getActionURL(secureRequest,secureResponse, null, true)%>");
    });
  </script>
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
