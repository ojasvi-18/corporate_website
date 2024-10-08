<!DOCTYPE html>
<%@page import="com.zillious.corporate_website.i18n.Language" %>
<%@page import="com.zillious.corporate_website.ui.beans.I18NBean"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%
ZilliousSecurityWrapperRequest zillousRequest = SecurityBean.getRequest(request);
Language lang =  Language.getLanguageFromCodeWithPropertyFileCheck(I18NBean.getSelectedLanguage(zillousRequest)) ;
String direction = Language.getDirectionString(lang.getRTLDirection());

%>
<!--[if IE 7]>     <html class="no-js ie ie7 lte9 lte8 lte7"> <![endif]-->
<!--[if IE 8]>     <html class="no-js ie ie8 lte9 lte8"> <![endif]-->
<!--[if IE 9]>     <html class="no-js ie ie9 lte9"> <![endif]-->
<!--[if gt IE 9]>  <html class="no-js"> <![endif]-->
<!--[if !IE]><!--> <html class="no-js" dir="<%=direction %>"> <!--<![endif]-->
