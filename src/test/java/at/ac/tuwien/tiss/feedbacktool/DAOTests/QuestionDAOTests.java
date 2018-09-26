package at.ac.tuwien.tiss.feedbacktool.DAOTests;

import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class QuestionDAOTests extends JpaBaseRolledBackTestCase{
    private QuestionDAO questionDAO;

    @Before
    public void init(){
        questionDAO = new QuestionDAO(em);
    }

    @Test
    public void testCreateQuestionTranslation(){
        Assert.assertEquals(1,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
        Assert.assertEquals(2,questionDAO.getQuestionsInAllLanguages().size());
        questionDAO.createQuestionTranslation(2,"Beipielfrage2", Locale.GERMAN);
        Assert.assertEquals(3,questionDAO.getQuestionsInAllLanguages().size());
        Assert.assertEquals(1,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
        questionDAO.createQuestionTranslation(2,"exampleQuestion2", Locale.ENGLISH);
        Assert.assertEquals(4,questionDAO.getQuestionsInAllLanguages().size());
        Assert.assertEquals(2,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
    }

    @Test
    public void testGetAllScalaQuestions(){
        Assert.assertEquals(1,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
        questionDAO.createQuestionTranslation(2,"Beipielfrage2", Locale.GERMAN);
        Assert.assertEquals(1,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
        questionDAO.createQuestionTranslation(2,"exampleQuestion2", Locale.ENGLISH);
        Assert.assertEquals(2,questionDAO.getAllScalaQuestions(Locale.ENGLISH).size());
    }

    @Test
    public void testGetQuestionsInAllLanguages(){
        Assert.assertEquals(2,questionDAO.getQuestionsInAllLanguages().size());
        questionDAO.createQuestionTranslation(2,"Beipielfrage2", Locale.GERMAN);
        Assert.assertEquals(3,questionDAO.getQuestionsInAllLanguages().size());
        questionDAO.createQuestionTranslation(2,"exampleQuestion2", Locale.ENGLISH);
        Assert.assertEquals(4,questionDAO.getQuestionsInAllLanguages().size());
        questionDAO.resetLatestQuestion();
        Assert.assertEquals(0,questionDAO.getQuestionsInAllLanguages().size());
    }

    @Test
    public void testResetLatestQuestion(){
        Assert.assertEquals(2,questionDAO.getQuestionsInAllLanguages().size());
        questionDAO.resetLatestQuestion();
        Assert.assertEquals(0,questionDAO.getQuestionsInAllLanguages().size());
    }
}
