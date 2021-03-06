package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.services.QuestionnaireManagerService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/AdminDelete")
public class AdminDelete extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireManagerService")
    private QuestionnaireManagerService qmService;

    public AdminDelete() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        doPost(request, response);

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date deletionDate = null;
        boolean isBadRequest;


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deletionDate = sdf.parse(request.getParameter("date"));
            isBadRequest = !isBeforeToday(deletionDate);

        } catch (NumberFormatException | NullPointerException | ParseException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        String path = getServletContext().getContextPath() + "/AdminHome?";

        if (qmService.questionnaireAlreadyExist(deletionDate)) {
            path += "message=Questionnaire correctly deleted";
            qmService.deleteQuestionnaire(deletionDate);
        } else {
            path += "error=Cannot retrieve questionnaire for this date";
        }

        response.sendRedirect(path);
    }

    private boolean isBeforeToday(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String strToday = dateFormat.format(new Date());

        return strDate.compareTo(strToday) < 0;
    }

}