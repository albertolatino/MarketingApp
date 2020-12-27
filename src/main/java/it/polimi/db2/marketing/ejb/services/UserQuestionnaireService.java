package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.*;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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
        UserQuestionnaire uq = find(u, qst);

        return uq != null;
    }

    public void beginQuestionnaire(User u, Questionnaire qst) {
        UserQuestionnaire uqToDelete = find(u, qst);
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

    public void submitQuestionnaire(User u, Questionnaire qst) {
        UserQuestionnaire uq = find(u, qst);

        uq.setHasSubmitted(true);
    }

    public boolean hasSubmitted(User u, Questionnaire qst) {
        UserQuestionnaire uq = find(u, qst);

        return uq != null && uq.getHasSubmitted();
    }

    private UserQuestionnaire find(User u, Questionnaire qst) {
        UserQuestionnaire.Key key = new UserQuestionnaire.Key(u.getId(), qst.getDate());

        return em.find(UserQuestionnaire.class, key);
    }

    public void addReview(String text, User user, Questionnaire qst){

        UserQuestionnaire uqst = find(user, qst);
        uqst.setReview(text);
    }

    public List<String> getAllReviews(Questionnaire q){

       Date date = q.getDate();
       List<String> reviews = new ArrayList<>();

       List<UserQuestionnaire> userQuestionnaires = em.createNamedQuery("UserQuestionnaire.getReviewsByQst", UserQuestionnaire.class)
                .setParameter(1, date).getResultList();

       for(UserQuestionnaire uq : userQuestionnaires){
           reviews.add(uq.getReview());
       }

       return reviews;
    }
}
