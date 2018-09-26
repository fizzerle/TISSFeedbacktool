package at.ac.tuwien.tiss.feedbacktool;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides the user settings
 */
@SessionScoped
@Named("settings")
public class UserSettings implements Serializable {

    private Locale locale;

    /**
     * creates the user settings and sets the locale to german
     */
    public UserSettings() {
        locale = Locale.GERMAN;
    }

    /**
     * Returns the locale for the user
     *
     * @return locale for the user
     */
    public Locale getLocale() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params =
                fc.getExternalContext().getRequestParameterMap();
        if (params.get("locale") != null) {
            locale = new Locale(params.get("locale"));
        }

        return locale;
    }

    /**
     * Sets the locale
     *
     * @param locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


}
