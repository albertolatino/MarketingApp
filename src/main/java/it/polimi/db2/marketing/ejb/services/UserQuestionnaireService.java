package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless
public class UserQuestionnaireService {
    @PersistenceContext(unitName = "MarketingEJB")
    private EntityManager em;

    public UserQuestionnaireService() {
    }

    public List<User> getUsersWhoSubmitted(Date date) {
        List<User> users;
        users = em.createNamedQuery("User.getUsersWhoSubmitted", User.class)
                .setParameter(1, date).getResultList();
        return users;
    }

    public List<User> getUsersWhoCanceled(Date date) {
        List<User> users;
        users = em.createNamedQuery("User.getUsersWhoCanceled", User.class)
                .setParameter(1, date).getResultList();
        return users;
    }

    public boolean checkNotStartedNorFinished(User u, Questionnaire qst) {
        u = em.merge(u);

        return !u.getIsSubmitted().containsKey(qst.getDate());
    }

    public void beginQuestionnaire(User u, Questionnaire qst) {
        u = em.merge(u);

        u.getIsSubmitted().put(qst.getDate(), Boolean.FALSE);
    }

    /*
    public boolean checkRespondedToMarketingQuestions(User u, Questionnaire qst) {
        for (Question q : qst.getQuestions()) {
            Answer.Key key = new Answer.Key(q.getId(), u.getId());
            if (em.find(Answer.class, key) == null) {
                return false;
            }
        }

        return true;
    }
    */

    public void submitQuestionnaire(User u, Questionnaire qst) {
        u = em.merge(u);

        u.getIsSubmitted().put(qst.getDate(), true);
    }

    public boolean isSubmitted(User u, Questionnaire qst) {
        u = em.merge(u);

        return u.getIsSubmitted().getOrDefault(qst.getDate(), false);
    }

    public void addReview(String text, Questionnaire qst){
        qst = em.merge(qst);

        qst.getReviews().add(text);
    }

    public Set<String> getAllReviews(Questionnaire q) {
        q = em.merge(q);

        return q.getReviews();
    }
}
