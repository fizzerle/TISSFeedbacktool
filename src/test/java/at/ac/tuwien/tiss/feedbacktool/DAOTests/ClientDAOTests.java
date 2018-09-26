package at.ac.tuwien.tiss.feedbacktool.DAOTests;

import at.ac.tuwien.tiss.feedbacktool.dao.ClientDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientDAOTests extends JpaBaseRolledBackTestCase{
    private ClientDAO clientDAO;

    @Before
    public void init(){
        clientDAO = new ClientDAO(em);
    }

    @Test
    public void testCreateClient() {
        Assert.assertFalse(clientDAO.clientExists("testuser"));
        clientDAO.createClient("testuser");
        Assert.assertTrue(clientDAO.clientExists("testuser"));
    }

    @Test
    public void textClientExists() {
        Assert.assertFalse(clientDAO.clientExists("testuser"));
        clientDAO.createClient("testuser");
        Assert.assertTrue(clientDAO.clientExists("testuser"));
    }
}
