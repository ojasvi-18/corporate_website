<%@page import="com.zillious.corporate_website.utils.StringUtility"%>
<%@page import="com.zillious.corporate_website.data.AttendanceDTO"%>
<%@page import="com.zillious.corporate_website.ui.session.SessionStore"%>
<%@page import="com.zillious.corporate_website.ui.beans.WebsiteBean"%>
<%@page import="com.zillious.corporate_website.ui.beans.AttendanceBean"%>
<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page
	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
	<%@page import="java.util.Map" %>
	<%@page import="java.util.List" %>
	<%@page import="com.zillious.corporate_website.data.user.User" %>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean
					.getRequest(request);
			ZilliousSecurityWrapperResponse secureResponse = SecurityBean
					.getResponse(response);
			
			User loggedInUser = SessionStore.getLoggedInUser(secureRequest);			
			boolean isAdmin = loggedInUser.getUserRole().isAdministrator();
			
			Map<User, AttendanceDTO> records = AttendanceBean.getRecordsFromRequest(secureRequest);
			String heading = AttendanceBean.getAttendancePageHeadingInRequest(secureRequest);
			boolean isShowTable =( isAdmin && (records != null));
			boolean isShowCalendar= (records!= null && !records.isEmpty());
			String userId = ((records == null || records.isEmpty())? null : String.valueOf(records.keySet().iterator().next().getUserId()));
			String attendanceAsJSON = AttendanceBean.convertRecordsToJSON(records);
%>
<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
	<jsp:param name="title" value="Employee Attendance Report" />
	<jsp:param name="meta_description"
		value="Zillious Solution is Asias leading Travel Technology Provider. Empowering over USD 1 Billion worth travel transaction annually." />
</jsp:include>

<body class="further contact">
	<%-- SITE CONTENT --%>
	<div id="site-content">

		<%-- Header --%>
		<jsp:include flush="false"
			page="/WEB-INF/pages/includes/header_mainnav.jsp" />

		<%-- Sub-Header --%>
		<section class="hero sub-header">
			<div class="container inactive">
				<div class="sh-title-wrapper">
					<h1>Attendance Report</h1>
					<p>This module shows the attendance records of the employees</p>
				</div>
			</div>
		</section>

		<%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param
				value="<%=WebsiteActions.ATTENDANCETRACKER.getDisplayName()%>"
				name="website_action" /></jsp:include>

		<%-- Main Content --%>
		<section class="section primary contact" id="s-contact">
			<jsp:include flush="false"
				page="/WEB-INF/pages/includes/messages.jsp" />
			<div class="container">
				<div>
					<div id="messageDiv">Please mention the dates for which
						attendance is required to be shown</div>
					<form class="h5-valid" id="contentForm" method="post">
						<fieldset>
							<div class="form-element">
                     		    <input type="text" name="startdate" class="box" required maxlength="50">
	                            <label>Start Date(DD/MM/YYYY)</label>
	                        </div>
	                        <div class="form-element">
	                            <input type="text" name="enddate" class="box" maxlength="20">
	                            <label>End Date(DD/MM/YYYY)</label>
	                        </div>
						</fieldset>
						<button type="submit" class="button large full-width brand-1">Get Attendance</button>
					</form>
				</div>
				<% if(isShowTable) { %>
				<header class="sep active" style="margin-bottom: 1em;">
					<div class="section-title">
						<h2>
							<%=heading%>
						</h2>
					</div>
				</header>
				<div id="tableDiv">
					<table id="user_attendance" class="display" style="border-width: 2px; border-color: blue; ; text-align: center;" width="100%" cellspacing="0" border="1">
						<thead>
							<tr>
								<th>ID</th>
								<th>Name</th>
								<th>Email</th>
								<th>Present</th>
								<th>Absent</th>
								<th>Half Days</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<%
							    for(User user : records.keySet()) {
							        AttendanceDTO dto = records.get(user);
							        String userID = user.getUserId();
							        if(userID == null) {
							            userID = user.getDeviceUserID();
							        }
							%>
							<tr>
								<td><%=userID%></td>
								<td><%=StringUtility.trimWithNullAndEmptyAsUnset(user.getName())%></td>
								<td><%=StringUtility.trimWithNullAndEmptyAsUnset(user.getEmail())%></td>
								<td><%=dto.getNumPresent()%></td>
								<td><%=dto.getNumAbsent()%></td>
								<td><%=dto.getNumHalfDays()%></td>
								<td><button onclick="attrkObj.showFullCalendar('<%=userID %>')" type="button">Show More</button></td>
							</tr>
							<%
							    }
							%>
						</tbody>
					</table>
				</div>
				<% } %>
				
				<% if(isShowCalendar) { %>
				<div id="calendarContainer" <% if(isAdmin) {%>style="display:none;"<%}%>>
				<header class="sep active" style="margin-bottom: 1em; margin-top: 2em;">
					<div class="section-title">
						<h2 id="calendar_header">
							User ID: <i><%=userId %></i>
						</h2>
					</div>
				</header>
				<div id="calendarDiv">
				</div>
				</div>
				<% } else if(records != null) {%>
					No Data Available
				<%} %>
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

<%if(isShowTable) {%>
		<script type="text/javascript" src="/static/js/jquery/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="/static/js/jquery/dataTables.jqueryui.min.js"></script>
	<link href="/static/css/jquery.dataTables.min.css" rel="stylesheet" />
<%} %>

<% if(isShowCalendar) { %>
	<script type="text/javascript" src="/static/js/jquery/fullcalendar/moment.min.js"></script>
	<script type="text/javascript" src="/static/js/jquery/fullcalendar/fullcalendar.min.js"></script>
	<link href="/static/css/fullcalendar.css" rel="stylesheet" />
<%} %>
	<%-- Page Specific Scripts --%>
	<script type="text/javascript">
	var attrkObj;
	    $(document).ready(function(){
	    	attrkObj = new AttendanceTracker($("#contentForm"), "<%=WebsiteActions.ATTENDANCETRACKER.getActionURL(secureRequest, secureResponse, new String[]{"getAttendance"}, true)%>", <%= isShowCalendar%>, <%=isAdmin%>, <%=attendanceAsJSON%>);
    });
  </script>

<script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
<link href="/static/css/jquery-ui.min.css" rel="stylesheet" />

</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
