<%@page import="com.zillious.corporate_website.ui.beans.AudienceBean"%>
<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%


%>
<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
    <jsp:param name="title" value="Home Page"/>
    <jsp:param name="meta_description" value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually."/>
</jsp:include>

<body class="index parallax-title"><%-- video-bg --%>
    <%-- SITE CONTENT --%>
    <div id="site-content">

        <%-- Header --%>
        <jsp:include flush="false" page="/WEB-INF/pages/includes/header_mainnav.jsp" />
        
        <%-- Video BG --%>
        <%--
		<video autoplay loop poster="/static/images/stock5.jpg" class="hero-vid">
			<source src="/static/videos/878986.mp4" type="video/mp4">
		</video>
		<div class="hero-overlay"></div>
		--%>

        <%-- Hero --%>
        <section class="hero inactive">
            <div class="hero-down">
                <a href="#s-welcome" class="mouse">
                    <div class="mouse-animations">
                        <div class="mouse-scroll-l"></div>
                        <div class="mouse-scroll-2"></div>
                        <div class="mouse-scroll-3"></div>
                    </div>
                </a>
            </div>
            <div class="container">
                <div class="title-wrapper">
                    <div class="hero-title">
                        <h2><z:message key="corp.home.welcome">Welcome to Zillious</z:message></h2>
                        <h3><z:message key="corp.home.welcomesubhead">ASIA'S LEADING TRAVEL TECHNOLOGY COMPANY</z:message></h3>
                    </div>
                    <div class="meta">
                        <p class="blurb"><z:message key="corp.home.about.travolution">Empower your company with the Travolution product. Travolution is truly cutting edge in terms of content, agility and performance; leave your customers with an experience to remember!</z:message></p>
                        <a href="/static/downloads/Zillious-Travolution_Brochure.pdf" target="_blank" class="button round brand-1"><i class="fa fa-file-pdf-o"></i> <z:message key="corp.common.downloadbrochure">Download Brochure</z:message></a>
                        <a href="#s-welcome" class="button round border"><i class="fa fa-book"></i> <z:message key="corp.common.readmore">Read More</z:message></a>
                    </div>
                </div>
            </div>
        </section>
        
        <%-- Main Content --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/channels.jsp" />
        
        <%-- Clients --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/clients.jsp" />

        <%-- Services --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/services.jsp" />

        <%-- Suppliers --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/suppliers.jsp" />

        <%-- Quote --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/quote.jsp" />
        
        <%-- Testimonials --%>
        <jsp:include flush="false" page="/WEB-INF/pages/sections/testimonials.jsp" />

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
    
    
    <%-- if popup content available, add the code to display the popup --%>
    <%
    	String content = AudienceBean.getPopupContent();
    	if(content != null) {
	%>
<script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
<link href="/static/css/jquery-ui-custom.min.css" rel="stylesheet" />
		<div id="modal">
			<%= content %>
			<%--
			Content example:
			<div style="background-color: white; display: block;">
<div style="padding: 4%;">
   <center>
      Hi<br>
      You are welcome to meet us at <b>TT 1264</b><br>
      <div>
         <img src="http://www.arabiantravelmarket.com/RXUK/RXUK_ArabianTravelMarket/responsive/images/Logo/ATM_380x180_logo2016-creative.png"><br>
         <div><img src="/static/images/logo.png" alt="Zillious Solutions Pvt Ltd"></div>
      </div>
      <br>
      <div style="color:red;"><img align="bottom" width="2%" src="/static/images/india_travel_awards.jpg" style=""><div style="font-weight: bold; padding-left: 1%; display: inline-block;"><div>Best Travel Technology Provider</div><div>
India Travel Awards 2014 and 2015</div></div></div>
   </center>
</div>
</div>
			 --%>
		</div>
		<Style type="text/css">
			.overlay-shadow {
				background-color: black;
				background-image: none;
				opacity: 0.9;
			}
		</Style>
		<script type="text/javascript">
		$(document).ready(function() {
			$( "#modal" ).dialog({
			  autoOpen: true,
			  closeOnEscape: true,
			  modal: true,
			  title: "Welcome to Zillious",
			  draggable: true,
			  resizable: false,
			  closeText: "",
			  open: function() {
			    $('.ui-widget-overlay').addClass('overlay-shadow');
			  }, 
			  close: function() {
			    $('.ui-widget-overlay').removeClass('overlay-shadow');
			  }
			});
		});
			
		</script>	    
  	<%
    	}
    %>
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
