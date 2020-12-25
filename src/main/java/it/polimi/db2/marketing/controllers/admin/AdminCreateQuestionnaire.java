package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/AdminCreate")
public class AdminCreateQuestionnaire extends ServletBase {
    private static final long serialVersionUID = 1L;


    public AdminCreateQuestionnaire() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        renderPage(request, response, "/WEB-INF/AdminCreate.html");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;


        Integer questionsNumber = null;
        boolean isBadRequest = false;


        try {
            questionsNumber = Integer.parseInt(request.getParameter("number"));
            isBadRequest = questionsNumber <= 0;
        } catch (NumberFormatException | NullPointerException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        //System.out.println(questionnaireDate);
        System.out.println(questionsNumber);

        Map<String, Object> variables = new HashMap<>();
        variables.put("number", questionsNumber);
        renderPage(request, response, "/WEB-INF/AdminCreateQuestions.html", variables);
    }
}