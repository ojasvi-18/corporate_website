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
    <jsp:param name="title" value="Setup Popup Contents"/>
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
                    <h1>Setup Popup Content</h1>
                    <p>Please specify the content of the popup dialog box that would appear on the index page</p>
                </div>
            </div> 
        </section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.SETUP_INDEX_PAGE_POPUP.getDisplayName() %>" name="website_action"/></jsp:include>

        <%-- Main Content --%>
        <section class="section primary contact" id="s-contact">
        <jsp:include flush="false" page="/WEB-INF/pages/includes/messages.jsp" />
            <div class="container">
					<div>
						<div id="messageDiv">Please fill the form below(in only English Characters) to setup the content of the dialog box</div>                        
						<form class="h5-valid" id="popupContentForm" method="post">
						<fieldset>
							<div class="form-element">
                     		    <input type="text" name="start_date" class="box" required maxlength="50">
	                            <label>Start Date(DDMMMYY)</label>
	                        </div>
	                        <div class="form-element">
	                            <input type="text" name="end_date" class="box" maxlength="20">
	                            <label>End Date(DDMMMYY)</label>
	                        </div>
						</fieldset>
	                    <div class="form-element">
	                        <textarea class="box" name="content" placeholder="Enter HTML Content" required></textarea>
	                    </div>
	                    <button type="submit" class="button large full-width brand-1">Submit Content</button>
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
	    	var popup = new PopupJS($("#popupContentForm"), "<%=WebsiteActions.SETUP_INDEX_PAGE_POPUP.getActionURL(secureRequest,secureResponse, new String[] {"addContentRequest"}, true)%>");
	    });
    </script>
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
