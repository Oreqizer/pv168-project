package leet.configurator.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class StartListener implements ServletContextListener {

    private final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("Web app initialized!");
        ServletContext servletContext = ev.getServletContext();
//        DataSource dataSource = Main.createMemoryDatabase(); TODO Main in backend
//        servletContext.setAttribute("customerManager", new CustomerManagerImpl(dataSource));
//        servletContext.setAttribute("bookManager", new BookManagerImpl(dataSource));
        log.info("Managers created.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("Exiting...");
    }
}