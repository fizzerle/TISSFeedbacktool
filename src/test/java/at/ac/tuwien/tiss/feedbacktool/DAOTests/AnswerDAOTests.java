package at.ac.tuwien.tiss.feedbacktool.DAOTests;

import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class AnswerDAOTests extends JpaBaseRolledBackTestCase{

    private AnswerDAO answerDAO;

    @Before
    public void init(){
        answerDAO = new AnswerDAO(em);
    }

    @Test
    public void testCreateAnswer() {
        answerDAO.createAnswer(1, "answer", 1, "testuser1", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1, Locale.GERMAN).size() == 1);
    }

    @Test
    public void testGetAnswersForContractorOrderedByUsername(){
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1,Locale.GERMAN).size() == 0);
        answerDAO.createAnswer(1, "answer1", 1, "testuser1", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1, Locale.GERMAN).size() == 1);
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1,Locale.GERMAN).get(0).getText() == "answer1");
        //test order by username
        answerDAO.createAnswer(1, "answer2", 1, "testuser2", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1,Locale.GERMAN).get(0).getText() == "answer1");
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1,Locale.GERMAN).get(1).getText() == "answer2");

        //test order of Timestamp
        answerDAO.createAnswer(1, "answer3", 1, "testuser1", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersForContractorOrderedByUsername(1,Locale.GERMAN).get(1).getText() == "answer3");
    }

    @Test
    public void getAnswersAddedUpPerContractor(){
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1,Locale.GERMAN).isEmpty());
        answerDAO.createAnswer(1, "0", 1, "testuser1", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).size() == 1);
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).get(0).getNumberOfPeopleWhoAnsweredThis() == 1);
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).get(0).getQuestion().getId() == 1);
        //test order by username
        answerDAO.createAnswer(1, "0", 1, "testuser2", FeedbacktoolUtil.getCurrentTimeStamp());
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).size() == 1);
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).get(0).getNumberOfPeopleWhoAnsweredThis() == 2);
        answerDAO.createAnswer(1, "1", 1, "testuser2", FeedbacktoolUtil.getCurrentTimeStamp());

        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).size() == 2);
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).get(0).getNumberOfPeopleWhoAnsweredThis() == 2);
        Assert.assertTrue(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN).get(1).getQuestion().getId() == 1);
    }
}
