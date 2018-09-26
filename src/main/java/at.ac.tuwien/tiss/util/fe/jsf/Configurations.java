package at.ac.tuwien.tiss.util.fe.jsf;

import java.util.Properties;

/**
 * Copied the idea from https://stackoverflow.com/questions/29896927/constants-and-properties-in-java
 * This class loads the properties from the properties file
 */
public final class Configurations {

    private static Configurations instance = null;
    private Properties properties = null;

    /**
     * Constructs the configuration object
     */
    private Configurations() {
        this.properties = new Properties();
        try {
            properties.load(JsfUtil.getCurrentClassLoader("").getResourceAsStream(Constants.PATH_CONFFILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates the instance is synchronized to avoid multithreads problems
     */
    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new Configurations();
        }
    }

    /**
     * Get the properties instance. Uses singleton pattern
     */
    public static String getProperty(String key) {
        // Uses singleton pattern to guarantee the creation of only one instance
        if (instance == null) {
            createInstance();
        }
        return instance.getProp(key);
    }

    /**
     * Get a property of the property file
     */
    public String getProp(String key) {
        String result = null;
        if (key != null && !key.trim().isEmpty()) {
            result = this.properties.getProperty(key);
        }
        return result;
    }

    /**
     * Override the clone method to ensure the "unique instance" requeriment of this class
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}