package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.exceptions.DateException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireManagerService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireManagerService")
    private QuestionnaireManagerService qmService;

    public CreateQuestionnaire() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        renderPage(request, response, "/WEB-INF/CreateQuestionnaire.html");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date questionnaireDate = null;
        String title = null;
        Part filePart = null;
        String filename = null;
        byte[] imageData = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            questionnaireDate = sdf.parse(request.getParameter("date"));
            title = (String) StringEscapeUtils.escapeJava(request.getParameter("title"));
            filePart = request.getPart("file");
            filename = filePart.getName();
            InputStream fileStream = filePart.getInputStream();
            imageData = new byte[fileStream.available()];
            fileStream.read(imageData);
            if (isBeforeToday(questionnaireDate)) {
                throw new DateException("Questionnaire date must not be in the past!");
            }
            if (qmService.questionnaireAlreadyExist(questionnaireDate)) {
                throw new DateException("A questionnaire for that date already exists!");
            }
        } catch (NumberFormatException | NullPointerException | ParseException | DateException e) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("error", e.getMessage());
            renderPage(request, response, "/WEB-INF/CreateQuestionnaire.html", variables);
            return;
        }


        // Take questions from form
        ArrayList<String> questions = new ArrayList<>();

        try {
            Enumeration<String> parameters = request.getParameterNames();
            String parameterName;

            while (parameters.hasMoreElements()) {
                parameterName = parameters.nextElement();
                if (!parameterName.equals("title") && !parameterName.equals("date"))
                    questions.add(StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
            }

        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }

        qmService.createQuestionnaire(questions, questionnaireDate, title, imageData);


        String path = getServletContext().getContextPath() + "/AdminHome?message=Questionnaire correctly created";
        response.sendRedirect(path);
    }

    private boolean isBeforeToday(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String strToday = dateFormat.format(new Date());

        return strDate.compareTo(strToday) < 0;
    }

}
