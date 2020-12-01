package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.*;
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

    public void createQuestionnaire(ArrayList<Question> questions, Date date, String title ){

        Questionnaire questionnaire = new Questionnaire(questions, date, title);
        //TODO persist???
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
}
