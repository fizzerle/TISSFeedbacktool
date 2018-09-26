package at.ac.tuwien.tiss.feedbacktool.DAOTests;

import at.ac.tuwien.tiss.feedbacktool.dao.ContractorDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContractorDAOTests extends JpaBaseRolledBackTestCase{
    private ContractorDAO contractorDAO;

    @Before
    public void init(){
        contractorDAO = new ContractorDAO(em);
    }

    @Test
    public void testContractorNames(){
        Assert.assertEquals(contractorDAO.getContractorNames().size(),21);
    }
}
