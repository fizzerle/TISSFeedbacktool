package at.ac.tuwien.tiss.feedbacktool;

import at.ac.tuwien.tiss.feedbacktool.DAOTests.*;
import at.ac.tuwien.tiss.feedbacktool.ModelTests.ContractorModelTests;
import at.ac.tuwien.tiss.feedbacktool.ModelTests.EvaluationModelTests;
import at.ac.tuwien.tiss.feedbacktool.ModelTests.QuestionModelTests;
import at.ac.tuwien.tiss.feedbacktool.ModelTests.QuestionnaireModelTests;
import at.ac.tuwien.tiss.feedbacktool.entitiesTests.SummaryQuestionItemTests;
import at.ac.tuwien.tiss.feedbacktool.entitiesTests.PojoTester;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AccessControlDAOTests.class, AnswerDAOTests.class, ClientDAOTests.class, ContractorDAOTests.class, QuestionDAOTests.class, EvaluationModelTests.class,PojoTester.class, QuestionModelTests.class,ContractorModelTests.class, QuestionnaireModelTests.class, SummaryQuestionItemTests.class})
public class ProjectTestSuite {
}
