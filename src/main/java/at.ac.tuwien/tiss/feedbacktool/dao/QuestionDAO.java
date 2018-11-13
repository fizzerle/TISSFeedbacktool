package at.ac.tuwien.tiss.feedbacktool.dao;

import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.QuestionType;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Data access object for questions
 */
@Transactional
@ApplicationScoped
public class QuestionDAO implements Serializable {

    private Query createQuestion;
    private Query getAllScalaQuestions;
    private Query getAllQuestions;
    private Query resetLatestQuestion;
    private Query createQuestionTranslation;
    private EntityManager em;

    /**
     * constructs a new QuestionDAO with nothing set
     */
    public QuestionDAO() {
    }

    /**
     * Constructs a new QuestionDAO with the entityManager set
     *
     * @param em the entityManager which is used to access the database
     */
    @Inject
    public QuestionDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Resets the latestQuestion flag in the database
     */
    public void resetLatestQuestion() {
        resetLatestQuestion = em.createNativeQuery("UPDATE questions SET latestquestion = FALSE WHERE latestquestion = TRUE");
        resetLatestQuestion.executeUpdate();
    }

    /**
     * Creates a database entry for a question
     *
     * @param questionType the question type for the question
     * @return the generated id of the question
     */
    public int createQuestion(QuestionType questionType) {
        createQuestion = em.createNativeQuery("INSERT INTO questions(type, latestquestion) VALUES (:questionType, TRUE) RETURNING id");
        createQuestion.setParameter("questionType", questionType.toString());
        int firstResult = (Integer) createQuestion.getSingleResult();
        return firstResult;
    }

    /**
     * creates a translation entry for a question
     *
     * @param questionid the id of the question to translate
     * @param question   the translation
     * @param locale     the locale of the translation
     */
    public void createQuestionTranslation(int questionid, String question, Locale locale) {
        createQuestionTranslation = em.createNativeQuery("INSERT INTO questions_tr(questionid, question, locale) VALUES (:questionid,:question,:locale)");
        createQuestionTranslation.setParameter("questionid", questionid);
        createQuestionTranslation.setParameter("question", question);
        createQuestionTranslation.setParameter("locale", locale.toString());
        createQuestionTranslation.executeUpdate();
    }

    /**
     * Gets all scala Questions from the database
     *
     * @param locale the locale of the question
     * @return a list of Question which contains all question where the type is QuestionType.SCALA
     */
    public List<Question> getAllScalaQuestions(Locale locale) {
        getAllScalaQuestions = em.createNativeQuery("SELECT id,question,type,locale FROM questions q INNER JOIN questions_tr qt ON qt.questionid = q.id WHERE type = '" + QuestionType.SCALA + "' AND locale = :locale");
        getAllScalaQuestions.setParameter("locale", locale.toString());
        List<Object[]> resultList = getAllScalaQuestions.getResultList();
        List<Question> questions = new ArrayList();
        for (Object[] question : resultList) {
            questions.add(new Question((Integer) question[0], (String) question[1], QuestionType.valueOf((String) question[2]), new Locale((String) question[3])));
        }
        return questions;
    }

    /**
     * Gets all questions in all languages that are the latest from the database
     *
     * @return a list of QuestionItems which contains all questions
     */
    public List<Question> getQuestionsInAllLanguages() {
        getAllQuestions = em.createNativeQuery("SELECT id,question,type,locale FROM questions q INNER JOIN questions_tr qt ON qt.questionid = q.id WHERE latestquestion = TRUE");
        List<Object[]> resultList = getAllQuestions.getResultList();
        List<Question> questions = new ArrayList();
        for (Object[] question : resultList) {
            questions.add(new Question((Integer) question[0], (String) question[1], QuestionType.valueOf((String) question[2]), new Locale((String) question[3])));
        }
        return questions;
    }

    /**
     * Deletes all questions
     */
    public void deleteAllQuestions() {
        Query deleteQuestionsTRAll = em.createNativeQuery("DELETE FROM questions_tr");
        Query deleteAll = em.createNativeQuery("DELETE FROM questions");
        Query resetQuestionNumber = em.createNativeQuery("ALTER SEQUENCE questions_id_seq RESTART WITH 1;");
        deleteQuestionsTRAll.executeUpdate();
        resetQuestionNumber.executeUpdate();
        deleteAll.executeUpdate();
    }

}
