package at.ac.tuwien.tiss.feedbacktool.ModelTests;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.QuestionType;
import at.ac.tuwien.tiss.feedbacktool.model.QuestionModel;
import at.ac.tuwien.tiss.util.fe.jsf.Configurations;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FeedbacktoolUtil.class,JsfUtil.class,Configurations.class})
public class QuestionModelTests {
    private QuestionDAO questionDAO;
    private UserSettings userSettings;
    private QuestionModel questionModel;

    @Before
    public void init() throws IllegalAccessException{
        PowerMockito.mockStatic(FeedbacktoolUtil.class);
        PowerMockito.when(FeedbacktoolUtil.getSupportedLocales()).thenReturn(new LinkedList<Locale>(){{add(Locale.GERMAN);add(Locale.ENGLISH);}});
        questionDAO = Mockito.mock(QuestionDAO.class);
        userSettings = Mockito.mock(UserSettings.class);
        Mockito.when(userSettings.getLocale()).thenReturn(Locale.GERMAN);
        questionModel = new QuestionModel(questionDAO,userSettings);
    }

    @Test
    public void testInitializeQuestions(){
        questionModel.initializeQuestions();
        Assert.assertNotNull(questionModel.getFreeTextQuestions(Locale.GERMAN));
        Assert.assertNotNull(questionModel.getOptionalQuestions(Locale.GERMAN));
        Assert.assertNotNull(questionModel.getRequiredQuestions(Locale.GERMAN));
        Assert.assertNotNull(questionModel.getFreeTextQuestions(Locale.ENGLISH));
        Assert.assertNotNull(questionModel.getOptionalQuestions(Locale.ENGLISH));
        Assert.assertNotNull(questionModel.getRequiredQuestions(Locale.ENGLISH));
    }

