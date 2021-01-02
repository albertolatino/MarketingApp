package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.*;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.marketing.utils.AnsweredList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        ArrayList<Question> qs = new ArrayList<>();

        for (String s : questions) {
            Question q = new Question(date, s);
            qs.add(q);
        }

        questionnaire.setQuestions(qs);
        em.persist(questionnaire);//questions are persisted with cascading
    }

    public void deleteQuestionnaire(Date deletionDate) {

        Questionnaire q = em.find(Questionnaire.class, deletionDate);
        System.out.println("DATA DEL QUESTIONARIO" + q.getDate());
        em.remove(q);
        em.flush();
    }

    public boolean questionnaireAlreadyExist(Date plannedDate) {
        List<Questionnaire> qList = null;
        qList = em.createNamedQuery("Questionnaire.getByDate", Questionnaire.class).setParameter(1, plannedDate).getResultList();

        return qList.size() > 0;
    }

    public Questionnaire findByDate(Date date) {
        return em.find(Questionnaire.class, date);
    }

    public Questionnaire getToday() throws QuestionnaireNotFoundException, QuestionnaireException {
        Questionnaire questionnaire;
        try {
            questionnaire = em.createNamedQuery("Questionnaire.getToday", Questionnaire.class).getSingleResult();
        } catch (NoResultException e) {
            throw new QuestionnaireNotFoundException();
        } catch (PersistenceException e) {
            throw new QuestionnaireException("Could not get Questionnaire");
        }

        return questionnaire;
    }

    public void addAnswers(List<Answer> answers) {
        for (Answer a : answers) {
            Answer.Key key = new Answer.Key(a.getQuestionId(), a.getUserId());
            Answer currentAnswer = em.find(Answer.class, key);
            if (currentAnswer == null) {
                em.persist(a);
            } else {
                currentAnswer.setAnswer(a.getAnswer());
            }
        }
    }



    public void addStatAnswers(StatAnswers statAnswers) {
        em.persist(statAnswers);
    }

    public List<Answer> getAnswersToQuestions(List<Question> questions, Integer userId) {
        List<Answer> answers = new ArrayList<>();
        Answer answer;
        Answer.Key answerKey;
        for (Question q : questions) {
            answerKey = new Answer.Key(q.getId(), userId);
            answer = em.find(Answer.class, answerKey);
            answers.add(answer);
        }
        return answers;
    }

    public boolean containsOffensiveWords(ArrayList<String> answers) {
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
