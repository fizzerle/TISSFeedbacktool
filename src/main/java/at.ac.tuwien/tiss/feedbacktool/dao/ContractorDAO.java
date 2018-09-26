package at.ac.tuwien.tiss.feedbacktool.dao;

import at.ac.tuwien.tiss.feedbacktool.entities.Contractor;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Data access object for contractors
 */
@Transactional
@ApplicationScoped
public class ContractorDAO implements Serializable {

    private Query selectAllContractors;
    private EntityManager em;

    /**
     * constructs a new ContractorDAO with nothing set
     */
    public ContractorDAO() {
    }

    /**
     * Constructs a new ContractorDAO with the entityManager set
     *
     * @param em the entityManager which is used to access the database
     */
    @Inject
    public ContractorDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Gets all contractors from the database
     *
     * @return a list of Contractor
     */
    public List<Contractor> getContractorNames() {
        selectAllContractors = em.createNativeQuery("SELECT id,name FROM contractors");
        List<Contractor> contractors = new LinkedList<>();
        List<Object[]> resultList = selectAllContractors.getResultList();
        for (Object[] result : resultList) {
            contractors.add(new Contractor((Integer) result[0], (String) result[1]));
        }
        return contractors;
    }

}
