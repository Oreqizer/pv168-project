package leet.configurator.frontend;

import leet.configurator.backend.ComponentManagerImpl;
import leet.configurator.backend.ComputerManagerImpl;
import leet.configurator.backend.Main;
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
        DataSource dataSource = Main.createMemoryDatabase();

        servletContext.setAttribute("computerManager", new ComputerManagerImpl(dataSource));
        servletContext.setAttribute("componentManager", new ComponentManagerImpl(dataSource));

        log.info("Managers created.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("Exiting...");
    }
}