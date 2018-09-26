package at.ac.tuwien.tiss.feedbacktool.ModelTests;

import at.ac.tuwien.tiss.feedbacktool.dao.ContractorDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Contractor;
import at.ac.tuwien.tiss.feedbacktool.model.ContractorModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.LinkedList;

public class ContractorModelTests {
    ContractorModel contractorModel;

    @Test
    public void testGetAndSet(){
        ContractorDAO contractorDAOMock = Mockito.mock(ContractorDAO.class);
        final Contractor contractor = new Contractor(1,"Test Contractor");
        Mockito.when(contractorDAOMock.getContractorNames()).thenReturn(new LinkedList<Contractor>(){{add(contractor);}});
        contractorModel = new ContractorModel(contractorDAOMock);
        Assert.assertNotEquals(null,contractorModel.getContractors());
        Assert.assertEquals(contractor,contractorModel.getContractors().get(0));
        contractorModel.setContractors(new LinkedList<Contractor>());
    }
}
