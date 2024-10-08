<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
    ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>

<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
	<jsp:param name="title" value="Careers" />
	<jsp:param name="meta_description"
		value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually." />
</jsp:include>

<body class="further careers">
	<%-- SITE CONTENT --%>
	<div id="site-content">

		<%-- Header --%>
		<jsp:include flush="false"
			page="/WEB-INF/pages/includes/header_mainnav.jsp" />

		<%-- Sub-Header --%>
		<section class="hero sub-header">
			<div class="container inactive">
				<div class="sh-title-wrapper">
					<h1><z:message key="corp.career.headingtitle">Career @ Zillious</z:message></h1>
					<p><z:message key="corp.career.headingsubtitle">Thank you for showing interest in <b>working with us</b></z:message>.</p>
					<a href="https://zilliouscareers.recruiterbox.com/" target="_blank"
						class="button border round cta"><z:message key="corp.common.applynow">Apply Now</z:message></a>
				</div>
			</div>
		</section>

		<%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.CAREERS.getDisplayName() %>" name="website_action"/></jsp:include>

		<%-- Main Content --%>
		<section class="section primary why-work-with-us" id="s-why-work-with-us">
			<div class="container">
				<header class="sep active">
					<div class="section-title"
						style="margin-left: auto; margin-right: auto; text-align: center;">
						<h3><z:message key="corp.career.maintitle">You would love <b>Zillious</b></z:message>.
						</h3>
						<p><z:message key="corp.career.mainsubtitle">And We would love to have you on our team.</z:message></p>
					</div>
				</header>
			</div>

			<div class="container">
				<div class="career-titles">
					<header class="career-content career-features">
						<div class="section-title">
							<h2><z:message key="corp.career.whyzillioustitle">Why Zillious you ask?</z:message></h2>
						</div>
						<p><z:message key="corp.career.whyzillioustext">Because we are a team of young, highly motivated individuals. We would give you opportunities to outperform yourself, and be better than you are today. However, if this is not a good enough reason for you to consider applying here, check out the fun stuff that we do here in Zillious below</z:message></p>
					</header>
					<div id="whyCareerZillious" class="career-features-carousel owl-carousel">
						<div class="widget career-feature">
							<i class="livicon" data-n="users" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.environmenttitle">Friendly environment</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.environmenttext">Here, At Zillious, Everybody is everybody's friend. We believe in m-to-m relationships</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="signal" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.culturetitle">Startup Culture</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.culturetext">Even after so many years, we love our Startup Culture so much. We do not bind ourselves into a specific technology, or aspect of the application. Rather, we believe that everybody can and should do everything that the company has to offer</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="home" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.policytitle">Open Door Policy</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.policytext">In case you are faced with any problem, we really encourage people to find anyone, suitable to help get it resolved</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="brightness-up" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.exposuretitle">Good Exposure</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.exposuretext">We believe that actual learning is on the job. So, employees are encouraged to get in touch with the clients, approach them, participate in the meetings etc</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="bank" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.empowermenttitle">Empowerment and Accountability</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.empowermenttext">Everyone here is responsible for their own code. We own our mistakes and learn from them</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="calendar" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.timingstitle">Flexible Timings</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.timingstext">We give importance to work, and not in the number of hours that the employee has spent at the office. Quality over Quantity, any day</z:message></p>
						</div>
						<div class="widget career-feature">
							<i class="livicon" data-n="trophy" data-c="#C1C1C1" data-op="1" data-s="68" data-hc="false"></i>
							<div class="widget-content">
								<div class="title">
									<h4><z:message key="corp.career.funtitle">Zillious is Fun!</z:message></h4>
								</div>
							</div>
							<p><z:message key="corp.career.funtext">All work and no play can make anyone a dull person. We are a bunch of young, energetic and "fun" people. We celebrate all festivals with equal zeal and enthusiasm. We organize recreational outings time and again. This gives us to bond as a team well, as well as, give a break from our Laptops</z:message></p>
						</div>
					</div>
				</div>
			</div>
		</section>
		<%-- Team --%>
		<jsp:include flush="false" page="/WEB-INF/pages/sections/team.jsp" />
		<%-- Why Us --%>
		<section class="section primary careers" id="s-contact">
			<div class="container">
				<div class="row contact-widgets">
					<div class="span-4 widget">
						<div class="widget-content">
							<header>
								<i class="livicon" data-n="globe" data-op="1" data-c="#C1C1C1"
									data-s="48" data-hc="false"></i>
								<div class="title">
									<h4><z:message key="corp.common.ouraddresstext">Our Address</z:message></h4>
									<h5><z:message key="corp.common.newdelhiheadofficetext">New Delhi - Head Office</z:message></h5>
								</div>
							</header>
						</div>
					</div>
					<div class="span-4 widget">
						<div class="widget-content">
							<header>
								<i class="livicon" data-n="phone" data-op="1" data-c="#C1C1C1"
									data-s="48" data-hc="false"></i>
								<div class="title">
									<h4><z:message key="corp.common.getintouchtext">Get in Touch</z:message></h4>
									<h5><z:message key="corp.common.happytoheartext">Happy to hear from you!</z:message></h5>
								</div>
							</header>
							<ul>
								<li><z:message key="corp.footer.locationlabel">Location</z:message>: <z:message key="corp.footer.locationnewdelhiindiatimezone">New Delhi, India (GMT +5:30)</z:message></li>
								<li><z:message key="corp.footer.phonelabel">Phone</z:message>: <z:message key="corp.footer.phonenumber">+91 (011) 6900 0863</z:message></li>
								<li><z:message key="corp.nav.careers">Career</z:message>: <a href="mailto:recruit@zillious.com">recruit@zillious.com</a></li>
							</ul>
						</div>
					</div>
				</div>
				<hr class="stripes" />
			</div>
		</section>

		<%-- Google Map --%>
		<jsp:include flush="false"
			page="/WEB-INF/pages/sections/googlemap.jsp" />

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
