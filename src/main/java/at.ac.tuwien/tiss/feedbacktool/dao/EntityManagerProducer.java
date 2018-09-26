package at.ac.tuwien.tiss.feedbacktool.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {

    @PersistenceContext(unitName = "FeedbacktoolPU")
    private EntityManager entityManager;

    @Produces
    @RequestScoped
    public EntityManager createDefaultEntityManager() {
        return this.entityManager;
    }

    protected void closeEntityManager(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
