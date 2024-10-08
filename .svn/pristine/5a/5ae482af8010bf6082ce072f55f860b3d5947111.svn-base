package com.zillious.corporate_website.ui.beans;

import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.ui.navigation.WebsiteActions;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

public class UIBean {
    private static final Logger s_logger         = Logger.getInstance(UIBean.class);
    private static final String CURRENT_UIACTION = "UIBEAN_CURRENCT_UIACTION";

    public static final String  FWD_URL_PARAM    = "fwd";

    private UIBean() {
    }

    public static String getHomePageLink(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        return WebsiteActions.HOME.getActionURL(request, response);
    }

    public static void setCurrentUIAction(ZilliousSecurityWrapperRequest request, WebsiteActions action) {
        request.setAttribute(CURRENT_UIACTION, action);
    }

    public static WebsiteActions getCurrentUIAction(ZilliousSecurityWrapperRequest request) {
        return (WebsiteActions) request.getAttribute(CURRENT_UIACTION);
    }
    
 }
