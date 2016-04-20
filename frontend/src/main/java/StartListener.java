import leet.configurator.backend.ComponentManager;
import leet.configurator.backend.ComputerManager;
import leet.configurator.backend.Main;
import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartListener implements ServletContextListener {

    private final static Logger log = LoggerFactory.getLogger(StartListener.class);


    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("Web app initialized!");

        System.out.println("app init");

        ServletContext servletContext = ev.getServletContext();
        ApplicationContext springContext = new AnnotationConfigApplicationContext(Main.SpringConfig.class);
        servletContext.setAttribute("ComponentManager", springContext.getBean("ComponentManager", ComponentManager.class));
        servletContext.setAttribute("ComputerManager", springContext.getBean("ComputerManager", ComputerManager.class));

        log.info("Managers created.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("Exiting...");
    }
}