    @Test
    public void testLoadQuestionsFromJsonNotEveryQuestionsHasTranslation() throws Exception{
        PowerMockito.mockStatic(JsfUtil.class);
        PowerMockito.when(JsfUtil.getCurrentClassLoader(any())).thenCallRealMethod();

        PowerMockito.when(FeedbacktoolUtil.readFromInputStream(any(InputStream.class))).thenReturn("" +
                "[\n" +
                "  {\n" +
                "    \"language\": \"de\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"Ich war mit der Servicegeschwindigkeit zufrieden\",\n" +
                "      \"Ich war mit der Freundlichkeit des Dienstleisters zufrieden\",\n" +
                "      \"Ich war mit der Sachkompetenz des Dienstleisters zufrieden\",\n" +
                "      \"Die Anfrage war Komplex\",\n" +
                "      \"Der Dienstleister war nützlich für das Anliegen\"\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Referenz zum Geschäftsfall\",\n" +
                "      \"Name\",\n" +
                "      \"Kontaktadresse\",\n" +
                "      \"Funktion\"\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Freitext\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"language\": \"en\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"I was satisfied with the speed of service\",\n" +
                "      \"I was satisfied with the friendliness of the contractor\",\n" +
                "      \"I was satisfied with the professional competence of the contractor\",\n" +
                "      \"The contractor was helpful for the concern\"\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Reference to the business case\",\n" +
                "      \"Name\",\n" +
                "      \"Contact address\",\n" +
                "      \"Function\"\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Free text\"\n" +
                "    ]\n" +
                "  }\n" +
                "]\n");

        questionModel.loadQuestionsFromJson(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());

        PowerMockito.verifyStatic(Mockito.times(0));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_INFO),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());
    }

    @Test
    public void testLoadQuestionsFromJsonWrongJsonSyntax() throws Exception{
        PowerMockito.mockStatic(JsfUtil.class);
        PowerMockito.when(JsfUtil.getCurrentClassLoader(any())).thenCallRealMethod();
        PowerMockito.when(FeedbacktoolUtil.readFromInputStream(any(InputStream.class))).thenReturn("[\n" +
                "  \n" +
                "    \"language\": \"de\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"Ich war mit der Servicegeschwindigkeit zufrieden\",\n" +
                "      \"Ich war mit der Freundlichkeit des Dienstleisters zufrieden\",\n" +
                "      \"Ich war mit der Sachkompetenz des Dienstleisters zufrieden\",\n" +
                "      \"Die Anfrage war Komplex\",\n" +
                "      \"Der Dienstleister war nützlich für das Anliegen\"\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Referenz zum Geschäftsfall\",\n" +
                "      \"Name\",\n" +
                "      \"Kontaktadresse\",\n" +
                "      \"Funktion\"\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Freitext\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"language\": \"en\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"I was satisfied with the speed of service\",\n" +
                "      \"I was satisfied with the friendliness of the contractor\",\n" +
                "      \"I was satisfied with the professional competence of the contractor\",\n" +
                "      \"The enquiry was complex\",\n" +
                "      \"The contractor was helpful for the concern\"\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Reference to the business case\",\n" +
                "      \"Name\",\n" +
                "      \"Contact address\",\n" +
                "      \"Function\"\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Free text\"\n" +
                "    ]\n" +
                "  }\n" +
                "]\n");

        questionModel.loadQuestionsFromJson(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());

        PowerMockito.verifyStatic(Mockito.times(0));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_INFO),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());
    }

    @Test
    public void testLoadQuestionsFromJson() throws Exception{
        PowerMockito.when(FeedbacktoolUtil.readQuestionsFromFile(any(String.class))).thenCallRealMethod();
        PowerMockito.mockStatic(JsfUtil.class);
        PowerMockito.when(JsfUtil.getCurrentClassLoader(any())).thenCallRealMethod();
        PowerMockito.when(FeedbacktoolUtil.readFromInputStream(any(InputStream.class))).thenReturn("[\n" +
                "  {\n" +
                "    \"language\": \"de\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"Ich war mit der Servicegeschwindigkeit zufrieden\",\n" +
                "      \"Ich war mit der Freundlichkeit des Dienstleisters zufrieden\",\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Referenz zum Geschäftsfall\",\n" +
                "      \"Name\",\n" +
                "      \"Kontaktadresse\",\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Freitext\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"language\": \"en\",\n" +
                "    \"Scala Questions\": [\n" +
                "      \"I was satisfied with the speed of service\",\n" +
                "      \"I was satisfied with the friendliness of the contractor\",\n" +
                "    ],\n" +
                "    \"Additional Information\": [\n" +
                "      \"Reference to the business case\",\n" +
                "      \"Name\",\n" +
                "      \"Contact address\",\n" +
                "    ],\n" +
                "    \"Free text Questions\": [\n" +
                "      \"Free text\"\n" +
                "    ]\n" +
                "  }\n" +
                "]\n");

        questionModel.loadQuestionsFromJson(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        PowerMockito.verifyStatic(Mockito.times(0));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_ERROR),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());

        PowerMockito.verifyStatic(Mockito.times(1));
        JsfUtil.addStatusMessage(eq(FacesMessage.SEVERITY_INFO),anyString(),any(Locale.class),anyString(),Matchers.anyVararg());

        Assert.assertEquals(2, questionModel.getRequiredQuestions(Locale.GERMAN).size());
        Assert.assertEquals("Ich war mit der Servicegeschwindigkeit zufrieden", questionModel.getRequiredQuestions(Locale.GERMAN).get(0).getText());
        Assert.assertEquals("Ich war mit der Freundlichkeit des Dienstleisters zufrieden", questionModel.getRequiredQuestions(Locale.GERMAN).get(1).getText());
        Assert.assertEquals(2, questionModel.getRequiredQuestions(Locale.ENGLISH).size());
        Assert.assertEquals("I was satisfied with the speed of service", questionModel.getRequiredQuestions(Locale.ENGLISH).get(0).getText());
        Assert.assertEquals("I was satisfied with the friendliness of the contractor", questionModel.getRequiredQuestions(Locale.ENGLISH).get(1).getText());

        Assert.assertEquals(3, questionModel.getOptionalQuestions(Locale.GERMAN).size());
        Assert.assertEquals("Referenz zum Geschäftsfall", questionModel.getOptionalQuestions(Locale.GERMAN).get(0).getText());
        Assert.assertEquals("Name", questionModel.getOptionalQuestions(Locale.GERMAN).get(1).getText());
        Assert.assertEquals("Kontaktadresse", questionModel.getOptionalQuestions(Locale.GERMAN).get(2).getText());
        Assert.assertEquals(3, questionModel.getOptionalQuestions(Locale.ENGLISH).size());
        Assert.assertEquals("Reference to the business case", questionModel.getOptionalQuestions(Locale.ENGLISH).get(0).getText());
        Assert.assertEquals("Name", questionModel.getOptionalQuestions(Locale.ENGLISH).get(1).getText());
        Assert.assertEquals("Contact address", questionModel.getOptionalQuestions(Locale.ENGLISH).get(2).getText());

        Assert.assertEquals(1, questionModel.getFreeTextQuestions(Locale.GERMAN).size());
        Assert.assertEquals("Freitext", questionModel.getFreeTextQuestions(Locale.GERMAN).get(0).getText());
        Assert.assertEquals(1, questionModel.getFreeTextQuestions(Locale.ENGLISH).size());
        Assert.assertEquals("Free text", questionModel.getFreeTextQuestions(Locale.ENGLISH).get(0).getText());
    }

    @Test
    public void testLoadAllQuestionsFromDB(){
        List<Question> questions = new LinkedList<>();
        Mockito.when(questionDAO.getQuestionsInAllLanguages()).thenReturn(questions);
        questions.add(new Question(1,"question1DE",QuestionType.SCALA,Locale.GERMAN));
        questions.add(new Question(1,"question1EN",QuestionType.SCALA,Locale.ENGLISH));
        questions.add(new Question(2,"question2DE",QuestionType.TEXT,Locale.GERMAN));
        questions.add(new Question(2,"question2EN",QuestionType.TEXT,Locale.ENGLISH));
        questions.add(new Question(3,"question3DE",QuestionType.TEXT_LONG,Locale.GERMAN));
        questions.add(new Question(3,"question3EN",QuestionType.TEXT_LONG,Locale.ENGLISH));
        questions.add(new Question(4,"question4DE",QuestionType.TEXT_LONG,Locale.GERMAN));
        questions.add(new Question(4,"question4EN",QuestionType.TEXT_LONG,Locale.ENGLISH));
        questionModel.loadAllQuestionsFromDB();
        Assert.assertEquals(1, questionModel.getRequiredQuestions(Locale.GERMAN).size());
        Assert.assertEquals(1, questionModel.getRequiredQuestions(Locale.GERMAN).get(0).getId());

        Assert.assertEquals(1, questionModel.getRequiredQuestions(Locale.ENGLISH).size());
        Assert.assertEquals(1, questionModel.getRequiredQuestions(Locale.ENGLISH).get(0).getId());

        Assert.assertEquals(1, questionModel.getOptionalQuestions(Locale.GERMAN).size());
        Assert.assertEquals(2, questionModel.getOptionalQuestions(Locale.GERMAN).get(0).getId());

        Assert.assertEquals(1, questionModel.getOptionalQuestions(Locale.ENGLISH).size());
        Assert.assertEquals(2, questionModel.getOptionalQuestions(Locale.ENGLISH).get(0).getId());

        Assert.assertEquals(2, questionModel.getFreeTextQuestions(Locale.GERMAN).size());
        Assert.assertEquals(3, questionModel.getFreeTextQuestions(Locale.GERMAN).get(0).getId());
        Assert.assertEquals(4, questionModel.getFreeTextQuestions(Locale.GERMAN).get(1).getId());

        Assert.assertEquals(2, questionModel.getFreeTextQuestions(Locale.ENGLISH).size());
        Assert.assertEquals(3, questionModel.getFreeTextQuestions(Locale.ENGLISH).get(0).getId());
        Assert.assertEquals(4, questionModel.getFreeTextQuestions(Locale.ENGLISH).get(1).getId());
    }


}
