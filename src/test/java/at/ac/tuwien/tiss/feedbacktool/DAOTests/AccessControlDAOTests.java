package at.ac.tuwien.tiss.feedbacktool.DAOTests;

import at.ac.tuwien.tiss.feedbacktool.dao.AccessControlDAO;
import at.ac.tuwien.tiss.util.fe.jsf.FeedbacktoolUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

public class AccessControlDAOTests extends JpaBaseRolledBackTestCase {
    private AccessControlDAO accessControlDAO;

    @Before
    public void init(){
        accessControlDAO = new AccessControlDAO(em);
    }

    @Test
    public void testCreateAccessControlEntry(){
        Timestamp currentTime = FeedbacktoolUtil.getCurrentTimeStamp();
        accessControlDAO.createAccessControlEntry("testuser1",currentTime,1);
        Timestamp lastAccessed = accessControlDAO.getAccessControlEntry("testuser1", 1);
        Assert.assertTrue(lastAccessed.equals(currentTime));
    }

    @Test
    public void testGetAccessControlEntry(){
        Timestamp lastAccessed = accessControlDAO.getAccessControlEntry("tesuser1", 1);
        Assert.assertEquals(lastAccessed,null);

        Timestamp currentTime = FeedbacktoolUtil.getCurrentTimeStamp();
        accessControlDAO.createAccessControlEntry("testuser1",currentTime,1);
        lastAccessed = accessControlDAO.getAccessControlEntry("testuser1", 1);
        Assert.assertEquals(lastAccessed,currentTime);
    }

    @Test
    public void testUpdateAccessControlEntry(){
        Timestamp currentTime = FeedbacktoolUtil.getCurrentTimeStamp();
        accessControlDAO.createAccessControlEntry("testuser1",currentTime,1);
        Timestamp lastAccessed = accessControlDAO.getAccessControlEntry("testuser1", 1);
        Assert.assertEquals(lastAccessed,currentTime);

        Timestamp updatedTime = FeedbacktoolUtil.getCurrentTimeStamp();
        accessControlDAO.updateAccessControlEntry("testuser1",updatedTime,1);
        lastAccessed = accessControlDAO.getAccessControlEntry("testuser1", 1);
        Assert.assertEquals(lastAccessed,updatedTime);
        Assert.assertNotEquals(updatedTime,currentTime);
    }
}
