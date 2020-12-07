package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.*;

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

    public void beginQuestionnaire(User u, Questionnaire qst) {
        UserQuestionnaire.Key key = new UserQuestionnaire.Key(u.getId(), qst.getDate());

        UserQuestionnaire uqToDelete = em.find(UserQuestionnaire.class, key);
        if (uqToDelete == null) {
            UserQuestionnaire uq = new UserQuestionnaire(u.getId(), qst.getDate());
            em.persist(uq);
        }
    }

    public boolean checkRespondedToMarketingQuestions(User u, Questionnaire qst) {
        for (Question q : qst.getQuestions()) {
            Answer.Key key = new Answer.Key(q.getId(), u.getId());
            if (em.find(Answer.class, key) == null) {
                return false;
            }
        }

        return true;
    }

    public void addStatAnswers(StatAnswers statAnswers) {
        em.persist(statAnswers);
    }

    public void submitQuestionnaire(User u, Questionnaire qst) {
        UserQuestionnaire.Key key = new UserQuestionnaire.Key(u.getId(), qst.getDate());

        UserQuestionnaire uq = em.find(UserQuestionnaire.class, key);

        uq.setHasSubmitted(true);
    }

    public UserQuestionnaire find(User u, Questionnaire qst) {
        UserQuestionnaire.Key key = new UserQuestionnaire.Key(u.getId(), qst.getDate());

        return em.find(UserQuestionnaire.class, key);
    }
}
