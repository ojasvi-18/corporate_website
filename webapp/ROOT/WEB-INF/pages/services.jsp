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
    <jsp:param name="title" value="Services"/>
    <jsp:param name="meta_description" value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually."/>
</jsp:include>

<body class="further services">
    <!-- SITE CONTENT -->

    <div id="site-content">

        <%-- Header --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/header_mainnav.jsp" />

        <!-- Sub-Header -->
        <section class="hero sub-header">
            <div class="container inactive">
                <div class="sh-title-wrapper">
                    <h1><z:message key="corp.services.maintitle">Our Services</z:message></h1>
                    <p><z:message key="corp.services.maintext">We provide innovative &amp; cost effective technology services and software solutions to our clients.</z:message></p>
                    <a href="#services" target="blank" class="button border round cta"><z:message key="corp.common.readmore">Read More</z:message></a>
                </div>
            </div>
        </section>

       <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.SERVICES.getDisplayName() %>" name="website_action"/></jsp:include>

        <!-- Main Content -->
        <section class="section services primary section-map" id="services">
		    <div class="container">
		        <header class="sep active">
		            <div class="section-title">
		                <h2><i class="fa fa-cogs"></i> <z:message key="corp.services.specializationtitle">Our Travel Technology <i>Specializations</i></z:message></h2>
		            </div>
		            <p class="sub-text"><z:message key="corp.services.specializationtext">Zillious provides innovative and cost effective technology services and software solutions to its clients, which help them to streamline their business processes and achieve competitive advantage.</z:message></p>
		        </header>
		        <div class="row inactive">
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="doc-portrait" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.consultancytitle">Consultancy</z:message></h4>
		                            <h5>For your complex Travel IT Project</h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.consultancytext">With over 450 man years of Travel Technology experience, we can analyze your requirements &amp; suggest the best solution for your next big travel technology project.</z:message></p>
		                </div>
		            </div>
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="responsive" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.mobileapptitle">Mobile App Development</z:message></h4>
		                            <h5><z:message key="corp.services.mobileappsubtitle">Advanced Apps for the next gen</z:message></h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.mobileapptext">Advanced Mobile App &amp; Website development for highly integrated &amp; responsive user experience. Not just itinerary &amp; alerts, but transactions with approval &amp; policy enforcing.</z:message></p>
		                </div>
		            </div>
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="money" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.automatedfaretitle">Automated Fare Loading</z:message></h4>
		                            <h5><z:message key="corp.services.automatedfaresubtitle">Over million fares every month</z:message></h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.automatedfaretext">Move on to the next level of netfare loading with automated processes. No need for an army of data entry consultants now! Be the first to sell the latest offers in your market!</z:message></p>
		                </div>
		            </div>
		        </div>
		        <div class="row inactive">
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="image" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.airlinerevenuetitle">Airline Revenue</z:message></h4>
		                            <h5><z:message key="corp.services.airlinerevenuesubtitle">Up-selling &amp; Cross selling</z:message></h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.airlinerevenuetext">Stay ahead of the competition by selling complimentary &amp; non-compete products. Optimize your inventory with customer focused up-selling through advanced trend analysis &amp; big data.</z:message></p>
		                </div>
		            </div>
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="connect" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.extranetinventorytitle">Extranet Inventory Solution</z:message></h4>
		                            <h5><z:message key="corp.services.extranetinventorysubtitle">Manage Air, Hotel &amp; Car</z:message></h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.extranetinventorytext">Load your Air, Hotel and Car inventories into our extranet. Advanced distribution system with rate plans, inventory allocation, blackout dates, private fares, Inventory &amp; Supplier Dashboards.</z:message></p>
		                </div>
		            </div>
		            <div class="span-4 widget service">
		                <div class="widget-content">
		                    <header>
		                        <i class="livicon" data-n="servers" data-op="1" data-c="#C1C1C1" data-s="48" data-hc="false"></i>
		                        <div class="title">
		                            <h4><z:message key="corp.services.micetitle">DMC &amp; MICE Service</z:message></h4>
		                            <h5><z:message key="corp.services.micesubtitle">Automate Business Processes</z:message></h5>
		                        </div>
		                    </header>
		                    <p><z:message key="corp.services.micetext">Create MICE event sub sites with instant confirmation &amp; payment collection. Online registration or Invite-only group management. Mix of contracted and dynamic fares. DMC inventory consolidation system.</z:message></p>
		                </div>
		            </div>
		        </div>
		    </div>
		</section>

        <%-- Clients --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/clients.jsp" />

        <%-- Quote --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/quote.jsp" />

        <%-- Testimonials --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/testimonials.jsp" />

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
