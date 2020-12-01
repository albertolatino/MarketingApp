package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.CredentialsException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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

    public List<Questionnaire> getAllQuestionnaires() throws QuestionnaireException {
        List<Questionnaire> questionnaires;
        try {
            questionnaires = em.createNamedQuery("Questionnaire.getAll", Questionnaire.class).getResultList();
        } catch (PersistenceException e) {
            throw new QuestionnaireException("Could not verify credentals");
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

        return qList.size() == 1;
    }



}
