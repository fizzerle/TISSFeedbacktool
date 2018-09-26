package at.ac.tuwien.tiss.feedbacktool.ModelTests;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.AccessControlDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.ClientDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.ContractorDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Answer;
import at.ac.tuwien.tiss.feedbacktool.entities.Contractor;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.QuestionType;
import at.ac.tuwien.tiss.feedbacktool.model.ContractorModel;
import at.ac.tuwien.tiss.feedbacktool.model.QuestionModel;
import at.ac.tuwien.tiss.feedbacktool.model.QuestionnaireModel;
import at.ac.tuwien.tiss.util.fe.jsf.Configurations;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.faces.application.FacesMessage;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import static org.mockito.Matchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JsfUtil.class,Configurations.class})
public class QuestionnaireModelTests {


    private AnswerDAO answerDAO;
    private AccessControlDAO accessControlDAO;
    private ClientDAO clientDAO;
    private QuestionModel questionModel;
    private UserSettings userSettings;
    private ContractorDAO contractorDAO;
    private ContractorModel contractorModel;
    private QuestionnaireModel questionnaireModel;

    @Before
    public void init(){
        answerDAO = Mockito.mock(AnswerDAO.class);
        accessControlDAO = Mockito.mock(AccessControlDAO.class);
        clientDAO = Mockito.mock(ClientDAO.class);
        questionModel = Mockito.mock(QuestionModel.class);
        userSettings = Mockito.mock(UserSettings.class);
        contractorModel = Mockito.mock(ContractorModel.class);
        contractorDAO = Mockito.mock(ContractorDAO.class);

        Mockito.when(userSettings.getLocale()).thenReturn(Locale.GERMAN);
        Mockito.when(contractorModel.getContractors()).thenReturn(new LinkedList<Contractor>(){{add(new Contractor(1,"Test Contractor"));}});

        questionnaireModel = new QuestionnaireModel(answerDAO,accessControlDAO,clientDAO,contractorDAO,questionModel,userSettings,contractorModel);
    }

    @Test
    public void testGetQuestions(){
        LinkedList<Question> requiredQuestions = new LinkedList<>();
        LinkedList<Question> optionalQuestions = new LinkedList<>();
        LinkedList<Question> freeTextQuestions = new LinkedList<>();
        requiredQuestions.add(new Question(1,"Test Question", QuestionType.SCALA,Locale.GERMAN));
        optionalQuestions.add(new Question(2,"Test Question", QuestionType.TEXT,Locale.GERMAN));
        freeTextQuestions.add(new Question(3,"Test Question", QuestionType.TEXT_LONG,Locale.GERMAN));
        Mockito.when(questionModel.getRequiredQuestions(Locale.GERMAN)).thenReturn(requiredQuestions);
        Mockito.when(questionModel.getOptionalQuestions(Locale.GERMAN)).thenReturn(optionalQuestions);
        Mockito.when(questionModel.getFreeTextQuestions(Locale.GERMAN)).thenReturn(freeTextQuestions);

        questionnaireModel.getQuestions();

        Assert.assertEquals(1,questionnaireModel.getRequiredAnswers().get(0).getQuestion().getId());
        Assert.assertEquals(2,questionnaireModel.getOptionalAnswers().get(0).getQuestion().getId());
        Assert.assertEquals(3,questionnaireModel.getFreeTextAnswers().get(0).getQuestion().getId());
    }

