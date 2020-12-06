package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class UserQuestionnaireService {
    @PersistenceContext(unitName = "MarketingEJB")
    private EntityManager em;

    public UserQuestionnaireService() {
    }

    public List<User> getUsersWhoSubmitted(Date date) {

        List<User> users;
        users = em.createNamedQuery("UserQuestionnaire.getUsersWhoSubmitted", User.class)
                .setParameter(1, date).getResultList();

        return users;

    }
    public List<User> getUsersWhoCanceled(Date date) {

        List<User> users;
        users = em.createNamedQuery("UserQuestionnaire.getUsersWhoCanceled", User.class)
                .setParameter(1, date).getResultList();

        return users;

    }

}
