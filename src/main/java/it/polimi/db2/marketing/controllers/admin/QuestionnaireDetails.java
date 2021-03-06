package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.DateException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireOperationsService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/QuestionnaireDetails")
public class QuestionnaireDetails extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireOperationsService")
    private QuestionnaireOperationsService qopService;

    public QuestionnaireDetails() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(request.getParameter("date"));
            if (getToday().before(date)) {
                throw new DateException("Questionnaire is planned for the future, no answers yet");
            }
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date inserted has wrong format");
        } catch (DateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        //get users who submitted and canceled the questionnaire of date
        List<User> usersSubmitted, usersCanceled;

        try {
            usersSubmitted = qopService.getUsersWhoSubmitted(date);
            usersCanceled = qopService.getUsersWhoCanceled(date);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("usersSubmitted", usersSubmitted);
        variables.put("usersCanceled", usersCanceled);
        renderPage(request, response, "/WEB-INF/QuestionnaireDetails.html", variables);
    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }
}