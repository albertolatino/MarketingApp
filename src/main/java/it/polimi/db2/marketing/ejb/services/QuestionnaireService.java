package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.*;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.*;

@Stateless
public class QuestionnaireService {
    @PersistenceContext(unitName = "MarketingEJB")
    private EntityManager em;

    public QuestionnaireService() {
    }

    public List<Questionnaire> getAll() throws QuestionnaireException {
        List<Questionnaire> questionnaires;
        try {
            questionnaires = em.createNamedQuery("Questionnaire.getAll", Questionnaire.class).getResultList();
        } catch (PersistenceException e) {
            throw new QuestionnaireException("Could not verify credentials");
        }
        if (questionnaires.isEmpty())
            return null;

        return questionnaires;
    }

    public void createQuestionnaire(ArrayList<String> questions, Date date, String title, byte[] imageData) {
        Questionnaire questionnaire = new Questionnaire(date, title, imageData);
        em.persist(questionnaire);

        for (String s : questions) {
            Question q = new Question(s);
            em.persist(q);
            q.setQuestionnaire(questionnaire);
        }
    }

    public void deleteQuestionnaire(Date deletionDate) {

        Questionnaire q = em.find(Questionnaire.class, deletionDate);
        System.out.println("DATA DEL QUESTIONARIO" + q.getDate());
        em.remove(q);
        em.flush();
    }

    public boolean questionnaireAlreadyExist(Date plannedDate) {
        Questionnaire q = em.find(Questionnaire.class, plannedDate);

        return q != null;
    }

    public Questionnaire findByDate(Date date) {
        return em.find(Questionnaire.class, date);
    }

    public Questionnaire getToday() {
        return em.find(Questionnaire.class, new Date());
    }

    public void addAnswers(User u, Map<Integer, String> answers) {
        u = em.merge(u);

        u.getAnswers().putAll(answers);
    }



    public void addStatAnswers(StatAnswers statAnswers) {
        em.persist(statAnswers);
    }

    public List<String> getAnswersToQuestions(User u, List<Question> questions) {
        List<String> answers = new ArrayList<>();

        u = em.merge(u);

        for (Question q : questions) {
            answers.add(u.getAnswers().get(q.getId()));
        }

        return answers;
    }

    public boolean containsOffensiveWords(Collection<String> answers) {
        Long num;
        for (String a : answers) {
            num = em.createNamedQuery("OffensiveWord.containsWord", Long.class).setParameter(1, a).getSingleResult();
            if (num != 0) {
                System.out.println(a + " contains a bad word");
                return true;
            } else
                System.out.println(a + " DOES NOT contain a bad word");
        }
        return false;
    }

    /*
    public AnsweredList getAllAnswers(Questionnaire q) {
        q = em.merge(q);
        List<Question> questions = q.getQuestions();
        AnsweredList toRet = new AnsweredList(questions);

        List<Object[]> users = em.createNamedQuery("User.usersWhoRespondedToQuestionnaire").setParameter(1, q.getDate()).getResultList();
        for (Object[] arr : users) {
            User u = (User) arr[0];
            Answer a = (Answer) arr[1];
            toRet.addAnswer(u.getName(), a.getQuestionId(), a.getAnswer());
        }

        return toRet;
    }

     */

    public List<Question> getQuestions(Questionnaire q) {
        q = em.merge(q);

        return q.getQuestions();
    }

}
