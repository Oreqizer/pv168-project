import leet.common.DBException;
import leet.common.EntityException;
import leet.configurator.backend.Component;
import leet.configurator.backend.ComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(ComponentServlet.URL_MAPPING + "/*")
public class ComponentServlet extends HttpServlet {

    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/components";

    private final static Logger log = LoggerFactory.getLogger(ComponentServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET ...");
        showComponentsList(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("POST ... {}");
        resp.setCharacterEncoding("utf-8");

        String action = req.getPathInfo();
        log.debug("POST ... {}",action);
        switch (action) {
            case "/add":

                String name = req.getParameter("name");
                int heat = Integer.parseInt(req.getParameter("heat"));
                int price = Integer.parseInt(req.getParameter("price"));
                int energy = Integer.parseInt(req.getParameter("energy"));

                if (name == null || name.length() == 0 || price<0 ) {
                    req.setAttribute("chyba", "Musis vyplnit vsetky hodnoty !");
                    log.debug("form data invalid");
                    showComponentsList(req, resp);
                    return;
                }

                try {
                    Component component = new Component(name,heat,price,energy);

                    getComponentManager().createComponent(component);

                    log.debug("redirecting after POST");
                    resp.sendRedirect(req.getContextPath()+URL_MAPPING);
                    return;
                } catch (EntityException | DBException e) {
                    log.error("Cannot add component", e);
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long id = Long.valueOf(req.getParameter("id"));
                    getComponentManager().removeComponent(getComponentManager().getComponent(id));
                    log.debug("redirecting after POST");

                    resp.sendRedirect(req.getContextPath()+URL_MAPPING);
                    return;
                } catch (EntityException | DBException e) {
                    log.error("Cannot delete component", e);
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }




    private ComponentManager getComponentManager() {
        return (ComponentManager) getServletContext().getAttribute("componentManager");
    }

    /**
     *  stores components
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void showComponentsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            log.debug("showing table of components");
            request.setAttribute("components", getComponentManager().getAllComponents());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.error("Cannot show components", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
