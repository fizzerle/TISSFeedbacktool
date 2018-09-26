package at.ac.tuwien.tiss.util.fe.jsf;

import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Provides utility functions for the feedback tool
 */
public class FeedbacktoolUtil {

    /**
     * Gets the current Timestamp
     *
     * @return the current Timestamp (using the sql timestamp)
     */
    public static Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new Timestamp(today.getTime());
    }

    /**
     * Reads a file and puts the content in a string
     *
     * @param filename the filename of the file to read
     * @return a string containing the content of the file
     */
    public static String readQuestionsFromFile(String filename) {
        ClassLoader currentClassLoader = JsfUtil.getCurrentClassLoader("");
        InputStream resourceAsStream = currentClassLoader.getResourceAsStream(filename);
        return readFromInputStream(resourceAsStream);
    }

    /**
     * Helper function to convert a stream to UTF-8 String
     *
     * @param inputStream the inputStream to convert
     * @return a String containing the content of the input stream in UTF-8
     */
    public static String readFromInputStream(InputStream inputStream) {

        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultStringBuilder.toString();
    }

    /**
     * Gets the supported Locales
     *
     * @return a list of the supported Locales
     */
    public static List<Locale> getSupportedLocales() {
        List<Locale> locales = new LinkedList<>();
        Iterator<Locale> supportedLocales = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
        while (supportedLocales.hasNext()) {
            locales.add(supportedLocales.next());
        }
        return locales;
    }
}
