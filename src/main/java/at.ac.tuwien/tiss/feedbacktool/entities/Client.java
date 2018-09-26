package at.ac.tuwien.tiss.feedbacktool.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * The class provides default getters and setters and addQuestions to add a Question to the suitable list
 */
public class Client {
    private List<Answer> scalaAnswers = new LinkedList<>();
    private List<Answer> openAnswers = new LinkedList<>();
    private List<Answer> freeTextAnswers = new LinkedList<>();
    private String username;

    /**
     * Adds the question to the list which is suitable for the question type
     *
     * @param answer the Answer which gets added to the list
     */
    public void addAnswer(Answer answer) {
        switch (answer.getQuestion().getType()) {
            case TEXT:
                openAnswers.add(answer);
                break;
            case SCALA:
                scalaAnswers.add(answer);
                break;
            case TEXT_LONG:
                freeTextAnswers.add(answer);
                break;
        }
    }

    /**
     * Gets scalaAnswers
     *
     * @return value of scalaAnswers
     */
    public List<Answer> getScalaAnswers() {
        return scalaAnswers;
    }

    /**
     * Sets the field scalaAnswers to the given parameter
     *
     * @param scalaAnswers) the parameter which the field gets assigned to
     */
    public void setScalaAnswers(List<Answer> scalaAnswers) {
        this.scalaAnswers = scalaAnswers;
    }

    /**
     * Gets openAnswers
     *
     * @return value of openAnswers
     */
    public List<Answer> getOpenAnswers() {
        return openAnswers;
    }

    /**
     * Sets the field openAnswers to the given parameter
     *
     * @param openAnswers) the parameter which the field gets assigned to
     */
    public void setOpenAnswers(List<Answer> openAnswers) {
        this.openAnswers = openAnswers;
    }

    /**
     * Gets freeTextAnswers
     *
     * @return value of freeTextAnswers
     */
    public List<Answer> getFreeTextAnswers() {
        return freeTextAnswers;
    }

    /**
     * Sets the field freeTextAnswers to the given parameter
     *
     * @param freeTextAnswers) the parameter which the field gets assigned to
     */
    public void setFreeTextAnswers(List<Answer> freeTextAnswers) {
        this.freeTextAnswers = freeTextAnswers;
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
     * @param username) the parameter which the field gets assigned to
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
