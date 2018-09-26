package at.ac.tuwien.tiss.feedbacktool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * copied from the jShowcase
 */

@ApplicationScoped
@Named("security")
public class SecurityBean {
    public SecurityBean() {
    }

    public boolean isAuthenticated() {
        return true;
    }

    public String getName() {
        return "showcase";
    }

    public String getLoginEntryPoint() {
        return "/login.xhtml";
    }

    public String getLogoutEntryPoint() {
        return "/logout.xhtml";
    }
}