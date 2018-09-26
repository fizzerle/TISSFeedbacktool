package at.ac.tuwien.tiss.feedbacktool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Holds the application settings
 */
@ApplicationScoped
@Named("appSettings")
public class AppSettings {
    /**
     * creates the app settings
     */
    public AppSettings() {
    }

    /**
     * Gets the application Name
     *
     * @return a string containing the application name
     */
    public String getApplicationName() {
        return "Tiss Feedback Tool";
    }
}