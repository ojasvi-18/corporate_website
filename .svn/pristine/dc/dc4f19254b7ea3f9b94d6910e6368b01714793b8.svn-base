package com.zillious.corporate_website.ui.beans;

import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.navigation.WebsiteActions;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

/**
 * @author nishant.gupta
 *
 */
public class WebsiteBean {

    static final int MAX_SIZE_PER_FILE_IN_KB = 200;

    public static boolean isUIActionAllowed(ZilliousSecurityWrapperRequest request, WebsiteActions action) {
        User loggedUser = SessionStore.getLoggedInUser(request);
        return isUIActionAllowed(loggedUser, action);
    }

    public static boolean isUIActionAllowed(User user, WebsiteActions action) {
        UserRoles role = (user == null) ? UserRoles.NOT_LOGGED_IN : user.getUserRole();
        return action.isRoleDefaultAllowed(role);
    }

}
