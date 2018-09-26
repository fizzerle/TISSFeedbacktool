package at.ac.tuwien.tiss.feedbacktool.dao;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * Data access object for clients
 */
@Transactional
@ApplicationScoped
public class ClientDAO implements Serializable {

    private Query createClientQuery;
    private Query findClientByUsername;
    private EntityManager em;

    /**
     * constructs a new ClientDAO with nothing set
     */
    public ClientDAO() {
    }

    /**
     * Constructs a new ClientDAO with the entityManager set
     *
     * @param em the entityManager which is used to access the database
     */
    @Inject
    public ClientDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * creates a database entry for a client
     *
     * @param username the username of the client
     */
    public void createClient(String username) {
        createClientQuery = em.createNativeQuery("INSERT INTO clients(username) VALUES (:username)");
        createClientQuery.setParameter("username", username);
        createClientQuery.executeUpdate();
    }

    /**
     * Returns true if the client exists
     *
     * @param username of the client
     * @return true if the client with the given username exists
     */
    public boolean clientExists(String username) {
        findClientByUsername = em.createNativeQuery("SELECT username FROM clients WHERE username = :username");
        findClientByUsername.setParameter("username", username);
        return findClientByUsername.getResultList().size() == 1;
    }

}
