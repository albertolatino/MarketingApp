package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
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


@WebServlet("/AdminCreateQuestions")
@MultipartConfig
public class AdminCreateQuestions extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;

    public AdminCreateQuestions() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        renderPage(request, response, "/WEB-INF/AdminCreateQuestions.html");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        boolean isBadRequest = false;
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
            System.out.println("File size is : " + fileStream.available());
            imageData = new byte[fileStream.available()];
            fileStream.read(imageData);
            isBadRequest = isBeforeToday(questionnaireDate);
        } catch (NumberFormatException | NullPointerException | ParseException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        String message;
        //Increment by one
        //questionnaireDate = incrementDate(questionnaireDate,1);

        //check there aren't other questionnaires in this date
        //TODO EVENTUALMENTE SOSTITUIRE INVECE DI NAMED QUERY CON EM.FIND
        if (questionnaireService.questionnaireAlreadyExist(questionnaireDate)) {
            message = "A questionnaire for this date already exists";
        } else {

            message = "Questionnaire correctly created";


            // Take questions from form
            ArrayList<String> questions = new ArrayList<>();

            try {
                Enumeration<String> parameters = request.getParameterNames();
                String parameterName = null;

                while (parameters.hasMoreElements()) {


                    parameterName = (String) parameters.nextElement();
                    if (!parameterName.equals("title") && !parameterName.equals("date"))
                        questions.add(StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
                }

            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }


            questionnaireService.createQuestionnaire(questions, questionnaireDate, title, imageData);

        }


        //TODO check that there are no questionnaires planned for questionnaireDate in db
        //TODO add a successful message as a parameter to be shown in home

        Map<String, Object> variables = new HashMap<>();
        variables.put("message", message);
        renderPage(request, response, "/WEB-INF/AdminHome.html", variables);
    }

    private boolean isBeforeToday(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String strToday = dateFormat.format(new Date());

        return strDate.compareTo(strToday) < 0;
    }

}
