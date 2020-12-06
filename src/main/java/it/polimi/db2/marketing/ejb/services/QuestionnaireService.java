package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Answer;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;

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

    public void createQuestionnaire(ArrayList<String> questions, Date date, String title){

        Questionnaire questionnaire = new Questionnaire(date, title);

        ArrayList<Question> qs = new ArrayList<>();

        for(String s : questions){

            Question q = new Question(questionnaire,date,s);
            em.persist(q);//TODO FARE MEGLIO CON QUESTION SERVICE
            qs.add(q);
        }

        questionnaire.setQuestions(qs);

        em.persist(questionnaire);
    }

    public boolean questionnaireAlreadyExist(Date plannedDate){

        List<Questionnaire> qList = null;

           qList = em.createNamedQuery("Questionnaire.getByDate", Questionnaire.class).setParameter(1,plannedDate).getResultList();

        return qList.size() > 0;
    }

    public Questionnaire getToday() throws QuestionnaireNotFoundException, QuestionnaireException  {
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
            em.persist(a);
        }
    }
}
