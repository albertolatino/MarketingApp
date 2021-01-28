package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.StatAnswers;
import it.polimi.db2.marketing.ejb.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless
public class QuestionnaireManagerService {
    @PersistenceContext(unitName = "MarketingEJB")
    private EntityManager em;

    public QuestionnaireManagerService() {
    }

    public List<Questionnaire> getAll() {
        List<Questionnaire> questionnaires = em.createNamedQuery("Questionnaire.getAll", Questionnaire.class).getResultList();
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

        em.remove(q);
        em.flush(); //needed because of triggers
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


    public void addStatAnswers(Questionnaire q, User u, Integer age, String sex, String expertise) {
        StatAnswers sa = new StatAnswers(q.getDate(), u.getId(), age, sex, expertise);

        em.persist(sa);
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
                return true;
            }
        }
        return false;
    }

    public List<Question> getQuestions(Questionnaire q) {
        q = em.merge(q);

        return q.getQuestions();
    }

    public byte[] getQuestionnaireImage(Questionnaire q) {
        q = em.merge(q);

        return q.getImage();
    }

    public StatAnswers getStatAnswers(User user, Questionnaire questionnaire) {
        user = em.merge(user);

        return user.getStatAnswers().get(questionnaire.getDate());
    }
}
