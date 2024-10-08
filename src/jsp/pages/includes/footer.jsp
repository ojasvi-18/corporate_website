<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.beans.WebsiteBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@ taglib prefix="z" uri="/WEB-INF/tld/messages.tld"%>
<%
	ZilliousSecurityWrapperRequest secureRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse secureResponse = SecurityBean.getResponse(response);
%>
<footer class="app-footer">
    <div class="container footer-content">
        <div class="row">
            <div class="span-3 footer-col address">
                <!-- Logo -->
                <div class="logo">
                    <!-- image logo -->
                    <a href="<%= WebsiteActions.HOME.getActionURL(secureRequest, secureResponse) %>" class="image-logo">
                        <img src="/static/images/logo.png" alt="Zillious Solutions" />
                    </a>
                </div>                       
                <p><z:message key="corp.footer.travolution">Empower your travel company with the Travolution product. Travolution is truly cutting edge in terms of content, agility and performance.</z:message></p>
                <ul>
                    <li><b><i class="fa fa-globe"></i> <z:message key="corp.footer.locationlabel">Location</z:message>:</b> <z:message key="corp.footer.locationnewdelhiindia">New Delhi, India</z:message></li>
                    <li><b><i class="fa fa-phone-square"></i> <z:message key="corp.footer.phonelabel">Phone</z:message>:</b> <z:message key="corp.footer.phonenumber">+91 (011) 6900 0863</z:message></li>
                    <li><b><i class="fa fa-envelope"></i> <z:message key="corp.footer.emaillabel">Email</z:message>:</b> <a href="mailto:sales@zillious.com">sales@zillious.com</a></li>
                </ul>
            </div>
            <div class="span-3 footer-col social">
                <h3><z:message key="corp.footer.newslettertitle">Newsletter</z:message></h3>
                <h4><z:message key="corp.footer.newslettersubtitle">Sign up to our mailing list</z:message></h4>
                <p><z:message key="corp.footer.newslettertext">Keep in touch with developments at Zillious.</z:message></p>
                <div>
                    <form class="newsletter h5-valid" action="" id="newsletter" method="post">
                    	<input type="hidden" name="cs" value="sub" />
				        <div id="message"></div>                        
                        <div class="form-element">
                            <input type="email" class="box" required="required" name="email" placeholder="<z:message key="corp.footer.newsletter.emailaddressplaceholder">email address(English only)</z:message>"/>
                            <label><z:message key="corp.footer.newsletter.emailaddress">Email Address</z:message></label>
                            <button type="button" name="addSubscription">
                                <i class="fa fa-check"></i>
                            </button>
                        </div>
                    </form>
                </div>
                <div class="social-icons">
                    <%--
                    <a href="#" class="icon tooltip" data-tip="Dribbble">
                        <i class="fa fa-dribbble"></i>
                    </a>
                    <a href="#" class="icon tooltip" data-tip="Twitter">
                        <i class="fa fa-twitter"></i>
                    </a>
                    <a href="#" class="icon tooltip" data-tip="DeviantArt">
                        <i class="fa fa-deviantart"></i>
                    </a>
                    <a href="#" class="icon tooltip" data-tip="Behance">
                        <i class="fa fa-behance"></i>
                    </a>
                    --%>
                    <a href="https://www.facebook.com/zillioussolutions" target="_blank" class="icon tooltip" data-tip="Facebook">
                        <i class="fa fa-facebook"></i>
                    </a>
                    <a href="https://www.linkedin.com/company/zillious" target="_blank" class="icon tooltip" data-tip="LinkedIn">
                        <i class="fa fa-linkedin"></i>
                    </a>
                </div>
            </div>
            <div class="span-6 footer-col testimonial">
                <h3><z:message key="corp.footer.testimonials.testimonialtitle">Testimonials</z:message></h3>
                <h4><z:message key="corp.footer.testimonials.testimonialsubtitle">See what our clients say</z:message></h4>
                <div class="owl-carousel footer-testimonials">
                    <div class="testimonial">
                        <blockquote>
                                <p><z:message key="corp.footer.testimonials.fcm">Today our clients, across 18 branches in India, benefit from the superior response times, more seamless & real-time reporting through the Travolution product!</z:message></p>
                        </blockquote>
                        <div class="cf">
                            <span class="profile">
                                <a href="/static/images/testimonials/gl_fcm.jpg" class="modal-image thumb"><img src="/static/images/testimonials/gl_fcm_thumb.jpg" alt="" /></a>
                            </span>
                            <cite>
                                <strong>Gaurav Luthra</strong>
                                <i><z:message key="corp.common.vpproducts">VP Products</z:message>, FCm Travel Solutions</i>
                            </cite>
                        </div>
                    </div>
                    <div class="testimonial">
                        <blockquote>
                            <p><z:message key="corp.footer.testimonials.traveltours">From the outset, their professionalism, willingness to go the extra mile, observance of deadlines and technical expertise assured us that we are in safe hands...</z:message></p>
                        </blockquote>
                        <div class="cf">
                            <span class="profile">
                                <a href="/static/images/testimonials/ak_tt.jpg" class="modal-image thumb"><img src="/static/images/testimonials/ak_tt_thumb.jpg" alt="" /></a>
                            </span>
                            <cite>
                                <strong>Ashwin Narayanan</strong>
                                <i><z:message key="corp.common.ceo">CEO</z:message>, Travel Tours Group</i>
                            </cite>
                        </div>
                    </div>
		            <div class="testimonial">
		                <blockquote>
		                    <p><z:message key="corp.testimonials.pearl">With <b>deep rooted understanding</b> of travel technology, Zillious is <b>quick to adapt</b> to the changing landscapes to keep safe distance from their competition...</z:message></p>
		                </blockquote>
                        <div class="cf">
                            <span class="profile">
                                <a href="/static/images/testimonials/as_tt.jpg" class="modal-image thumb"><img src="/static/images/testimonials/as_tt_thumb.jpg" alt="" /></a>
                            </span>
                            <cite>
                                <strong>Arjun Seth</strong>
                                <i><z:message key="corp.common.managingdirector">MD</z:message>, Pearl Group Of Companies</i>
                            </cite>
                        </div>
		            </div>
                </div>
            </div>
        </div>
    </div>
    <div class="bottom-bar">
        <div class="container">
            <div class="footer-wrapper">
                <!-- Copyright Section -->
                <div class="copyright" id="copyright">
                    &copy; 2017, <z:message key="corp.common.zillious">Zillious Solutions Pvt Ltd (India)</z:message>
                </div>
                <!-- Footer Navigation -->
                <nav class="footer-nav">                
                    <ul>
                        <li><a href="<%= WebsiteActions.ABOUT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.about">About</z:message></a></li>
                        <%--<li><a href="<%= WebsiteActions.PRIVACY.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.privacy">Privacy</z:message></a></li> --%>
                        <li><a href="<%= WebsiteActions.TERMS.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.terms">Terms</z:message></a></li>
                        <li><a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.contact">Contact</z:message></a></li>
<%--                         <%if(WebsiteBean.isUIActionAllowed(secureRequest, WebsiteActions.LOGIN)){ %>      
	                        <li><a href="<%= WebsiteActions.LOGIN.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.login">Login</z:message></a></li>
						<%}%> 
						
                        <%if(WebsiteBean.isUIActionAllowed(secureRequest, WebsiteActions.LOGOUT)){ %>      
	                        <li><a href="<%= WebsiteActions.LOGOUT.getActionURL(secureRequest, secureResponse, null, true) %>"><z:message key="corp.nav.logout">Logout</z:message></a></li>
						<%}%>  --%>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</footer>
