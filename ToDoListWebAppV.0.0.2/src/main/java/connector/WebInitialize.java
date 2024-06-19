package connector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// This annotation marks the class as a Spring Boot application
@SpringBootApplication
public class WebInitialize implements WebApplicationInitializer {

    // Method called when the application starts up
    @Override
    public void onStartup(ServletContext context) throws ServletException {
        // Create the Spring application context
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(DatabaseConnector.class); // Registering the configuration class

        // Register the DispatcherServlet with the ServletContext
        ServletRegistration.Dynamic dispatcher = context.addServlet("dispatcher", new DispatcherServlet(ctx));
        dispatcher.setLoadOnStartup(1); // Set load-on-startup priority
        dispatcher.addMapping("/"); // Map the DispatcherServlet to the root URL ("/")
    }
}
