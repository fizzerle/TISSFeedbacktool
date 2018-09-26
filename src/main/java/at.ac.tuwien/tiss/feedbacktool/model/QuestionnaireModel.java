package at.ac.tuwien.tiss.feedbacktool.model;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.AccessControlDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.ClientDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.ContractorDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Answer;
import at.ac.tuwien.tiss.feedbacktool.entities.Client;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.util.fe.jsf.Constants;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * The class provides the model for the questionnaire part
 */
@Model
@ViewAccessScoped
public class QuestionnaireModel implements Serializable {
    private List<Answer> requiredAnswers;
    private List<Answer> optionalAnswers;
    private List<Answer> freeTextAnswers;
    private Client client;

    private AnswerDAO answerDAO;
    private AccessControlDAO accessControlDAO;
    private ClientDAO clientDAO;
    private QuestionModel questionModel;
    private UserSettings userSettings;
    private int selectedContractor;

    /**
     * @param answerDAO        the answerDAO which is used to get the answers from the datasource
     * @param accessControlDAO the accessControlDAO which is used to get the accessControlEntries from the datasource
     * @param clientDAO        the clientDAO which is used to get the clients from the datasource
     * @param contractorDAO    the contractorDAO which is used to get the contractors from the datasource
     * @param questionModel    the questionModel which is used to get the questions
     * @param userSettings     the userSettings of a specific user
     * @param contractorModel  the contractorModel which is used to get the contractors
     */
    @Inject
    public QuestionnaireModel(AnswerDAO answerDAO, AccessControlDAO accessControlDAO, ClientDAO clientDAO, ContractorDAO contractorDAO, QuestionModel questionModel, UserSettings userSettings, ContractorModel contractorModel) {
        this.answerDAO = answerDAO;
        this.accessControlDAO = accessControlDAO;
        this.clientDAO = clientDAO;
        this.questionModel = questionModel;
        this.userSettings = userSettings;
        selectedContractor = contractorModel.getContractors().get(0).getId();
        //here the real username should be read of the TISS backend
        client = new Client();
        client.setUsername("exampleUsername");
    }

    /**
     * Only needed because it is a managed bean
     */
    public QuestionnaireModel() {
    }

    @PostConstruct
    public void init() {
        getQuestions();
    }

    public void getQuestions() {
        requiredAnswers = new LinkedList<>();
        for (Question question : questionModel.getRequiredQuestions(userSettings.getLocale())) {
            requiredAnswers.add(new Answer(question));
        }
        optionalAnswers = new LinkedList<>();
        for (Question question : questionModel.getOptionalQuestions(userSettings.getLocale())) {
            optionalAnswers.add(new Answer(question));
        }
        freeTextAnswers = new LinkedList<>();
        for (Question question : questionModel.getFreeTextQuestions(userSettings.getLocale())) {
            freeTextAnswers.add(new Answer(question));
        }
    }

    public List<Answer> getOptionalAnswers() {
        return optionalAnswers;
    }

    public void setOptionalAnswers(List<Answer> optionalAnswers) {
        this.optionalAnswers = optionalAnswers;
    }

    public List<Answer> getRequiredAnswers() {
        return requiredAnswers;
    }

    public void setRequiredAnswers(List<Answer> requiredAnswers) {
        this.requiredAnswers = requiredAnswers;
    }

    public List<Answer> getFreeTextAnswers() {
        return freeTextAnswers;
    }

    public void setFreeTextAnswers(List<Answer> freeTextAnswers) {
        this.freeTextAnswers = freeTextAnswers;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String actionSubmit() {

        if (!allRequiredQuestionsAreAnswered() || checkMultipleSubmissions()) return null;

        writeAnswersToDatabase();
        return "success";
    }

    private boolean allRequiredQuestionsAreAnswered() {
        for (Answer requiredAnswer : requiredAnswers) {
            if (requiredAnswer.getText().equals("")) {
                JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "ErrMessages", userSettings.getLocale(), "allQuestionsRequired");
                return false;
            }
        }
        return true;
    }

    private void writeAnswersToDatabase() {
        Timestamp currentTime = FeedbacktoolUtil.getCurrentTimeStamp();
        for (Answer answer : requiredAnswers) {
            answerDAO.createAnswer(answer.getQuestion().getId(), answer.getText(), selectedContractor, client.getUsername(), currentTime);
        }
        for (Answer answer : optionalAnswers) {
            if (!answer.getText().isEmpty())
                answerDAO.createAnswer(answer.getQuestion().getId(), answer.getText(), selectedContractor, client.getUsername(), currentTime);
        }
        for (Answer answer : freeTextAnswers) {
            if (!answer.getText().isEmpty())
                answerDAO.createAnswer(answer.getQuestion().getId(), answer.getText(), selectedContractor, client.getUsername(), currentTime);
        }
    }

    private boolean checkMultipleSubmissions() {
        Timestamp currentTime = FeedbacktoolUtil.getCurrentTimeStamp();
        Timestamp lastTimeAccessed = accessControlDAO.getAccessControlEntry(client.getUsername(), selectedContractor);

        if (lastTimeAccessed != null) {
            long milliseconds = lastTimeAccessed.getTime() - currentTime.getTime();
            int weeks = (int) milliseconds / 1000 / 3600 / 168;
            if (weeks < Constants.WEEKS_BANNED) {
                JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "ErrMessages", userSettings.getLocale(), "userBlocked");
                return true;
            } else {
                accessControlDAO.updateAccessControlEntry(client.getUsername(), currentTime, selectedContractor);
            }
        } else {
            if (!clientDAO.clientExists(client.getUsername())) clientDAO.createClient(client.getUsername());
            accessControlDAO.createAccessControlEntry(client.getUsername(), currentTime, selectedContractor);
        }
        return false;
    }

    public void generateRandomFeedback(int feedbackcount) {
        for (int j = 0; j < feedbackcount; j++) {
            client.setUsername("User " + j);
            for (Answer answer : requiredAnswers) {
                answer.setText(((int) (Math.random() * 5)) + "");
            }
            for (Answer answer : optionalAnswers) {
                if (Math.random() < 0.5)
                    answer.setText("Dies ist das Feedback für " + answer.getQuestion().getText());
                else answer.setText("");
            }
            for (Answer answer : freeTextAnswers) {
                if (Math.random() < 0.5)
                    answer.setText("Dies ist das Feedback für " + answer.getQuestion().getText());
                else answer.setText("");
            }
            actionSubmit();
        }
    }

    public int getSelectedContractor() {
        return selectedContractor;
    }

    public void setSelectedContractor(int selectedContractor) {
        this.selectedContractor = selectedContractor;
    }
}
