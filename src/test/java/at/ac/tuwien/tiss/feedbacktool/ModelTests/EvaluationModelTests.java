package at.ac.tuwien.tiss.feedbacktool.ModelTests;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.*;
import at.ac.tuwien.tiss.feedbacktool.model.ContractorModel;
import at.ac.tuwien.tiss.feedbacktool.model.EvaluationModel;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.primefaces.model.chart.ChartSeries;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class EvaluationModelTests {

    private AnswerDAO answerDAO;
    private QuestionDAO questionDAO;
    private ContractorModel contractorModel;
    private UserSettings userSettings;
    private EvaluationModel evaluationModel;

    @Before
    public void init(){
        contractorModel = Mockito.mock(ContractorModel.class);
        answerDAO = Mockito.mock(AnswerDAO.class);
        questionDAO = Mockito.mock(QuestionDAO.class);
        userSettings = Mockito.mock(UserSettings.class);
        Mockito.when(contractorModel.getContractors()).thenReturn(new LinkedList<Contractor>(){{add(new Contractor(1,"Test Contractor"));}});
        Mockito.when(userSettings.getLocale()).thenReturn(Locale.GERMAN);
        evaluationModel = new EvaluationModel(answerDAO,questionDAO,contractorModel,userSettings);
    }

    @Test
    public void testActionSubmit(){
        contractorModel = Mockito.mock(ContractorModel.class);
        answerDAO = Mockito.mock(AnswerDAO.class);
        questionDAO = Mockito.mock(QuestionDAO.class);
        userSettings = Mockito.mock(UserSettings.class);
        Mockito.when(answerDAO.getAnswersForContractorOrderedByUsername(1, Locale.GERMAN)).thenReturn(new LinkedList<Answer>());
        Mockito.when(answerDAO.getAnswersAddedUpPerContractor(1, Locale.GERMAN)).thenReturn(new LinkedList<Answer>());
        Mockito.when(questionDAO.getAllScalaQuestions(Locale.GERMAN)).thenReturn(new LinkedList<Question>());
        evaluationModel.actionSubmit();
        Assert.assertNotEquals(null,evaluationModel.getHorizontalBarModel());
        Assert.assertNotEquals(null,evaluationModel.getSummaryQuestionItems());
        Assert.assertNotEquals(null,evaluationModel.getClientsWhoAnsweredQuestions());
    }

    @Test
    public void testCreateSummary(){
        Mockito.when(questionDAO.getAllScalaQuestions(Locale.GERMAN)).thenReturn(new LinkedList<Question>(){{add(new Question(1, "question", QuestionType.SCALA,Locale.GERMAN));}});
        List<Answer> answersAddedUpPerContractor = new LinkedList<>();
        answersAddedUpPerContractor.add(new Answer(new Question(1,"question1"), "0", 1));
        answersAddedUpPerContractor.add(new Answer(new Question(1,"question1"), "1", 3));
        answersAddedUpPerContractor.add(new Answer(new Question(1,"question1"), "2", 4));
        answersAddedUpPerContractor.add(new Answer(new Question(2,"question2"), "1", 4));

        List<Question> allQuestions = new LinkedList<>();
        allQuestions.add(new Question(1, "question1", QuestionType.SCALA,Locale.GERMAN));
        allQuestions.add(new Question(2, "question2", QuestionType.SCALA,Locale.GERMAN));
        evaluationModel.createSummary(answersAddedUpPerContractor,allQuestions);
        Assert.assertEquals(2,evaluationModel.getSummaryQuestionItems().size());

        Assert.assertEquals(1,evaluationModel.getSummaryQuestionItems().get(0).getCounts()[0]);
        Assert.assertEquals(3,evaluationModel.getSummaryQuestionItems().get(0).getCounts()[1]);
        Assert.assertEquals(4,evaluationModel.getSummaryQuestionItems().get(0).getCounts()[2]);
        Assert.assertEquals(4,evaluationModel.getSummaryQuestionItems().get(1).getCounts()[1]);
    }

    @Test
    public void testGenerateSingleFeedback(){
        Assert.assertEquals(null,evaluationModel.getClientsWhoAnsweredQuestions());
        List<Answer> allAnswers = new LinkedList<>();
        Timestamp currentTimeStamp = FeedbacktoolUtil.getCurrentTimeStamp();
        allAnswers.add(new Answer(new Question(1,"question1",QuestionType.SCALA,Locale.GERMAN),"answer1","contractor1","username1", new Timestamp(currentTimeStamp.getTime()-3000)));
        allAnswers.add(new Answer(new Question(2,"question2",QuestionType.SCALA,Locale.GERMAN),"answer2","contractor1","username1", currentTimeStamp));
        allAnswers.add(new Answer(new Question(3,"question3",QuestionType.TEXT,Locale.GERMAN),"answer3","contractor1","username2", currentTimeStamp));
        allAnswers.add(new Answer(new Question(4,"question4",QuestionType.TEXT_LONG,Locale.GERMAN),"answer4","contractor1","username2", currentTimeStamp));
        allAnswers.add(new Answer(new Question(2,"question2",QuestionType.SCALA,Locale.GERMAN),"answer5","contractor1","username2", currentTimeStamp));
        evaluationModel.generateSingleFeedback(allAnswers);
        Assert.assertEquals(3 ,evaluationModel.getClientsWhoAnsweredQuestions().size());

        Assert.assertEquals("username1",evaluationModel.getClientsWhoAnsweredQuestions().get(0).getUsername());
        Assert.assertEquals("username1",evaluationModel.getClientsWhoAnsweredQuestions().get(1).getUsername());
        Assert.assertEquals("username2",evaluationModel.getClientsWhoAnsweredQuestions().get(2).getUsername());
        Assert.assertEquals(3,evaluationModel.getClientsWhoAnsweredQuestions().get(2).getOpenAnswers().get(0).getQuestion().getId());
        Assert.assertEquals(4,evaluationModel.getClientsWhoAnsweredQuestions().get(2).getFreeTextAnswers().get(0).getQuestion().getId());
        Assert.assertEquals(2,evaluationModel.getClientsWhoAnsweredQuestions().get(2).getScalaAnswers().get(0).getQuestion().getId());
    }

    @Test
    public void testCreateHorizontalBarModel(){
        List<SummaryQuestionItem> summaryQuestionItems = new LinkedList<>();
        summaryQuestionItems.add(new SummaryQuestionItem(1,"question1",new int[]{1,2,3,4,5}));
        summaryQuestionItems.add(new SummaryQuestionItem(2,"question2",new int[]{0,0,0,0,0}));
        summaryQuestionItems.add(new SummaryQuestionItem(3,"question3",new int[]{1,1,1,1,1}));
        evaluationModel.createHorizontalBarModel(summaryQuestionItems);
        Map<Object, Number> firtChartData = evaluationModel.getHorizontalBarModel().getSeries().get(0).getData();
        Assert.assertEquals(1,firtChartData.get("Nr. 1").doubleValue(),0.1);
        Assert.assertEquals(0,firtChartData.get("Nr. 2").doubleValue(),0.1);
        Assert.assertEquals(1,firtChartData.get("Nr. 3").doubleValue(),0.1);

        Map<Object, Number> secondChartData = evaluationModel.getHorizontalBarModel().getSeries().get(1).getData();
        Assert.assertEquals(2,secondChartData.get("Nr. 1").doubleValue(),0.1);
        Assert.assertEquals(0,secondChartData.get("Nr. 2").doubleValue(),0.1);
        Assert.assertEquals(1,secondChartData.get("Nr. 3").doubleValue(),0.1);

        Map<Object, Number> lastChartData = evaluationModel.getHorizontalBarModel().getSeries().get(4).getData();
        Assert.assertEquals(5,lastChartData.get("Nr. 1").doubleValue(),0.1);
        Assert.assertEquals(0,lastChartData.get("Nr. 2").doubleValue(),0.1);
        Assert.assertEquals(1,lastChartData.get("Nr. 3").doubleValue(),0.1);
    }

}
