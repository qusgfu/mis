package cel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;

import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.WebResourceCollection;

@SpringBootApplication 
@MapperScan(basePackages = "cel.logweb")
@PropertySource(value={"file:ssaConf/ssa-config.properties"})
@PropertySource(value={"file:ssaConf/start-config.properties"}) 
public class StartApplication {
	
	 public static void main(String[] args) throws Exception {   
		    SpringApplication.run(StartApplication.class, args);
     }
	 

	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.getUrlMappings().clear();
		registration.addUrlMappings("*.do");
		registration.addUrlMappings("*.html"); 
		return registration;
	}
 
	
	@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container.getClass().isAssignableFrom(UndertowEmbeddedServletContainerFactory.class)) {
                    UndertowEmbeddedServletContainerFactory undertowContainer = (UndertowEmbeddedServletContainerFactory) container;
                    undertowContainer.addDeploymentInfoCustomizers(new ContextSecurityCustomizer());
                }
            }
        };
    }

    private static class ContextSecurityCustomizer implements UndertowDeploymentInfoCustomizer {

        @Override
        public void customize(io.undertow.servlet.api.DeploymentInfo deploymentInfo) {
            SecurityConstraint constraint = new SecurityConstraint();
            WebResourceCollection traceWebresource = new WebResourceCollection();
            traceWebresource.addUrlPattern("/*");
            traceWebresource.addHttpMethod("HEAD");
            traceWebresource.addHttpMethod("PUT");
            traceWebresource.addHttpMethod("DELETE");
            traceWebresource.addHttpMethod("OPTIONS");
            traceWebresource.addHttpMethod("TRACE");
            traceWebresource.addHttpMethod("COPY");
            traceWebresource.addHttpMethod("SEARCH");
            traceWebresource.addHttpMethod("PROPFIND");
            traceWebresource.addHttpMethod("MOVE");
            traceWebresource.addHttpMethod("TRACK");
            traceWebresource.addHttpMethod("PROPPATCH");
            traceWebresource.addHttpMethod("MKCOL");
            traceWebresource.addHttpMethod("LOCK");
            traceWebresource.addHttpMethod("UNLOCK"); 
            
            constraint.addWebResourceCollection(traceWebresource);
            deploymentInfo.addSecurityConstraint(constraint);
        }

    }

	
}
