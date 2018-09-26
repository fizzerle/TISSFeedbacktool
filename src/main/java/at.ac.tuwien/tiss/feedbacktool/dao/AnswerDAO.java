package at.ac.tuwien.tiss.feedbacktool.dao;

import at.ac.tuwien.tiss.feedbacktool.entities.Answer;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.QuestionType;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Data access object for answers
 */
@Transactional
@ApplicationScoped
public class AnswerDAO implements Serializable {

    private Query createAnswerQuery;
    private Query selectAllAnswersForContractor;
    private Query answersWithCount;
    private EntityManager em;

    /**
     * constructs a new AnswerDAO with nothing set
     */
    public AnswerDAO() {
    }

    /**
     * Constructs a new AnswerDAO with the entityManager set
     *
     * @param em the entityManager which is used to access the database
     */
    @Inject
    public AnswerDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Creates a database entry for the answer
     *
     * @param questionid     the id of the question the answer is for
     * @param answer         the answer to the question
     * @param contractorid   the id of the contractor the answer is for
     * @param username       the username of the person who answered the question
     * @param submissionTime time when the user submitted the answer
     */
    public void createAnswer(int questionid, String answer, int contractorid, String username, Timestamp submissionTime) {
        createAnswerQuery = em.createNativeQuery("INSERT INTO answers(questionid, text,contractorid,username,submissiontime) VALUES (:questionid, :answer,:contractorid,:username,:submissionTime) ");
        createAnswerQuery.setParameter("answer", answer);
        createAnswerQuery.setParameter("questionid", questionid);
        createAnswerQuery.setParameter("username", username);
        createAnswerQuery.setParameter("submissionTime", submissionTime);
        createAnswerQuery.setParameter("contractorid", contractorid);
        createAnswerQuery.executeUpdate();
    }

    /**
     * Returns the answers for an contractor ordered by the username of the client and the submission time
     *
     * @param id     the id of the contractor
     * @param locale the locale of the question
     * @return a list a QuestionItems for the requested contractor ordered by the username of the client
     */
    public List<Answer> getAnswersForContractorOrderedByUsername(int id, Locale locale) {
        selectAllAnswersForContractor = em.createNativeQuery("SELECT a.questionid,qt.question,a.text,q.type,locale,c.name,a.username,submissiontime FROM answers a LEFT JOIN contractors c ON a.contractorID=c.ID LEFT JOIN questions q ON q.id = a.questionid INNER JOIN questions_tr qt ON qt.questionid = q.id WHERE c.id = :id AND locale = :locale ORDER BY a.username,a.submissiontime, a.questionid");
        selectAllAnswersForContractor.setParameter("id", id);
        selectAllAnswersForContractor.setParameter("locale", locale.toString());
        List<Object[]> resultList = selectAllAnswersForContractor.getResultList();
        List<Answer> questions = new ArrayList<>();
        for (Object[] answer : resultList) {
            questions.add(new Answer(new Question((Integer) answer[0], (String) answer[1], QuestionType.valueOf((String) answer[3]), new Locale((String) answer[4])), (String) answer[2], (String) answer[5], (String) answer[6], (Timestamp) answer[7]));
        }
        return questions;
    }

    /**
     * Counts all answers of the scala questions separately for a given contractor
     *
     * @param id     the id of the contractor
     * @param locale the locale of the question
     * @return a list of QuestionItems with the count variable set, the result is ordered by answer and the question id
     */
    public List<Answer> getAnswersAddedUpPerContractor(int id, Locale locale) {
        answersWithCount = em.createNativeQuery("SELECT a.questionid,qt.question,a.text,count(contractorID),type FROM answers a INNER JOIN questions q ON a.questionid = q.id INNER JOIN contractors c ON a.contractorID=c.ID INNER JOIN questions_tr qt ON qt.questionid = q.id WHERE c.id = :id AND q.type = 'SCALA' AND locale = :locale GROUP BY question,a.questionid,text,type ORDER BY type,text,a.questionid");
        answersWithCount.setParameter("id", id);
        answersWithCount.setParameter("locale", locale.toString());
        List<Object[]> answers = answersWithCount.getResultList();
        List<Answer> answersWithCount = new LinkedList<>();
        for (Object[] answer : answers) {
            answersWithCount.add(new Answer(new Question((Integer) answer[0], (String) answer[1]), (String) answer[2], ((BigInteger) answer[3]).intValue()));
        }
        return answersWithCount;
    }
}

