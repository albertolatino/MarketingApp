package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.utils.AnsweredList;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Home")
public class Home extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService qstService;

    public Home() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;

        Questionnaire qst;
        try {
            qst = qstService.getToday();
        } catch (Exception e) {
            qst = null;
        }

        String message = StringEscapeUtils.escapeJava(request.getParameter("message"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("today", qst != null);
        variables.put("message", message);
        if (qst != null) {
            byte[] imageData = qst.getImage();
            String encoded = new String(Base64.getEncoder().encode(imageData));
            variables.put("image", encoded);
            AnsweredList answers = qstService.getAllAnswers(qst);
            System.out.println(answers);
            variables.put("answers", answers);
        }
        renderPage(request, response, "/WEB-INF/Home.html", variables);
    }
}
