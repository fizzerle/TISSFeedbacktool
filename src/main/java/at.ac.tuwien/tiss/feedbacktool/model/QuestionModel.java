package at.ac.tuwien.tiss.feedbacktool.model;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.QuestionType;
import at.ac.tuwien.tiss.util.fe.jsf.Constants;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * The class provides the model to load new questions from the json file. Addionally its there that quesions
 * get fetched only once from the database
 */
@Model
@ApplicationScoped
public class QuestionModel {
    private Map<Locale, Map<QuestionType, List<Question>>> questions = new HashMap<>();
    private List<Locale> availableLocales = FeedbacktoolUtil.getSupportedLocales();

    private QuestionDAO questionDAO;
    private UserSettings userSettings;

    /**
     * Only needed because it is a managed bean
     */
    public QuestionModel() {
    }

    /**
     * Constructs the question model
     *
     * @param questionDAO  the questionDAO which is used to get the question from the database
     * @param userSettings the userSettings of a specific user
     */
    @Inject
    public QuestionModel(QuestionDAO questionDAO, UserSettings userSettings) {
        this.questionDAO = questionDAO;
        this.userSettings = userSettings;
        loadAllQuestionsFromDB();
    }

    /**
     * Initializes all variables
     */
    public void initializeQuestions() {
        for (Locale locale : availableLocales) {
            Map<QuestionType, List<Question>> differentTypes = new HashMap<>();
            differentTypes.put(QuestionType.SCALA, new ArrayList<Question>());
            differentTypes.put(QuestionType.TEXT, new ArrayList<Question>());
            differentTypes.put(QuestionType.TEXT_LONG, new ArrayList<Question>());
            questions.put(locale, differentTypes);
        }
    }

    /**
     * Loads all questions from a json file in multiple languages. If there are mistakes in the json file or not every
     * Question has a translation shows a error message
     */
    public void loadQuestionsFromJson() {
        initializeQuestions();
        Map<QuestionType, List<JSONArray>> jsonArrays = new HashMap<>();
        jsonArrays.put(QuestionType.SCALA, new LinkedList<JSONArray>());
        jsonArrays.put(QuestionType.TEXT, new LinkedList<JSONArray>());
        jsonArrays.put(QuestionType.TEXT_LONG, new LinkedList<JSONArray>());
        int numberOfScalaQuestions = 0;
        int numberOfAdditionalQuestions = 0;
        int numberOfFreetextQuestions = 0;
        for (int i = 0; i < availableLocales.size(); i++) {
            questionDAO.resetLatestQuestion();
            String questionsJson = FeedbacktoolUtil.readQuestionsFromFile(Constants.JSON_FILENAME + "_" + availableLocales.get(i).toString() + ".json");
            try {
                JSONObject jsonObject = new JSONObject(questionsJson);
                JSONArray currentScala = jsonObject.getJSONArray("Scala Questions");
                JSONArray currentAdditional = jsonObject.getJSONArray("Additional Information");
                JSONArray currentFreeText = jsonObject.getJSONArray("Free text Questions");
                if (i != 0) {
                    if (jsonArrays.get(QuestionType.SCALA).get(i - 1).length() != currentScala.length()
                            || jsonArrays.get(QuestionType.TEXT).get(i - 1).length() != currentAdditional.length()
                            || jsonArrays.get(QuestionType.TEXT_LONG).get(i - 1).length() != currentFreeText.length()) {
                        JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "Messages", userSettings.getLocale(), "errorMessageJson");
                        return;
                    }
                } else {
                    numberOfScalaQuestions = currentScala.length();
                    numberOfAdditionalQuestions = currentAdditional.length();
                    numberOfFreetextQuestions = currentFreeText.length();
                }
                jsonArrays.get(QuestionType.SCALA).add(currentScala);
                jsonArrays.get(QuestionType.TEXT).add(currentAdditional);
                jsonArrays.get(QuestionType.TEXT_LONG).add(currentFreeText);
            } catch (JSONException e) {
                JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "Messages", userSettings.getLocale(), "errorMessageJsonParsing", e.getMessage());
                return;
            }
        }


        for (Map.Entry<QuestionType, List<JSONArray>> entry : jsonArrays.entrySet()) {
            List<JSONArray> scalaQuestionArrays = jsonArrays.get(entry.getKey());
            for (int i = 0; i < entry.getValue().get(0).length(); i++) {
                int questionID = questionDAO.createQuestion(entry.getKey());
                for (int j = 0; j < scalaQuestionArrays.size(); j++) {
                    questionDAO.createQuestionTranslation(questionID, scalaQuestionArrays.get(j).getString(i), availableLocales.get(j));
                    questions.get(availableLocales.get(j)).get(entry.getKey()).add(new Question(questionID, scalaQuestionArrays.get(j).getString(i), entry.getKey(), availableLocales.get(j)));
                }
            }
        }

        JsfUtil.addStatusMessage(FacesMessage.SEVERITY_INFO, "Messages", userSettings.getLocale(), "infoMessageJson", numberOfScalaQuestions + numberOfAdditionalQuestions + numberOfFreetextQuestions);
    }

    /**
     * Loads all questions from the Database and add them to the suitable list
     */
    public void loadAllQuestionsFromDB() {
        initializeQuestions();
        for (Question question : questionDAO.getQuestionsInAllLanguages()) {
            switch (question.getType()) {
                case TEXT:
                    questions.get(question.getLocale()).get(QuestionType.TEXT).add(question);
                    break;
                case SCALA:
                    questions.get(question.getLocale()).get(QuestionType.SCALA).add(question);
                    break;
                case TEXT_LONG:
                    questions.get(question.getLocale()).get(QuestionType.TEXT_LONG).add(question);
                    break;
            }
        }
    }

    /**
     * Get a new list of the latest required questions (the questions are copied in a new array when called)
     *
     * @return a new list of the latest required questions
     */
    public List<Question> getRequiredQuestions(Locale locale) {
        return new ArrayList<>(questions.get(locale).get(QuestionType.SCALA));
    }

    /**
     * Get a new list of the latest optional questions (the questions are copied in a new array when called)
     *
     * @return a new list of the latest optional questions
     */
    public List<Question> getOptionalQuestions(Locale locale) {
        return new ArrayList<>(questions.get(locale).get(QuestionType.TEXT));
    }

    /**
     * Get a new list of the latest free text questions (the questions are copied in a new array when called)
     *
     * @return a new list of the latest free text questions
     */
    public List<Question> getFreeTextQuestions(Locale locale) {
        return new ArrayList<>(questions.get(locale).get(QuestionType.TEXT_LONG));
    }
}