    @Test
    public void testActionSubmit(){
        PowerMockito.mockStatic(JsfUtil.class);
        PowerMockito.when(JsfUtil.getCurrentClassLoader(any())).thenCallRealMethod();
        LinkedList<Answer> optionalAnswers = new LinkedList<>();
        Answer answer4 = new Answer(new Question(4,"question 4"));
        Answer answer5 = new Answer(new Question(5,"question 5"));
        answer4.setText("answer4");
        optionalAnswers.add(answer4);
        optionalAnswers.add(answer5);

        LinkedList<Answer> freeText = new LinkedList<>();
        Answer answer6 = new Answer(new Question(6,"question 6"));
        Answer answer7 = new Answer(new Question(7,"question 7"));
        answer6.setText("answer7");
        freeText.add(answer6);
        freeText.add(answer7);

        questionnaireModel.setOptionalAnswers(optionalAnswers);
        questionnaireModel.setFreeTextAnswers(freeText);

        //not all required Answers are answered
        LinkedList<Answer> requiredAnswers = new LinkedList<>();
        requiredAnswers.add(new Answer(new Question(1,"question 1")));
        requiredAnswers.add(new Answer(new Question(2,"question 2")));
        requiredAnswers.add(new Answer(new Question(3,"question 3")));
        questionnaireModel.setRequiredAnswers(requiredAnswers);

        Assert.assertEquals(null,questionnaireModel.actionSubmit());

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),Matchers.matches("allQuestionsRequired"), Matchers.anyVararg());

        //all requiered Answers are answered
        requiredAnswers = new LinkedList<>();
        Answer answer1 = new Answer(new Question(1,"question 1"));
        Answer answer2 = new Answer(new Question(2,"question 2"));
        Answer answer3 = new Answer(new Question(3,"question 3"));
        answer1.setText("answer1");
        answer2.setText("answer2");
        answer3.setText("answer3");
        requiredAnswers.add(answer1);
        requiredAnswers.add(answer2);
        requiredAnswers.add(answer3);
        questionnaireModel.setRequiredAnswers(requiredAnswers);

        Assert.assertEquals("success",questionnaireModel.actionSubmit());

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());

        Mockito.verify(answerDAO,Mockito.times(5)).createAnswer(Matchers.anyInt(),Matchers.anyString(),Matchers.anyInt(),Matchers.anyString(),Matchers.any(Timestamp.class));

        //all required Answers are answered but client wants to submit multiple times with no enough time between
        Mockito.when(accessControlDAO.getAccessControlEntry(Matchers.anyString(),Matchers.anyInt())).thenReturn(new Timestamp(new Date().getTime()));
        Assert.assertEquals(null,questionnaireModel.actionSubmit());

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),Matchers.matches("userBlocked"), Matchers.anyVararg());

        //all required Answers are answered but client wants to submit multiple times with enough time between
        Mockito.when(accessControlDAO.getAccessControlEntry(Matchers.anyString(),Matchers.anyInt())).thenReturn(new Timestamp(new Date().getTime()+3*1000*3600*168));
        Assert.assertEquals("success",questionnaireModel.actionSubmit());

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),Matchers.matches("userBlocked"), Matchers.anyVararg());
    }

    @Test
    public void testGenerateRandomFeedback(){
        LinkedList<Answer> optionalAnswers = new LinkedList<>();
        Answer answer4 = new Answer(new Question(4,"question 4"));
        Answer answer5 = new Answer(new Question(5,"question 5"));
        optionalAnswers.add(answer4);
        optionalAnswers.add(answer5);

        LinkedList<Answer> freeText = new LinkedList<>();
        Answer answer6 = new Answer(new Question(6,"question 6"));
        Answer answer7 = new Answer(new Question(7,"question 7"));
        freeText.add(answer6);
        freeText.add(answer7);

        questionnaireModel.setOptionalAnswers(optionalAnswers);
        questionnaireModel.setFreeTextAnswers(freeText);

        //not all required Answers are answered
        LinkedList<Answer> requiredAnswers = new LinkedList<>();
        requiredAnswers.add(new Answer(new Question(1,"question 1")));
        requiredAnswers.add(new Answer(new Question(2,"question 2")));
        requiredAnswers.add(new Answer(new Question(3,"question 3")));
        questionnaireModel.setRequiredAnswers(requiredAnswers);
        questionnaireModel.generateRandomFeedback(5);
        Mockito.verify(answerDAO,Mockito.times(5)).createAnswer(Matchers.eq(1),Matchers.anyString(),Matchers.anyInt(),Matchers.anyString(),Matchers.any(Timestamp.class));
    }
}
