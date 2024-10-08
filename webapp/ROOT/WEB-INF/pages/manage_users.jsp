<jsp:directive.include file="/WEB-INF/pages/includes/page_top.jsp" />
<%@page import="com.zillious.corporate_website.ui.beans.SecurityBean"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest"%>
<%@page
	import="com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse"%>
<%@page
	import="com.zillious.corporate_website.ui.navigation.WebsiteActions"%>
<%@page import="java.util.List"%>
<%@page import="com.zillious.corporate_website.data.user.User"%>
<%@page import="com.zillious.corporate_website.ui.beans.WebsiteBean"%>
<%@page import="com.zillious.corporate_website.data.user.UserRoles"%>
<%
    ZilliousSecurityWrapperRequest secureRequest = SecurityBean
					.getRequest(request);
			ZilliousSecurityWrapperResponse secureResponse = SecurityBean
					.getResponse(response);

			String jsonString = "{\"userroles\" : [";
			//{"id":"A", "name":"Admin"},{"id":"E", "name":"Employee"}]}
			
			List<User> users = WebsiteBean.getUsersFromReq(request);
%>
<jsp:include flush="false" page="/WEB-INF/pages/includes/page_head.jsp">
	<jsp:param name="title" value="Manage Zillious Users" />
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
					<h1>Manage Users</h1>
					<p>You can add more users, or edit details of existing ones</p>
					<a href="javascript:void(0);" class="button border round cta"
						onclick="manage_users.gotoAdd()">Add New User</a>
				</div>
			</div>
		</section>

		<%-- Breadcrumb --%>
		<jsp:include page="/WEB-INF/pages/includes/breadcrumb.jsp"><jsp:param
				value="<%=WebsiteActions.MANAGE_USERS.getDisplayName()%>"
				name="website_action" /></jsp:include>

		<%-- Main Content --%>
		<section class="section primary contact" id="s-contact">
			<jsp:include flush="false"
				page="/WEB-INF/pages/includes/messages.jsp" />
			<div class="container">
				<header class="sep active" style="margin-bottom: 1em;">
					<div class="section-title">
						<h2>
							<i>Existing</i> Users
						</h2>
					</div>
				</header>
				<div id="tableDiv">
					<table id="existing_users" class="display"
						style="border-width: 2px; border-color: blue;; text-align: center;"
						width="100%" cellspacing="0" border="1">
						<thead>
							<tr>
								<th>ID</th>
								<th>Role</th>
								<th>Name</th>
								<th>Enabled</th>
								<th>Email</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<%
							    int i = 0;
																												    for(User user : users) {
							%>
							<tr id="tr_<%=++i%>">
								<td><%=user.getUserId()%></td>
								<td><%=user.getUserRole().getDisplayName()%></td>
								<td><%=user.getName()%></td>
								<td><%=user.isEnabled() ? "Enabled" : "Disabled"%></td>
								<td><%=user.getEmail()%></td>
								<td>
									<form>
										<input type="button" value="Edit User"
											onclick='manage_users.editUser($("#tr_<%=i%>"))' />
									</form>
								</td>
							</tr>
							<%
							    }
							%>
						</tbody>
					</table>
				</div>

				<header class="sep active"
					style="margin-top: 1em; margin-bottom: 1em;">
					<div class="section-title">
						<h2>
							Upload <b>User Mappings</b>
						</h2>
					</div>
				</header>
				
				<div style="width: 50%; margin-left: 20%;">
					<form id="userMappingForm" method="post">
						<div id="userMappingUploadDiv" style="width: 100%; padding: 5%;">
							Select a file:<input name="uploadMappingFile" style="display: inline-block; float: right;" type="file" accept=".csv,.txt" />
						</div>
						<center>
							<button name="addButton" onclick="add_mappings.uploadMappings()" type="button">Upload Mappings</button>
						</center>
					</form>
				</div>
		
				<br />
				

				<header class="sep active"
					style="margin-top: 1em; margin-bottom: 1em;">
					<div class="section-title">
						<h2>
							<b>Add</b>/<b>Modify</b> Users
						</h2>
					</div>
				</header>
		
				<div id="modifyUsersDiv" style="width: 50%; margin-left: 20%;">
					<form id="userForm" method="post">
						<div id="selectionDiv">
							<center><span id="singleUploadSpan">Single upload</span><input
								type="radio" name="numusers" value="single" checked="checked"/>
								<span id="bulkUploadSpan">Bulk upload</span> <input type="radio" name="numusers"
								value="bulk" />
								</center>
						</div>
						<div id="addUserDiv">
							<div style="min-height:17em;">
							<div id="singleUploadDiv">
								<table class="display"
									style="border-width: 2px; border-color: blue;; text-align: center;"
									width="100%" cellspacing="0" border="1">
									<thead>
										<tr>
											<th>Action</th>
											<th>
												<div id="actionDiv">Add New User</div>
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>Role</td>
											<td>
												<select name="role">
													<%
													    UserRoles[] roles = UserRoles.getLoggedInRoles();
														for (int roleCount = 0; roleCount < roles.length; roleCount++) {
															UserRoles role = roles[roleCount];						    			    
										    				jsonString += "{\"id\":\"" +role.serialize() + "\" , \"name\":\"" + role.getDisplayName() + "\"}" + (roleCount < (roles.length - 1) ? "," : "");
													%>
													<option value="<%=role.serialize()%>"><%=role.getDisplayName()%></option>
													<%
													    } 
										    			jsonString += "]}";
													%>
												</select>
											</td>
										</tr>
										<tr>
											<td>User ID</td>
											<td id="userIDTD"><input type="text" name="user_id"
												value="" size="30" maxlength="99" /></td>
										</tr>
										<tr>
											<td>Name</td>
											<td><input type="text" name="name" value="" size="50"
												maxlength="99" /></td>
										</tr>
										<tr>
											<td>Email</td>
											<td><input type="text" name="email" value="" size="50"
												maxlength="99" /></td>
										</tr>
										<tr>
											<td>Enabled</td>
											<td><input type="checkbox" name="enabled"
												checked="checked" /></td>
										</tr>
										<tr>
											<td>Password</td>
											<td>
												<div id="passwordDiv" style="display: none">
													Update Password? <input name="update_password"
														type="checkbox">
												</div> <input type="password" name="password" value="" size="50"
												maxlength="20" />
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div id="bulkUploadDiv" style="width: 100%; padding: 5%; display:none;">
								Select a file:<input name="uploadFile" style="display: inline-block; float: right;" type="file" accept=".csv,.txt" />
							</div>
							</div>
							<center>
								<button name="modButton" onclick="manage_users.modifyUser()"
									type="button">Add New User(s)</button>
							</center>
						</div>
					</form>
				</div>
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
	<script type="text/javascript"
		src="/static/js/jquery/jquery.dataTables.min.js"></script>
	<script type="text/javascript"
		src="/static/js/jquery/dataTables.jqueryui.min.js"></script>
	<link href="/static/css/jquery.dataTables.min.css" rel="stylesheet" />

	<%-- Page Specific Scripts --%>
	<script type="text/javascript">
		var manage_users;
		var add_mappings;
	    $(document).ready(function(){
	      manage_users = new ManageUsersJS($("#userForm"),"<%=WebsiteActions.MANAGE_USERS.getActionURL(secureRequest, secureResponse, new String[] {"modify"}, true)%>", <%=jsonString%>);
	      add_mappings = new UserMappingJS($("#userMappingForm"),"<%=WebsiteActions.MANAGE_USERS.getActionURL(secureRequest, secureResponse, new String[] {"uploadMappings"}, true)%>");
        });
  </script>
</body>

<jsp:directive.include file="/WEB-INF/pages/includes/page_bottom.jsp" />
