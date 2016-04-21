package web;

import configurator.common.DBException;
import configurator.common.EntityException;
import configurator.computer.Computer;
import configurator.computer.ComputerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oreqizer on 20/04/16.
 */
@WebServlet(ComputerServlet.URL_MAPPING + "/*")
public class ComputerServlet extends HttpServlet {

    private static final String LIST_JSP = "/list.jsp";
    public static final String URL_MAPPING = "/computers";

    private final static Logger log = LoggerFactory.getLogger(ComponentServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        log.debug("GET ...");
        showComputerList(req, res);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        //support non-ASCII characters in form
        req.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = req.getPathInfo();
        log.debug("POST ... {}", action);

        switch (action) {

            case "/add":
                addComputer(req, res);
                break;

            case "/delete":
                deleteComputer(req, res);
                break;

            case "/update":
                updateComputer(req, res);
                break;

            default:
                log.error("Unknown action " + action);
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);

        }

    }

    private void showComputerList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        log.debug("showing table of computers");

        req.setAttribute("computers", getComputerManager().getAllComputers());
        req.getRequestDispatcher(LIST_JSP).forward(req, res);

    }

    private void addComputer(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {

            //getting POST parameters from form
            int slots = Integer.parseInt(req.getParameter("slots"));
            int cooling = Integer.parseInt(req.getParameter("cooling"));
            int price = Integer.parseInt(req.getParameter("price"));

            // store to DB
            Computer pc = new Computer(slots, cooling, price);
            pc = getComputerManager().createComputer(pc);
            log.info("PC created: {}", pc.toString());

            //redirect-after-POST protects from multiple submission
            log.debug("redirecting after POST");
            res.sendRedirect(req.getContextPath() + URL_MAPPING);

        } catch (EntityException | DBException | NumberFormatException e) {

            log.error("Cannot add PC: {}", e);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    private void deleteComputer(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {

            Long id = Long.valueOf(req.getParameter("id"));
            getComputerManager().removeComputer(id);

            log.debug("redirecting after POST");
            res.sendRedirect(req.getContextPath() + URL_MAPPING);

        } catch (EntityException | DBException e) {

            log.error("Cannot delete computer", e);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    private void updateComputer(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {

            //getting POST parameters from form
            Long id = Long.valueOf(req.getParameter("id"));
            int slots = Integer.parseInt(req.getParameter("slots"));
            int cooling = Integer.parseInt(req.getParameter("cooling"));
            int price = Integer.parseInt(req.getParameter("price"));

            // store to DB
            Computer pc = getComputerManager().getComputer(id)
                    .setSlots(slots)
                    .setCooling(cooling)
                    .setPrice(price);

            getComputerManager().updateComputer(pc);
            log.info("PC updated: {}", pc.toString());

            //redirect-after-POST protects from multiple submission
            log.debug("redirecting after POST");
            res.sendRedirect(req.getContextPath() + URL_MAPPING);

        } catch (EntityException | DBException | NumberFormatException e) {

            log.error("Cannot update PC: {}", e);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    /**
     * Gets ComputerManager from ServletContext, where it was stored by {@link ./../StartListener}.
     *
     * @return ComputerManager instance
     */
    private ComputerManager getComputerManager() {
        return (ComputerManager) getServletContext().getAttribute("computerManager");
    }

}
