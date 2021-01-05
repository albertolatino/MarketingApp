package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.FormException;
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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/TodaysQuestionnaire")
public class TodaysQuestionnaire extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService qstService;
    @EJB(name = "it.polimi.db2.marketing.services/UserQuestionnaireService")
    private UserQuestionnaireService uqService;

    public TodaysQuestionnaire() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Questionnaire qst;
        try {
            qst = qstService.getToday();
        } catch (QuestionnaireNotFoundException e) {
            String path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
            return;
        } catch (QuestionnaireException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        if (uqService.isSubmitted(user, qst)) {
            String path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
            return;
        }

        uqService.beginQuestionnaire(user, qst);

        Map<Integer, String> answersMap = (Map<Integer, String>) session.getAttribute("answers");

        Map<String, Object> variables = new HashMap<>();
        variables.put("questionnaireName", qst.getTitle());
        variables.put("questions", qst.getQuestions());
        variables.put("answers", answersMap);
        renderPage(request, response, "/WEB-INF/TodaysQuestionnaire.html", variables);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String loginpath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }

        User user = (User) session.getAttribute("user");

        Questionnaire qst;
        try {
            qst = qstService.getToday();
        } catch (QuestionnaireNotFoundException e) {
            String path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
            return;
        } catch (QuestionnaireException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        if (uqService.checkNotStartedNorFinished(user, qst)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad action sequence!");
            return;
        }

        // contains all indices of all questions in the current questionnaire
        Map<Integer, String> answers;
        try {
            answers = getAnswers(request, user, qst);
        } catch (NumberFormatException | NullPointerException | FormException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        request.getSession().setAttribute("answers", answers);

        Map<String, Object> variables = new HashMap<>();
        variables.put("questionnaireName", qst.getTitle());
        renderPage(request, response, "/WEB-INF/TodaysQuestionnaireStatistics.html", variables);
    }

    private Map<Integer, String> getAnswers(HttpServletRequest request, User user, Questionnaire qst) throws FormException {
        List<Integer> allQuestionnaireNumbers = qst.getQuestions().stream().map(Question::getId).collect(Collectors.toList());

        Map<Integer, String> answers = new HashMap<>();

        Enumeration<String> parameters = request.getParameterNames();
        String parameterName;

        while (parameters.hasMoreElements()) {

            parameterName = parameters.nextElement();
            if(!parameterName.equals("review")) {
                int questionNumber = Integer.parseInt(parameterName);
                String answerString = (StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
                if (answerString == null || answerString.length() == 0)
                    throw new FormException();

                answers.put(Integer.parseInt(parameterName), answerString);

                // removes the id of the answered question from the list, indicating that the question has been answered
                allQuestionnaireNumbers.remove(Integer.valueOf(questionNumber));
            }
        }

        if (allQuestionnaireNumbers.size() > 0)
            throw new FormException();

        return answers;

    }
}