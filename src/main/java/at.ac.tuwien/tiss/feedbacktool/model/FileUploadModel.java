package at.ac.tuwien.tiss.feedbacktool.model;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.AccessControlDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.ClientDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@Model
@ViewAccessScoped
public class FileUploadModel implements Serializable {

    private UserSettings userSettings;
    private QuestionModel questionModel;
    private StreamedContent exampleJson;
    private InputStream jsonInputStream;
    private AnswerDAO answerDAO;
    private ClientDAO clientDAO;
    private AccessControlDAO accessControlDAO;
    private QuestionDAO questionDAO;

    public FileUploadModel() {
    }

    @Inject
    public FileUploadModel(UserSettings userSettings, QuestionModel questionModel, AnswerDAO answerDAO, ClientDAO clientDAO, AccessControlDAO accessControlDAO, QuestionDAO questionDAO) {
        this.userSettings = userSettings;
        this.questionModel = questionModel;
        InputStream stream = JsfUtil.getCurrentClassLoader("").getResourceAsStream("questionsNew_de.json");
        exampleJson = new DefaultStreamedContent(stream, "application/json", "template.json");
        this.answerDAO = answerDAO;
        this.clientDAO = clientDAO;
        this.accessControlDAO = accessControlDAO;
        this.questionDAO = questionDAO;
    }

    private Part uploadedFile;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
        try {
            this.jsonInputStream = uploadedFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets exampleJson
     *
     * @return value of exampleJson
     */
    public StreamedContent getExampleJson() {
        return exampleJson;
    }

    public String upload() {
        if (this.uploadedFile == null) {
            JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "Messages", userSettings.getLocale(), "noUploadSelected");
            return null;
        } else {
            answerDAO.deleteAllAnswers();
            accessControlDAO.deleteAllAccessControlEntries();
            clientDAO.deleteAllClients();
            questionDAO.deleteAllQuestions();
            questionModel.loadAllQuestionsFromDB();
            String contentType = this.uploadedFile.getContentType();
            if (contentType.indexOf("json") == -1) {
                JsfUtil.addStatusMessage(FacesMessage.SEVERITY_ERROR, "Messages", userSettings.getLocale(), "uploadInvalidType");
                return null;
            } else {
                questionModel.loadQuestionsFromJson(jsonInputStream);
                return null;
            }
        }
    }
}
