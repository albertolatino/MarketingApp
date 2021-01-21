package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.services.QuestionnaireManagerService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/AdminHistory")
public class AdminHistory extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireManagerService")
    private QuestionnaireManagerService qmService;

    public AdminHistory() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;


        //retrieve all questionnaires
        List<Questionnaire> questionnaires;
        try {
            questionnaires = qmService.getAll();
        } catch (Exception e) {
            String path = getServletContext().getContextPath() + "/AdminHome?error=Error fetching questionnaires data";
            response.sendRedirect(path);
            return;
        }

        //TODO em.create questionnaire, but how to get date and title that are in another servlet?

        Map<String, Object> variables = new HashMap<>();
        variables.put("questionnaires", questionnaires);
        variables.put("today", new Date());
        renderPage(request, response, "/WEB-INF/History.html", variables);
    }
}