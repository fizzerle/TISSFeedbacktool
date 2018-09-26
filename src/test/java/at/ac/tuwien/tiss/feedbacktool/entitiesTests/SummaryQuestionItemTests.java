package at.ac.tuwien.tiss.feedbacktool.entitiesTests;

import at.ac.tuwien.tiss.feedbacktool.entities.SummaryQuestionItem;
import at.ac.tuwien.tiss.util.fe.jsf.Constants;
import org.junit.Assert;
import org.junit.Test;

public class SummaryQuestionItemTests {

    @Test
    public void testGetAverageAnswer(){
        SummaryQuestionItem summaryQuestionItem = new SummaryQuestionItem(1,"question1",new int[]{0,0,1,0,0});
        String averageAnswer = summaryQuestionItem.getAverageAnswer();
        Assert.assertEquals("2",averageAnswer);
        summaryQuestionItem.setCounts(new int[]{0,0,0,0,5});
        Assert.assertEquals("4",summaryQuestionItem.getAverageAnswer());
        summaryQuestionItem.setCounts(new int[]{1,2,3,4,5});
        Assert.assertEquals("3",summaryQuestionItem.getAverageAnswer());
        summaryQuestionItem.setCounts(new int[]{1,2,3,4,30});
        Assert.assertEquals("4",summaryQuestionItem.getAverageAnswer());
    }
}
