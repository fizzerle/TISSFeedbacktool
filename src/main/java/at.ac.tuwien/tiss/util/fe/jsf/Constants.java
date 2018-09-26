package at.ac.tuwien.tiss.util.fe.jsf;

import java.util.ArrayList;

/**
 * This class provides the constants for the project
 */
public class Constants {
    // Properties (user configurable)
    public static final int WEEKS_BANNED = Integer.parseInt(Configurations.getProperty(Properties.WEEKS_BANNED));
    public static final String JSON_FILENAME = Configurations.getProperty(Properties.JSON_FILENAME);
    public static final String CHART_COLORS = Configurations.getProperty(Properties.CHART_COLORS);

    // Constants (not user configurable)
    public static final String PATH_CONFFILE = "Feedback.properties";
    public final static ArrayList<String> SCALALABELS = new ArrayList<String>() {{
        add("trifft zu");
        add("trifft eher zu");
        add("neutral");
        add("trifft eher nicht zu");
        add("trifft nicht zu");
    }};
}
