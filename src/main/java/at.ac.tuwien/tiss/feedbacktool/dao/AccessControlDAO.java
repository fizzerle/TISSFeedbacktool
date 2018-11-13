package at.ac.tuwien.tiss.feedbacktool.dao;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Data Access Object for the Access Control
 */
@Transactional
@ApplicationScoped
public class AccessControlDAO implements Serializable {
    private Query createAccessControlEntry;
    private Query getAccessControlEntry;
    private Query updateAccessControlEntry;
    private EntityManager em;

    /**
     * constructs a new AccessControlDAO with nothing set
     */
    public AccessControlDAO() {
    }

    /**
     * Constructs a new AccessControlDAO with the entityManager set
     *
     * @param em the entityManager which is used to access the database
     */
    @Inject
    public AccessControlDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Creates the access Control Entries in the Database
     *
     * @param username       the username for the access control entry
     * @param submissionTime the timestamp defining when the user submitted the form
     * @param contractorid   the id of the contractor
     */
    public void createAccessControlEntry(String username, Timestamp submissionTime, int contractorid) {
        createAccessControlEntry = em.createNativeQuery("INSERT INTO accesscontrol(username, timestamp, contractorid) VALUES (:username,:timestamp,:contractorid)");
        createAccessControlEntry.setParameter("username", username);
        createAccessControlEntry.setParameter("timestamp", submissionTime);
        createAccessControlEntry.setParameter("contractorid", contractorid);
        createAccessControlEntry.executeUpdate();
    }

    /**
     * Gets the Access Control Entry from the Database
     *
     * @param username     the username for the access control entry
     * @param contractorID the contractor id for the access Control Entry
     * @return the timestamp when the user submitted the form the last time, null when the there is no entry
     */
    public Timestamp getAccessControlEntry(String username, int contractorID) {
        getAccessControlEntry = em.createNativeQuery("SELECT timestamp FROM accesscontrol WHERE username = :username AND contractorid = :contractorid");
        getAccessControlEntry.setParameter("username", username);
        getAccessControlEntry.setParameter("contractorid", contractorID);
        List resultList = getAccessControlEntry.getResultList();
        if (resultList.isEmpty()) return null;
        else return (Timestamp) getAccessControlEntry.getResultList().get(0);
    }

    /**
     * updates the access Control Entry. Should only be called if there is an Entry otherwise an exception is thrown
     *
     * @param username     the username that defines what access control entry to update
     * @param timestamp    the new timestamp
     * @param contractorid the contractor id that defines what access control entry to update
     */
    public void updateAccessControlEntry(String username, Timestamp timestamp, int contractorid) {
        updateAccessControlEntry = em.createNativeQuery("UPDATE accesscontrol SET  timestamp = :timestamp WHERE username = :username AND contractorid = :contractorid");
        updateAccessControlEntry.setParameter("username", username);
        updateAccessControlEntry.setParameter("contractorid", contractorid);
        updateAccessControlEntry.setParameter("timestamp", timestamp);
        updateAccessControlEntry.executeUpdate();
    }

    /**
     * Deletes all access control entries
     */
    public void deleteAllAccessControlEntries() {
        Query deleteAll = em.createNativeQuery("DELETE FROM accesscontrol");
        deleteAll.executeUpdate();
    }
}
