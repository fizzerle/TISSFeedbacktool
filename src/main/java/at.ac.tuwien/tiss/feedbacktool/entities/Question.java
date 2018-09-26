package at.ac.tuwien.tiss.feedbacktool.entities;

import java.io.Serializable;
import java.util.Locale;

/**
 * The class provides the default implementation of a Question
 */
public class Question implements Serializable {
    private int id;
    private String text;
    private QuestionType type;
    private Locale locale;

    /**
     * Creates a questionItem with the given fields set to the parameters
     *
     * @param id     a int representing the id of a question
     * @param text   a string containing the text
     * @param type   the text type of the text
     * @param locale the locale of the question
     */
    public Question(int id, String text, QuestionType type, Locale locale) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.locale = locale;
    }

    /**
     * Creates a questionItem with the given fields set to the parameters
     *
     * @param text a string containing the text
     * @param id   an int describing the question id
     */
    public Question(int id, String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * Gets id
     *
     * @return value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the field id to the given parameter
     *
     * @param id the parameter which the field gets assigned to
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets text
     *
     * @return value of text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the field text to the given parameter
     *
     * @param text the parameter which the field gets assigned to
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets type
     *
     * @return value of type
     */
    public QuestionType getType() {
        return type;
    }

    /**
     * Sets the field type to the given parameter
     *
     * @param type the parameter which the field gets assigned to
     */
    public void setType(QuestionType type) {
        this.type = type;
    }

    /**
     * Gets locale
     *
     * @return value of locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the field locale to the given parameter
     *
     * @param locale the parameter which the field gets assigned to
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}