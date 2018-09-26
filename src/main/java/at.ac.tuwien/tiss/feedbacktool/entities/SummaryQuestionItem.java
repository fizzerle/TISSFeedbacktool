package at.ac.tuwien.tiss.feedbacktool.entities;

import at.ac.tuwien.tiss.util.fe.jsf.Constants;

/**
 * This class provides a summary for scala Questions. It provides functionality to calculate the average of the answers in different forms
 */
public class SummaryQuestionItem {

    private int questionID;
    private String question;
    private int[] counts;

    /**
     * Creates a SummaryQuestionItem
     *
     * @param questionID the question id of the question which the summary is for
     * @param question   a string containing the question
     */
    public SummaryQuestionItem(int questionID, String question) {
        this.questionID = questionID;
        this.question = question;
        counts = new int[5];
    }

    /**
     * Creates a SummaryQuestionItem
     *
     * @param questionID the question id of the question which the summary is for
     * @param question   a string containing the question
     * @param counts     a int[] containing the number of people who answered this specific SCALA answer
     */
    public SummaryQuestionItem(int questionID, String question, int[] counts) {
        this.questionID = questionID;
        this.question = question;
        this.counts = counts;
    }

    /**
     * Gets questionID
     *
     * @return value of questionID
     */
    public int getQuestionID() {
        return questionID;
    }

    /**
     * Sets the field questionID to the given parameter
     *
     * @param questionID the parameter which the field gets assigned to
     */
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    /**
     * Gets question
     *
     * @return value of question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the field question to the given parameter
     *
     * @param question the parameter which the field gets assigned to
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets counts
     *
     * @return value of counts
     */
    public int[] getCounts() {
        return counts;
    }

    /**
     * Sets the field counts to the given parameter
     *
     * @param counts the parameter which the field gets assigned to
     */
    public void setCounts(int[] counts) {
        this.counts = counts;
    }

    /**
     * Calculates the average answer of the questions with QuestionType SCALA
     *
     * @return a String representation of the average SCALA answer
     */
    public String getAverageAnswer() {
        double overallCount = 0;
        double count = 0;
        for (int i = 0; i < counts.length; i++) {
            overallCount += (i + 1) * counts[i];
            count += counts[i];
        }
        if (count == 0) return "" + -1;
        return Math.round((overallCount / count) - 1) + "";
    }

    /**
     * Calculates the average answer of the questions with QuestionType SCALA but returns the correct label for the scala label
     *
     * @return a string representing the average answer with a scala label
     */
    public String getAverageScalaLabel() {
        int averageAnswer = Integer.parseInt(getAverageAnswer());
        if (averageAnswer == -1) return "";
        return Constants.SCALALABELS.get(averageAnswer);
    }
}
