package at.ac.tuwien.tiss.feedbacktool.entities;

import at.ac.tuwien.tiss.util.fe.jsf.Constants;

import java.sql.Timestamp;

public class Answer {
    private Question question;
    private String text;
    private String contractorName;
    private String username;
    private Timestamp submissionTime;
    private Integer numberOfPeopleWhoAnsweredThis;

    /**
     * Creates a questionItem with the given fields set to the parameters
     *
     * @param question       a Question object containing all information of the question
     * @param text           a string containing the answer to the question
     * @param contractorName a string containing the name of the contractorName
     * @param username       a string containing the username of the person who answers the question
     * @param submissionTime the time a answer was submitted
     */
    public Answer(Question question, String text, String contractorName, String username, Timestamp submissionTime) {
        this.question = question;
        this.text = text;
        this.contractorName = contractorName;
        this.username = username;
        this.submissionTime = submissionTime;
    }

    /**
     * Creates a questionItem with the given fields set to the parameters
     *
     * @param question                      a Question object containing all information of the question
     * @param text                          a string containing the answer to the question
     * @param numberOfPeopleWhoAnsweredThis number of people who answered a particular question
     */
    public Answer(Question question, String text, int numberOfPeopleWhoAnsweredThis) {
        this.question = question;
        this.text = text;
        this.numberOfPeopleWhoAnsweredThis = numberOfPeopleWhoAnsweredThis;
    }

    /**
     * Creates a questionItem with the given fields set to the parameters
     *
     * @param question a Question object containing all information of the question
     */
    public Answer(Question question) {
        this.question = question;
        text = "";
    }

    /**
     * Gets question
     *
     * @return value of question
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Sets the field question to the given parameter
     *
     * @param question the parameter which the field gets assigned to
     */
    public void setQuestion(Question question) {
        this.question = question;
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
     * Gets contractorName
     *
     * @return value of contractorName
     */
    public String getContractorName() {
        return contractorName;
    }

    /**
     * Sets the field contractorName to the given parameter
     *
     * @param contractorName the parameter which the field gets assigned to
     */
    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    /**
     * Gets username
     *
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the field username to the given parameter
     *
     * @param username the parameter which the field gets assigned to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets submissionTime
     *
     * @return value of timestamp
     */
    public Timestamp getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Sets the field submissionTime to the given parameter
     *
     * @param submissionTime the parameter which the field gets assigned to
     */
    public void setSubmissionTime(Timestamp submissionTime) {
        this.submissionTime = submissionTime;
    }

    /**
     * Gets numberOfPeopleWhoAnsweredThis
     *
     * @return value of numberOfPeopleWhoAnsweredThis
     */
    public Integer getNumberOfPeopleWhoAnsweredThis() {
        return numberOfPeopleWhoAnsweredThis;
    }

    /**
     * Sets the field numberOfPeopleWhoAnsweredThis to the given parameter
     *
     * @param numberOfPeopleWhoAnsweredThis the parameter which the field gets assigned to
     */
    public void setNumberOfPeopleWhoAnsweredThis(Integer numberOfPeopleWhoAnsweredThis) {
        this.numberOfPeopleWhoAnsweredThis = numberOfPeopleWhoAnsweredThis;
    }

    /**
     * Gets the scala text which is normally a int as a string representation of the scala value
     *
     * @return a string representation of the scala value
     */
    public String getScalaAnswerAsText() {
        if (question.getType() == QuestionType.SCALA) return Constants.SCALALABELS.get(Integer.parseInt(text));
        else return text;
    }
}
