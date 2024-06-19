package connector;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import entities.TaskEntity;
import entities.UserEntity;

// Enable Spring Web MVC
@EnableWebMvc
// Configuration class for Spring
@Configuration
// Component scanning to automatically discover Spring components like controllers, models, and services
@ComponentScan(basePackages = "controller, model, service")
public class DatabaseConnector {

    // Configure view resolver for JSP views
    @Bean
    public static InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    // Method to configure Hibernate properties
    public static Properties myprops() {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        props.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/todolistv3");
        props.put("hibernate.connection.username", "root");
        props.put("hibernate.connection.password", "root");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        return props;
    }

    // Method to configure Hibernate session factory
    @Bean
    public SessionFactory factory() {
        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration();
        config.setProperties(myprops());
        config.addAnnotatedClass(UserEntity.class);
        config.addAnnotatedClass(TaskEntity.class);
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        return config.buildSessionFactory(registry);
    }
}
