package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.entities.UserQuestionnaire;

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

    public boolean checkAlreadyExists(User u, Questionnaire qst) {
        UserQuestionnaire.Key key = new UserQuestionnaire.Key(u.getId(), qst.getDate());

        UserQuestionnaire uq = em.find(UserQuestionnaire.class, key);

        return uq != null;
    }

    public void addQuestionnaireBegin(User u, Questionnaire qst) {
        UserQuestionnaire uq = new UserQuestionnaire(u.getId(), qst.getDate());

        em.persist(uq);
    }

}
