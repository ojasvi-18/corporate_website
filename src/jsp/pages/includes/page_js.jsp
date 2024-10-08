<%@page import="com.zillious.corporate_website.i18n.Country"%>
<%@page import="com.zillious.corporate_website.i18n.Language"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page import="com.zillious.corporate_website.ui.beans.I18NBean"%>
<%@page import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%
	ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
	ZilliousSecurityWrapperResponse zilliousResponse = SecurityBean.getResponse(response);
	String defLanguage = (String)zilliousRequest.getAttribute("defLanguage");
%>
<%-- jQuery --%>
<script src="/static/js/jquery/jquery-2.1.4-min.js"></script>

<%-- I18N Script --%>
<script src="/static/js/jquery/i18n/jquery.i18n.properties-min-1.0.9.js"></script>

<script type="text/javascript">
var ua = window.navigator.userAgent;    
var msie = ua.indexOf("MSIE ");
if (msie > 0) {
  	window.location.href = "<%=WebsiteActions.BROWSER_UNSUPPORTED.getActionURL(zilliousRequest, zilliousResponse, null, true)%>";
}
    
 <%-- var countryLang = <%= Country.getCountryLanguageJSON() %>;--%>
 <%--var countryLang = <%= Language.getLanguageJSON() %>--%>
  var i18n;
  <%
  String languageCode = I18NBean.getSelectedLanguage(zilliousRequest);
  %>
  var lang = "<%=languageCode%>";
  var langDir = <%= Language.getLanguageFromCodeWithPropertyFileCheck(languageCode).getRTLDirection()%>;
  var newsletter;
  var WEB_UTILS;
  $(document).ready(function() {
    
    
    $.i18n.properties({ 
      name: 'messages', 
      path: '/static/bundles/', 
      mode: 'both', 
      language: lang, 
      callback: null
       });
    
    <%--i18n = new i18nJS($("form[name='i18nLocaleForm']"), "<%=defLanguage %>");--%>
    if(langDir == true) {
      $(".client-logos").css("right","0");
      $(".newsletter").find("button[name='addSubscription']").css("right","auto").css("left","0");
      $(".profile img").css("margin-right","-25%").css("margin-left","0");
      $(".hero.sub-header .cta").css("right","auto").css("left","0");
      $(".more-info-meta").css("margin-left","auto").css("margin-right","4%");
      $("#langNav, .flyout-trigger").addClass("rtl");
    }
    
    if($("#newsletter").length > 0) {
	   newsletter = new newsLetterJS($("#newsletter"), "<%=WebsiteActions.NEWSLETTER.getActionURL(zilliousRequest,zilliousResponse, new String[] {"addSubscription"}, true)%>");
    }
    
    <%--Web-utils --%>
    WEB_UTILS = new function() {
      this.SYS_VARS = new Object();
      this.initSysVars = function () {
         this.SYS_VARS.CUR_DATE = new Date();
      }
      
      this.initSysVars();
      
      this.getSysCurrentDate = function() {
        return this.copyDate(this.SYS_VARS.CUR_DATE);  
      }
      
      this.copyDate = function(dt) {
        var dt2 = new Date();
        dt2.setTime(dt.getTime());
        return dt2;
      }
      
      this.initDate = function(inputField, dFormat) {
        var curDate = WEB_UTILS.getSysCurrentDate();
        $(inputField).datepicker({
          beforeShow : function(input) {
            $(input).css({
              "position" : "relative",
              "z-index" : 1000
            });
          },
          onSelect: function() {
            $(this).change(); 
          },
          onClose: function(input) {
            $(inputField).css({
              "z-index" : 1
            });
          },
          maxDate : curDate,
          numberOfMonths : 2,
          showOn : "focus",
          dateFormat: (dFormat != null ? dFormat : "mm/dd/yy"),
          isRTL:langDir,
        });
        
      }

      this.initPreviousDate = function(inputField, dFormat) {
        var curDate = WEB_UTILS.getSysCurrentDate();
        $(inputField).datepicker({
          beforeShow : function(input) {
            $(input).css({
              "position" : "relative",
              "z-index" : 1000
            });
          },
          onSelect: function() {
            $(this).change(); 
          },
          onClose: function(input) {
            $(inputField).css({
              "z-index" : 1
            });
          },
          maxDate : curDate,
          numberOfMonths : 2,
          showOn : "focus",
          dateFormat: (dFormat != null ? dFormat : "mm/dd/yy"),
          isRTL:langDir,
        });
        
      }
      
      this.addDaysToDate = function(dt, days) {
        dt2 = new Date(dt.getTime() + (1000*60*60*24*days));
        return dt2;
      }
      
      this.isValidNonEmptyString = function(cStr) {
        return cStr && cStr != "";
      }
      
      this.isValidEmail = function(email) {
        return email.match(/^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$/);
       };
       
       this.isValidPassword = function(cStr) {
         return cStr.match(/^[0-9a-zA-Z\!\@\#\$\%\^\&\*\(\)\-\_\+\=\{\}\[\]\|\:\;\<\,\>\.\?\~]{8,}$/);
       };
       
       this.addError = function(errorMsg) {
         alert(errorMsg);
       };
       
       this.setSelectBoxByValue = function(selectElem, value) {
         $(selectElem).val(value).prop('selected', true);
       }
    }
    //initialising nice-select drop down menu plugin
    <%--$("select").niceSelect();--%>
    
    <%--Custom menu js code --%>
		//Navi hover
		$('ul li.dropdown').hover(function () {
			$(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn();
		}, function () {
			$(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut();
		});
    
  });
</script>

<script src="/static/js/all1-min.js"></script>

<%-- General --%>
<script src="/static/js/all2-min.js"></script>

<%-- LivIcons --%>
<script src="/static/js/all3-min.js"></script>

<%-- Google Map --%>
<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCQXurIOUqVv64pnQ7kKwgua8bng6cf-mU&language=<%=languageCode%>"></script>


