package at.ac.tuwien.tiss.feedbacktool.model;

import at.ac.tuwien.tiss.feedbacktool.UserSettings;
import at.ac.tuwien.tiss.feedbacktool.dao.AnswerDAO;
import at.ac.tuwien.tiss.feedbacktool.dao.QuestionDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Answer;
import at.ac.tuwien.tiss.feedbacktool.entities.Client;
import at.ac.tuwien.tiss.feedbacktool.entities.Question;
import at.ac.tuwien.tiss.feedbacktool.entities.SummaryQuestionItem;
import at.ac.tuwien.tiss.util.fe.jsf.Constants;
import at.ac.tuwien.tiss.util.fe.jsf.JsfUtil;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * The class provides the model for the evaluation part
 */
@Model
@ViewAccessScoped
public class EvaluationModel implements Serializable {
    private int selectedContractor;
    private List<Answer> allAnswers;
    private List<SummaryQuestionItem> summaryQuestionItems;
    private List<Client> clientsWhoAnsweredQuestions;
    private AnswerDAO answerDAO;
    private QuestionDAO questionDAO;
    private UserSettings userSettings;
    private HorizontalBarChartModel horizontalBarModel;

    /**
     * Only needed because it is a managed bean
     */
    public EvaluationModel() {
    }

    /**
     * Constructs a new Evaluation Model
     *
     * @param answerDAO       the answerDAO which is used to get the answers from the datasource
     * @param questionDAO     the questionDAO which is used to get the questions from the datasource
     * @param contractorModel the contractorModel which is used to get the contractors from the datasource
     * @param userSettings    the userSettings of the specific user
     */
    @Inject
    public EvaluationModel(AnswerDAO answerDAO, QuestionDAO questionDAO, ContractorModel contractorModel, UserSettings userSettings) {
        this.answerDAO = answerDAO;
        this.questionDAO = questionDAO;
        this.userSettings = userSettings;
        selectedContractor = contractorModel.getContractors().get(0).getId();
    }

    /**
     * Gets all questions from the database and creates the horizontal bar model for the chart. Additionally creates the summary for all scala Questions
     * and prepare the single feedback for presentation.
     *
     * @return a string containing the name of the next page
     */
    public String actionSubmit() {
        allAnswers = answerDAO.getAnswersForContractorOrderedByUsername(selectedContractor, userSettings.getLocale());
        List<Answer> answersAddedUpForContractor = answerDAO.getAnswersAddedUpPerContractor(selectedContractor, userSettings.getLocale());
        List<Question> allQuestions = questionDAO.getAllScalaQuestions(userSettings.getLocale());
        createSummary(answersAddedUpForContractor, allQuestions);
        createHorizontalBarModel(summaryQuestionItems);
        generateSingleFeedback(allAnswers);
        return "evaluationResults";
    }

    /**
     * Loads the counted answers from the database and creates a overall summary for the scala questions
     *
     * @param answersAddedUpForContractor a list containing the answers added up per contractor
     * @param allQuestions a list containing all scala questions
     */
    public void createSummary(List<Answer> answersAddedUpForContractor, List<Question> allQuestions) {
        Map<Integer, SummaryQuestionItem> summaryQuestionItemsMap = new HashMap<>();

        for (Question question : allQuestions) {
            summaryQuestionItemsMap.put(question.getId(), new SummaryQuestionItem(question.getId(), question.getText()));
        }

        for (Answer answer : answersAddedUpForContractor) {
            SummaryQuestionItem summaryQuestionItem = summaryQuestionItemsMap.get(answer.getQuestion().getId());
            summaryQuestionItem.getCounts()[Integer.parseInt(answer.getText())] = answer.getNumberOfPeopleWhoAnsweredThis();
        }

        summaryQuestionItems = new ArrayList<>(summaryQuestionItemsMap.values());
        for (SummaryQuestionItem summaryQuestionItem : summaryQuestionItems) {
            int overallCount = 0;
            for (int count : summaryQuestionItem.getCounts()) {
                overallCount += count;
            }

            for (int i = 0; i < summaryQuestionItem.getCounts().length; i++) {
                summaryQuestionItem.getCounts()[i] = (int)Math.round((((double)summaryQuestionItem.getCounts()[i])/overallCount)*100);
            }

        }
    }

