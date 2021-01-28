package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.StatAnswers;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.DateException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireManagerService;
import it.polimi.db2.marketing.ejb.services.UserService;

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


@WebServlet("/GetAnswers")
public class GetAnswers extends ServletBase {
    private static final long serialVersionUID = 1L;

    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireManagerService")
    private QuestionnaireManagerService qmService;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/UserService")
    private UserService uService;

    public GetAnswers() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date date = null;
        Integer userid = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(request.getParameter("date"));
            userid = Integer.parseInt(request.getParameter("userid"));
            if (getToday().before(date)) {
                throw new DateException("Missing or empty credential value");
            }
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date inserted has wrong format");
        } catch (DateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        // show questionnaire answers of each user
        // get questions and answers of user for questionnaire of date
        List<Question> questions;
        List<String> answers;
        Questionnaire questionnaire;
        User user;
        StatAnswers statAnswers;

        try {
            questionnaire = qmService.findByDate(date);
            questions = qmService.getQuestions(questionnaire);
            user = uService.getUser(userid);
            answers = qmService.getAnswersToQuestions(user, questions);
            statAnswers = qmService.getStatAnswers(user, questionnaire);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("questions", questions);
        variables.put("answers", answers);
        variables.put("statAnswers", statAnswers);
        variables.put("user", user);
        renderPage(request, response, "/WEB-INF/Answers.html", variables);
    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }
}