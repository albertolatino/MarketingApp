package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Answer;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.StatAnswers;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.ejb.services.UserQuestionnaireService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/TodaysQuestionnaireStatistics")
public class TodaysQuestionnaireStatistics extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService qstService;
    @EJB(name = "it.polimi.db2.marketing.services/UserQuestionnaireService")
    private UserQuestionnaireService uqService;

    public TodaysQuestionnaireStatistics() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;

        HttpSession session = request.getSession();

        String submit = StringEscapeUtils.escapeJava(request.getParameter("submit"));
        if (submit.equals("Cancel")) {
            session.removeAttribute("answers");
            String path = getServletContext().getContextPath() + "/Home?message=Questionnaire canceled.";
            response.sendRedirect(path);
            return;
        }

        User user = (User) session.getAttribute("user");

        Questionnaire qst;
        try {
            qst = qstService.getToday();
        } catch (QuestionnaireNotFoundException e) {
            String path = getServletContext().getContextPath() + "/Home?message=Error fetching the questionnaire.";
            response.sendRedirect(path);
            return;
        } catch (QuestionnaireException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        if (uqService.hasSubmitted(user, qst)) {
            String path = getServletContext().getContextPath() + "/Home?message=Already submitted!";
            response.sendRedirect(path);
            return;
        }

        if (!uqService.checkAlreadyExists(user, qst)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad action sequence!");
            return;
        }

        ArrayList<Answer> answers = (ArrayList<Answer>) session.getAttribute("answers");
        session.removeAttribute("answers");
        if (answers == null) {
            String questionnairePath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(questionnairePath);
            return;
        }

        // insert statistical questions
        Integer age = null;
        String sex = null;
        String expertise = null;
        String unparsedAge;
        unparsedAge = StringEscapeUtils.escapeJava(request.getParameter("age"));
        if (unparsedAge != null && !unparsedAge.isEmpty()) {
            try {
                age = Integer.parseInt(unparsedAge);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad integer input for age");
                return;
            }
        }

        sex = StringEscapeUtils.escapeJava(request.getParameter("sex"));
        expertise = StringEscapeUtils.escapeJava(request.getParameter("expertise"));
        if (sex.isEmpty()) sex = null;
        if (expertise.isEmpty()) expertise = null;

        StatAnswers statAnswers = new StatAnswers(qst.getDate(), user.getId(), age, sex, expertise);

        //convert to list of answers to strings
        ArrayList<String> answersString = new ArrayList<>();
        for (Answer a : answers)
            answersString.add(a.getAnswer().toLowerCase());


        //answersString.add("DICK11 is bad word");
        //answersString.add("PENIS is bad word");
        //answersString.add("CHICKEN is not bad word");

        boolean containsProfanity = qstService.containsOffensiveWords(answersString);
        if (containsProfanity) {
            //block user, display blocked page
            user.setIsBlocked(true);
            session.removeAttribute("user");
            renderPage(request, response, "/WEB-INF/blocked-user.html");
            return;
        }

        qstService.addAnswers(answers);
        qstService.addStatAnswers(statAnswers);
        uqService.submitQuestionnaire(user, qst);

        String path = getServletContext().getContextPath() + "/Home?message=Questionnaire responses successfully submitted!";
        response.sendRedirect(path);
    }
}