    /**
     * Creates the horizontal bar model and sets the field horizontalBarModel to it
     */
    public void createHorizontalBarModel(List<SummaryQuestionItem> summaryQuestionItems) {
        horizontalBarModel = new HorizontalBarChartModel();
        horizontalBarModel.setSeriesColors(Constants.CHART_COLORS);
        horizontalBarModel.setAnimate(true);

        int currentSkalaValue = 0;
        ChartSeries chartSeries = new ChartSeries();
        ChartSeries question;

        chartSeries.setLabel(Constants.SCALALABELS.get(currentSkalaValue));

        for (int i = 0; i < Constants.SCALALABELS.size(); i++) {
            question = new ChartSeries();
            question.setLabel(Constants.SCALALABELS.get(i));
            for (SummaryQuestionItem summaryQuestionItem : summaryQuestionItems) {

                double count = summaryQuestionItem.getCounts()[i];
                question.set(JsfUtil.getMessageResourceString("Messages", userSettings.getLocale(), "chartDescriptionNumber") + summaryQuestionItem.getQuestionID(), count);
            }
            horizontalBarModel.addSeries(question);
        }

        horizontalBarModel.setTitle(JsfUtil.getMessageResourceString("Messages", userSettings.getLocale(), "chartDescriptionScala"));
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel(JsfUtil.getMessageResourceString("Messages", userSettings.getLocale(), "chartLegendPerson"));

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel(JsfUtil.getMessageResourceString("Messages", userSettings.getLocale(), "chartLegendQuestion"));
    }

    /**
     * Prepares a list of clients to show the single feedback of every client
     *
     * @param allAnswers a list of all answers
     */
    public void generateSingleFeedback(List<Answer> allAnswers) {
        clientsWhoAnsweredQuestions = new LinkedList<>();
        String username = "";
        Timestamp timestamp = null;
        Client client = new Client();
        for (Answer answer : allAnswers) {
            if (!answer.getUsername().equals(username) || !answer.getSubmissionTime().equals(timestamp)) {
                if (!username.equals("")) clientsWhoAnsweredQuestions.add(client);
                client = new Client();
                username = answer.getUsername();
                timestamp = answer.getSubmissionTime();
            }
            client.setUsername(username);
            client.addAnswer(answer);
        }
        if (!username.equals("")) clientsWhoAnsweredQuestions.add(client);
    }

    /**
     * Gets the horizontal bar model for the chart
     *
     * @return the horizontal bar model for the chart
     */
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    /**
     * Gets selectedContractor
     *
     * @return value of selectedContractor
     */
    public int getSelectedContractor() {
        return selectedContractor;
    }

    /**
     * Sets the field selectedContractor to the given parameter
     *
     * @param selectedContractor the parameter which the field gets assigned to
     */
    public void setSelectedContractor(int selectedContractor) {
        this.selectedContractor = selectedContractor;
    }

    /**
     * Gets questionItems
     *
     * @return value of questionItems
     */
    public List<Answer> getAllAnswers() {
        return allAnswers;
    }

    /**
     * Sets the field questionItems to the given parameter
     *
     * @param allAnswers the parameter which the field gets assigned to
     */
    public void setAllAnswers(List<Answer> allAnswers) {
        this.allAnswers = allAnswers;
    }

    /**
     * Gets summaryQuestionItems
     *
     * @return value of summaryQuestionItems
     */
    public List<SummaryQuestionItem> getSummaryQuestionItems() {
        return summaryQuestionItems;
    }

    /**
     * Sets the field summaryQuestionItems to the given parameter
     *
     * @param summaryQuestionItems the parameter which the field gets assigned to
     */
    public void setSummaryQuestionItems(List<SummaryQuestionItem> summaryQuestionItems) {
        this.summaryQuestionItems = summaryQuestionItems;
    }

    /**
     * Gets clientsWhoAnsweredQuestions
     *
     * @return value of clientsWhoAnsweredQuestions
     */
    public List<Client> getClientsWhoAnsweredQuestions() {
        return clientsWhoAnsweredQuestions;
    }

    /**
     * Sets the field clientsWhoAnsweredQuestions to the given parameter
     *
     * @param clientsWhoAnsweredQuestions the parameter which the field gets assigned to
     */
    public void setClientsWhoAnsweredQuestions(List<Client> clientsWhoAnsweredQuestions) {
        this.clientsWhoAnsweredQuestions = clientsWhoAnsweredQuestions;
    }
}
