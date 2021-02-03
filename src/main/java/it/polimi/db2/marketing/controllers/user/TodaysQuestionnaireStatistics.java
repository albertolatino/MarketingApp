package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.services.QuestionnaireManagerService;
import it.polimi.db2.marketing.ejb.services.QuestionnaireOperationsService;
import it.polimi.db2.marketing.ejb.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/TodaysQuestionnaireStatistics")
public class TodaysQuestionnaireStatistics extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireManagerService")
    private QuestionnaireManagerService qmService;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireOperationsService")
    private QuestionnaireOperationsService qopService;
    @EJB(name = "it.polimi.db2.marketing.services/UserService")
    private UserService uService;

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

        Questionnaire qst = qmService.getToday();

        if (qst == null) {
            String path = getServletContext().getContextPath() + "/Home?message=Error fetching the questionnaire.";
            response.sendRedirect(path);
            return;
        }

        if (qopService.isSubmitted(user, qst)) {
            String path = getServletContext().getContextPath() + "/Home?message=Already submitted!";
            response.sendRedirect(path);
            return;
        }

        if (qopService.checkNotStartedNorFinished(user, qst)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad action sequence!");
            return;
        }

        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        session.removeAttribute("answers");
        if (answers == null) {
            String questionnairePath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(questionnairePath);
            return;
        }

        boolean isBadRequest = false;

        // insert statistical questions
        Integer age = null;
        String unparsedAge = StringEscapeUtils.escapeJava(request.getParameter("age"));
        String sex = StringEscapeUtils.escapeJava(request.getParameter("sex"));
        String expertise = StringEscapeUtils.escapeJava(request.getParameter("expertise"));
        String review = StringEscapeUtils.escapeJava(request.getParameter("review"));

        if (review.isEmpty())
            review = null;

        if (unparsedAge != null && !unparsedAge.isEmpty()) {
            try {
                age = Integer.parseInt(unparsedAge);
                if (age <= 0) {
                    isBadRequest = true;
                }
            } catch (NumberFormatException e) {
                isBadRequest = true;
            }
        }

        if (sex.isEmpty()) {
            sex = null;
        } else if (!(sex.equals("M") || sex.equals("F") || sex.equals("N"))) {
            isBadRequest = true;
        }

        if (expertise.isEmpty()) {
            expertise = null;
        } else if (!(expertise.equals("H") || expertise.equals("M") || expertise.equals("L"))) {
            isBadRequest = true;
        }

        if (isBadRequest) {
            String path = getServletContext().getContextPath() + "/Home?message=Some fields of your questionnaire are wrong or incomplete!";
            response.sendRedirect(path);
            return;
        }

        boolean reviewContainsProfanity = false;
        if (review != null) {
            ArrayList<String> reviews = new ArrayList<>();
            reviews.add(review);
            reviewContainsProfanity = qmService.containsOffensiveWords(reviews);
        }

        boolean containsProfanity = qmService.containsOffensiveWords(answers.values());
        if (containsProfanity || reviewContainsProfanity) {
            //block user, display blocked page
            uService.blockUser(user);
            session.removeAttribute("user");
            renderPage(request, response, "/WEB-INF/BlockedUser.html");
            return;
        }

        qmService.addAnswers(user, answers);
        if (review != null)
            qopService.addReview(review, qst);
        qmService.addStatAnswers(qst, user, age, sex, expertise);
        qopService.submitQuestionnaire(user, qst);

        String path = getServletContext().getContextPath() + "/Home?message=Questionnaire responses successfully submitted!";
        response.sendRedirect(path);
    }
}
