package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
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


}
