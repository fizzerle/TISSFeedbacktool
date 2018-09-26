package at.ac.tuwien.tiss.feedbacktool.model;

import at.ac.tuwien.tiss.feedbacktool.dao.ContractorDAO;
import at.ac.tuwien.tiss.feedbacktool.entities.Contractor;

import javax.enterprise.inject.Model;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * The class provides the model for the contractor dropdown
 */
@Model
@ApplicationScoped
public class ContractorModel {
    private List<Contractor> contractors;

    /**
     * Only needed because it is a managed bean
     */
    public ContractorModel() {
    }

    /**
     * Constructs a new Contractor Model
     *
     * @param contractorDAO the contractor DAO which is used to get the values from the database
     */
    @Inject
    public ContractorModel(ContractorDAO contractorDAO) {
        contractors = contractorDAO.getContractorNames();
    }

    /**
     * Gets contractors
     *
     * @return value of contractors
     */
    public List<Contractor> getContractors() {
        return contractors;
    }

    /**
     * Sets the field contractors to the given parameter
     *
     * @param contractors the parameter which the field gets assigned to
     */
    public void setContractors(List<Contractor> contractors) {
        this.contractors = contractors;
    }
}
