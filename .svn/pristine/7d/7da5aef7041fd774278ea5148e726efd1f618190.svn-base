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
    <jsp:param name="title" value="Terms of Use"/>
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
                    <h1><z:message key="corp.terms.maintitle">Terms of Use</z:message></h1>
                    <p><z:message key="corp.terms.mainsubtitle">For further details or concerns please contact us.</z:message></p>
                    <a href="<%= WebsiteActions.CONTACT.getActionURL(secureRequest, secureResponse) %>" class="button border round cta"><z:message key="corp.common.contactus">Contact Us</z:message></a>
                </div>
            </div> 
        </section>

        <%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param value="<%= WebsiteActions.TERMS.getDisplayName() %>" name="website_action"/></jsp:include>

        <%-- Main Content --%>
        <section class="section primary contact" id="s-contact">
            <div class="container">
                <p><z:message key="corp.terms.lastupdatedtitle">Last updated: 14th November, 2015</z:message></p>
                <p><z:message key="corp.terms.lastupdatedtext1">This web site (the "Site") is published and maintained by Zillious Solutions Private Limited ("Zillious"), a company incorporated and existing in accordance with the laws of the Republic of India. When you access, browse or use this Site you accept, without limitation or qualification, the terms and conditions set forth below. When you access any sub-site (whether belonging to an 'associate' of Zillious or otherwise) through this site, then such sub-site may have its own terms and conditions of use which is specific to such sub-site. Sub-sites may contain such additional terms and conditions of use as may be set out in such sub-site.</z:message></p>
                <p><z:message key="corp.terms.lastupdatedtext2">These Terms and Conditions of Use and any additional terms posted on this Site together constitute the entire agreement between Zillious and you with respect to your use of this Site.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.sitecontenttitle">Site and Its Contents</z:message></h2>
                <p><z:message key="corp.terms.sitecontenttext1">This Site is only for your personal use. You shall not distribute, exchange, modify, sell or transmit anything you copy from this Site, including but not limited to any text, images, audio and video, for any business, commercial or public purpose.</z:message></p>
                <p><z:message key="corp.terms.sitecontenttext2">As long as you comply with the terms of these Terms and Conditions of Use, Zillious grants you a non-exclusive, non-transferable, limited right to enter, view and use this Site. You agree not to interrupt or attempt to interrupt the operation of this Site in any way.</z:message></p>
                <p><z:message key="corp.terms.sitecontenttext3">Access to certain areas of the Site may only be available to registered members. To become a registered member, you may be required to answer certain questions. Answers to such questions may be mandatory and/or optional. You represent and warrant that all information you supply to us, about yourself, and others, is true and accurate.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.ownershiptitle">Ownership</z:message></h2>
                <p><z:message key="corp.terms.ownershiptext1">All materials on this Site, including but not limited to audio, images, software, text, icons and such like (the "Content"), are protected by copyright under international conventions and copyright laws. You cannot use the Content, except as specified herein. You agree to follow all instructions on this Site limiting the way you may use the Content.</z:message></p>
                <p><z:message key="corp.terms.ownershiptext2">There are a number of proprietary logos, service marks and trademarks found on this Site whether owned/used by Zillious or otherwise. By displaying them on this Site, Zillious is not granting you any license to utilize those proprietary logos, service marks, or trademarks. Any unauthorized use of the Content may violate copyright laws, trademark laws, the laws of privacy and publicity, and civil and criminal statutes.</z:message></p>
                <p><z:message key="corp.terms.ownershiptext3">You may download such copy/copies of the Content to be used only by you for your personal use at home unless the subsite you are accessing states that you may not. If you download any Content from this Site, you shall not remove any copyright or trademark notices or other notices that go with it.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.othersrightstitle">Others' rights</z:message></h2>
                <p><z:message key="corp.terms.othersrightstext1">If this Site contains bulletin boards, chat rooms, access to mailing lists or other message or communication facilities, you agree to use the same only to send and receive messages and materials that are proper and related thereto. By way of example and not as a limitation, you agree that when using the Site or any facility available herefrom, you shall not do any of the following:</z:message>
                <ol>
                <li><z:message key="corp.terms.othersrights.point1">Defame, abuse, harass, stalk, threaten or otherwise violate the legal rights (such as rights of privacy and publicity) of others</z:message></li>
                <li><z:message key="corp.terms.othersrights.point2">Publish, post, distribute or disseminate any defamatory, infringing, obscene, indecent or unlawful material or information</z:message></li>
                <li><z:message key="corp.terms.othersrights.point3">Upload or attach files that contain software or other material protected by intellectual property laws (or by rights of privacy and publicity) unless the User owns or controls the rights thereto or has received all consents therefor as may be required by law</z:message></li>
                <li><z:message key="corp.terms.othersrights.point4">Upload or attach files that contain viruses, corrupted files or any other similar software or programs that may damage the operation of another's computer</z:message></li>
                <li><z:message key="corp.terms.othersrights.point5">Delete any author attributions, legal notices or proprietary designations or labels in any file that is uploaded</z:message></li>
                <li><z:message key="corp.terms.othersrights.point6">Falsify the origin or source of software or other material contained in a file that is uploaded</z:message></li>
                <li><z:message key="corp.terms.othersrights.point7">Advertise or offer to sell any goods or services, or conduct or forward surveys, contests or chain letters, or download any file posted by another user of a Forum that the User knows, or reasonably should know, cannot be legally distributed in such manner.</z:message></li>
                </ol>
                </p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.usersmaterialtitle">User's Material</z:message></h2>
                <p><z:message key="corp.terms.usersmaterialtext1">You are prohibited from posting or transmitting any defamatory, libelous, obscene, pornographic, profane, threatening or unlawful material or any material that could constitute or encourage conduct that would be considered a criminal offense or give rise to civil liability, or otherwise violate any law.</z:message></p>
                <p><z:message key="corp.terms.usersmaterialtext2">Zillious assumes no liability or responsibility arising from the contents of any communications containing any defamatory, erroneous, inaccurate, libelous, obscene or profane material. Zillious may change, edit, or remove any user material or conversations that are illegal, indecent, obscene or offensive, or that violates Zillious's policies in any way.</z:message></p>
                <p><z:message key="corp.terms.usersmaterialtext3">Zillious will fully cooperate with any law enforcement authorities or court order requesting or directing Zillious to disclose the identity of anyone posting such materials.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.zilliousrightstitle">Zillious Rights</z:message></h2>
                <p><z:message key="corp.terms.zilliousrightstext1">If you send any communications or materials to the Site by electronic mail or otherwise, including any comments, data, questions, suggestions or the like, all such communications are, and will be treated by Zillious, as non-confidential.</z:message></p>
                <p><z:message key="corp.terms.zilliousrightstext2">You hereby give up any and all claim that any use of such material violates any of your rights including moral rights, privacy rights, proprietary or other property rights, publicity rights, rights to credit for material or ideas, or any other right, including the right to approve the way Zillious uses such material.</z:message></p>
                <p><z:message key="corp.terms.zilliousrightstext3">Any material submitted to this Site may be adapted, broadcast, changed, copied, disclosed, licensed, performed, posted, published, sold, transmitted or used by Zillious anywhere in the world, in any medium, forever.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.transmittedmaterialtitle">Transmitted Material</z:message></h2>
                <p><z:message key="corp.terms.transmittedmaterialtext1">Internet transmissions are never completely private or secure. You understand that any message or information you send to this Site may be read or intercepted by others unless there is a special notice that a particular message (for example, credit card information) is encrypted (send in code). Sending a message to Zillious does not cause Zillious to have any special responsibility to you.</z:message></p>
                <p><z:message key="corp.terms.transmittedmaterialtext2">The copyright in the contents of this website belong to Zillious. Accordingly Zillious reserves all rights. Copying of part or all the contents of this website without permission of Zillious is prohibited.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.conteststitle">Contests and interactions</z:message></h2>
                <p><z:message key="corp.terms.conteststext1">This Site may contain contests that require you to send in material or information about yourself or offer prizes. Each contest has its own rules, which you must read and agree to before you participate.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.disclaimertitle">Disclaimer</z:message></h2>
                <p><z:message key="corp.terms.disclaimertext1">The material in this Site could include technical inaccuracies or typographical errors. Zillious may make changes or improvements at any time.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext2">The materials on this site are provided on an 'As Is' basis, without warranties of any kind either expressed or implied. To the fullest extent permissible pursuant to applicable law, Zillious disclaims all warranties of merchantibility and fitness for a particular purpose.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext3">Zillious does not warrant that the functions contained in this site will be uninterrupted or error free, that defects will be corrected, or that this site or the servers that make it available are free of viruses or other harmful components, but shall endeavour to ensure your fullest satisfaction.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext4">Zillious does not warrant or make any representations regarding the use of or the result of the use of the material on the site in terms of their correctness, accuracy, reliability, or otherwise, insofar as such material is derived from other service providers such as airlines, hotel owners and tour operators.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext5">You acknowledge that this Website is provided only on the basis set out in these terms and conditions. Your uninterrupted access or use of this Website on this basis may be prevented by certain factors outside our reasonable control including, without limitation, the unavailability, inoperability or interruption of the Internet or other telecommunications services or as a result of any maintenance or other service work carried out on this Website. Zillious does not accept any responsibility and will not be liable for any loss or damage whatsoever arising out of or in connection with any ability/inability to access or to use the Site.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext6">You also acknowledge that through this Site, Zillious merely provides information services in order to facilitate highest quality services to you.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext7">Zillious will not be liable to you or to any other person for any direct, indirect, incidental, punitive or consequential loss, damage, cost or expense of any kind whatsoever and howsoever caused from out of your usage of this Site.</z:message></p>
                <p><z:message key="corp.terms.disclaimertext8">Notwithstanding anything else to the contrary contained elsewhere herein or otherwise at law, Zillious's liability (whether by way of indemnification to you or otherwise) shall be restricted to a maximum of INR 1000 (Indian Rupees One Thousand only).</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.tnctitle">Terms and Conditions of Use</z:message></h2>
                <p><z:message key="corp.terms.tnctext1">Zillious may add to, change or remove any part of these Terms and Conditions of Use at any time, without notice. Any changes to these Terms and Conditions of Use or any terms posted on this Site apply as soon as they are posted. By continuing to use this Site after any changes are posted, you are indicating your acceptance of those changes.</z:message></p>
                <p><z:message key="corp.terms.tnctext2">Zillious may add, change, discontinue, remove or suspend any other Content posted on this Site, including features and specifications of products described or depicted on the Site, temporarily or permanently, at any time, without notice and without liability.</z:message></p>
                <p><z:message key="corp.terms.tnctext3">Zillious reserves the right to undertake all necessary steps to ensure that the security, safety and integrity of Zillious's systems as well as its clients' interests are and remain, well-protected.</z:message></p>
                <p><z:message key="corp.terms.tnctext4">Towards this end, Zillious may take various steps to verify and confirm the authenticity, enforceability and validity of orders placed by you.</z:message></p>
                <p><z:message key="corp.terms.tnctext5">If Zillious, in its sole and exclusive discretion, concludes that the said orders are not or do not reasonably appear to be, authentic, enforceable or valid, then Zillious may cancel the said orders at any time up to 5 hours before the scheduled time of departure of the relevant flight or 5 hours before the expected date of visit to any property booked through Zillious.</z:message></p>
                
                <hr class="stripes" />
                <h2><z:message key="corp.terms.provisionstitle">General Provisions</z:message></h2>
                <p><z:message key="corp.terms.provisionstext1">Zillious reserves its exclusive right in its sole discretion to alter, limit or discontinue the Site or any material posted herein, in any respect. Zillious shall have no obligation to take the needs of any User into consideration in connection therewith.</z:message></p>
                <p><z:message key="corp.terms.provisionstext2">Zillious reserves its right to deny in its sole discretion any user access to this Site or any portion hereof without notice.</z:message></p>
                <p><z:message key="corp.terms.provisionstext3">No waiver by Zillious of any provision of these Terms and Conditions shall be binding except as set forth in writing and signed by its duly authorized representative.</z:message></p>
                <p><z:message key="corp.terms.provisionstext4">If any dispute arises between you and Zillious during your use of the Site or thereafter, in connection with and arising from your use or attempt to use this Site, the dispute shall be referred to arbitration. The place of arbitration shall be Delhi. The arbitration proceedings shall be in the English language.</z:message></p>
                <p><z:message key="corp.terms.provisionstext5">The said arbitration proceedings shall be governed and construed in accordance with the Arbitration and Conciliation Act, 1996 and modifications thereof as in force at the relevant time.</z:message></p>
                <p><z:message key="corp.terms.provisionstext6">These terms and conditions are governed by and shall be construed in accordance with the laws of the Republic of India and any dispute shall exclusively be subject to the jurisdiction of the appropriate Courts situated at Delhi, India.</z:message></p>
            </div>
        </section>

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
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
