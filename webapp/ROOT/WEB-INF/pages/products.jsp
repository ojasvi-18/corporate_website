<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>

<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
    <jsp:param name="title" value="Products"/>
    <jsp:param name="meta_description" value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually."/>
</jsp:include>

<body class="further portfolio portfolio-index">
    <!-- SITE CONTENT -->

    <div id="site-content">

        <%-- Header --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/header_mainnav.jsp" />

        <!-- Sub-Header -->
        <section class="hero sub-header">
		    <div class="container inactive">
		        <div class="sh-title-wrapper">
		            <h1><z:message key="corp.products.maintitle">Our Products</z:message></h1>
		            <p><z:message key="corp.products.maintext">Our collaborative style of working encourages active involvement of client at every stage of the product life cycle through its inception, design and development to implementation.</z:message></p>
		            <a href="#s-welcome" target="blank" class="button border round cta"><z:message key="corp.common.readmore">Read More</z:message></a>
		        </div>
		    </div>
		</section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.PRODUCTS.getDisplayName() %>" name="website_action"/></jsp:include>

        <!-- Main Content -->

        <%-- Portfolio --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/channels.jsp">
            <jsp:param name="psize" value="large"/>
            <jsp:param name="active" value="active"/>
        </jsp:include>

        <%-- Quote --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/quote.jsp" />
        
        <%-- Portfolio --%>
        <%--
        <jsp:include flush="false" page="/WEB-INF/pages/sections/portfolio.jsp" />
        --%>

        <%-- Clients --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/clients.jsp" />

        <%-- Banner --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/banner.jsp" />

        <%-- Footer --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/footer.jsp" />
    </div>
    <!-- end #site-content -->


    <%-- TOOLS/UTILITIES --%>
    <jsp:directive.include file="/WEB-INF/pages/includes/page_tools.jsp" />
    
    <%-- SCRIPTS --%>
    <jsp:directive.include file="/WEB-INF/pages/includes/page_js.jsp" />

    <%-- Page Specific Scripts --%>
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
