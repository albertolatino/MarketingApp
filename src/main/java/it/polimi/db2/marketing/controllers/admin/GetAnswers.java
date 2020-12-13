package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Answer;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet("/GetAnswers")
public class GetAnswers extends ServletBase {
    private static final long serialVersionUID = 1L;

    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireService")
    private QuestionnaireService qnnaireService;

    public GetAnswers() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;

        Date date = null;
        Integer userid = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //TODO escape
            date = sdf.parse(request.getParameter("date"));
            userid = Integer.parseInt(request.getParameter("userid"));
            //TODO see style
            //cannot see history for future questionnaire
            if (getToday().before(date)) {
                throw new Exception("Missing or empty credential value");
            }

        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date inserted has wrong format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        // show questionnaire answers of each user
        // get questions and answers of user for questionnaire of date
        List<Question> questions;
        List<Answer> answers;
        Questionnaire questionnaire;

        try {
            questionnaire = qnnaireService.findByDate(date);
            questions = questionnaire.getQuestions();
            answers = qnnaireService.getAnswersToQuestions(questions, userid);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("questions", questions);
        variables.put("answers", answers);

        renderPage(request, response, "/WEB-INF/answers.html", variables);
    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }
}