package leet.configurator.backend;

import leet.common.DBException;
import leet.common.EntityException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by zeman on 20-Apr-16.
 */
public class Main {

    public static void main(String[] args) throws EntityException, DBException {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ComponentManager componentManager = ctx.getBean(ComponentManager.class);
        ComputerManager computerManager = ctx.getBean(ComputerManager.class);

        componentManager.getAllComponents().forEach(System.out::println);

        computerManager.getAllComputers().forEach(System.out::println);

        Computer computer = new Computer(5,6,200);
        computerManager.createComputer(computer);

    }

    @Configuration
    @EnableTransactionManagement
    @PropertySource("classpath:myconf.properties")
    public static class SpringConfig {

        @Autowired
        Environment env;

        @Bean
        public DataSource dataSource() {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName(env.getProperty("jdbc.driver"));
            bds.setUrl(env.getProperty("jdbc.url"));
            bds.setUsername(env.getProperty("jdbc.user"));
            bds.setPassword(env.getProperty("jdbc.password"));
            return bds;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        public ComponentManager computerManager() {

            return new ComponentManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));

        }

        @Bean
        public ComputerManager componentManager() {
            return new ComputerManagerImpl(dataSource());
        }


    }
}
