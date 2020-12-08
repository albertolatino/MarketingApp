package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebServlet("/AdminHistory")
public class AdminHistory extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionnaireService")
    private QuestionnaireService qService;

    public AdminHistory() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;


        //retrieve all questionnaires
        List<Questionnaire> questionnaires;
        try {
            questionnaires = qService.getAll();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }


        for(Questionnaire q : questionnaires) {
            System.out.println(q.getTitle());
            System.out.println(q.getDate());
        }
        //TODO em.create questionnaire, but how to get date and title that are in another servlet?

        String path = "/WEB-INF/history.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("questionnaires", questionnaires);
        getTemplateEngine().process(path, ctx, response.getWriter());

    }


    public void destroy() {
    }

}