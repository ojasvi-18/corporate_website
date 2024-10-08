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
    <jsp:param name="title" value="About Us"/>
    <jsp:param name="meta_description" value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually."/>
</jsp:include>

<body class="further about">
    <!-- SITE CONTENT -->

    <div id="site-content">

        <%-- Header --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/header_mainnav.jsp" />

        <!-- Sub-Header -->
        <section class="hero sub-header">
            <div class="container inactive">
                <div class="sh-title-wrapper">
                    <h1><z:message key="corp.about.title">About Zillious</z:message></h1>
                    <p><z:message key="corp.about.subtitle">We are a technology solution provider &amp; business process consulting company for the travel industry.</z:message></p>
                    <a href="#s-why-choose-us" target="blank" class="button border round cta"><z:message key="corp.common.readmore">Read More</z:message></a>
                </div>
            </div>
        </section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.ABOUT.getDisplayName() %>" name="website_action"/></jsp:include>

        <!-- Main Content -->
        <%-- Why Us --%>
        <section class="section primary why-choose-us section-map inactive" id="s-why-choose-us">
            <div class="container">
                <header class="sep active">
                    <div class="section-title">
                        <h2><z:message key="corp.aboutus.maintitle">Why <i>Choose Us</i></z:message></h2>
                        <h3><z:message key="corp.aboutus.mainsubtitle">Your Success is Our Success</z:message></h3>
                    </div>
                    <p><z:message key="corp.aboutus.text">At Zillious, we are always striving to achieve value for our customers. Our cost efficient approach coupled with early adoption of leading edge yet effective technologies help us deliver quick-to-market solutions that drive up the bottom-line of our customers.</z:message></p>
                </header>
                <div class="wcu-content">
                    <div class="wcu-graphic">
                        <img src="/static/images/desktop.png" alt="" />
                    </div>
                    <div class="wcu-features">
                        <div class="widget wcu-feature wcu-left">
                            <div class="widget-content">
                                <i class="livicon" data-n="wrench" data-op="1" data-c="#C1C1C1" data-s="55" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.aboutus.agilitytitle">Agility</z:message></h4>
                                    <h5><z:message key="corp.aboutus.agilitysubtitle">Workflow customization</z:message></h5>
                                </div>
                                <p><z:message key="corp.aboutus.agilitytext">We specialize in building interfaced &amp; plug-gable modules so that we can give you the flexibility to have customized workflow as per your business processes.</z:message></p>
                            </div>
                        </div>
                        <div class="widget wcu-feature wcu-left">
                            <div class="widget-content">
                                <i class="livicon" data-n="image" data-op="1" data-c="#C1C1C1" data-s="55" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.aboutus.experiencetitle">Experience &amp; Specialization</z:message></h4>
                                    <h5><z:message key="corp.aboutus.experiencesubtitle">Millions of tickets</z:message></h5>
                                </div>
                                <p><z:message key="corp.aboutus.experiencetext">We are only focused &amp; specialize in building Travel Technology solutions. This gives us ability to deliver quality products on-time, every-time!</z:message></p>
                            </div>
                        </div>
                        <div class="widget wcu-feature wcu-right">
                            <div class="widget-content">
                                <i class="livicon" data-n="piggybank" data-op="1" data-c="#C1C1C1" data-s="55" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.aboutus.saastitle">SaaS Model</z:message></h4>
                                    <h5><z:message key="corp.aboutus.saassubtitle">Low upfront costs</z:message></h5>
                                </div>
                                <p><z:message key="corp.aboutus.saastext">We keep our implementation costs down to minimal. Our business model relies on successful implementation with high ongoing adoption rates, making it a win-win!</z:message></p>
                            </div>
                        </div>
                        <div class="widget wcu-feature wcu-right">
                            <div class="widget-content">
                                <i class="livicon" data-n="gear" data-op="1" data-c="#C1C1C1" data-s="55" data-hc="false"></i>
                                <div class="title">
                                    <h4><z:message key="corp.aboutus.stabilitytitle">Stability</z:message></h4>
                                    <h5><z:message key="corp.aboutus.stabilitysubtitle">Long term relationships</z:message></h5>
                                </div>
                                <p><z:message key="corp.aboutus.stabilitytext">For the dynamic Travel industry, our product offering evolves with your organization over the years. As a result more than 90% of our customers continue to use the product since sign-up.</z:message></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>        

        <%-- Stats Bar --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/stats.jsp" />

        <%-- Team --%>
		<jsp:include flush="false" page="/WEB-INF/pages/sections/team.jsp" />
		
        <%-- Clients --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/clients.jsp" />

        <%-- Banner --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/banner.jsp" />

        <%-- More Info --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/more_info.jsp" />

        <%-- Suppliers --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/suppliers.jsp" />

        <%-- Google Map --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/googlemap.jsp" />
        
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
