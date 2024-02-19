package tn.iit.security;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Order(1)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Value("${keycloak.config.file}")
    private Resource keycloakConfigFileLocation;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        
        http
            .authorizeRequests()
                .antMatchers("/hello").authenticated()
                .anyRequest().permitAll()
            .and()
            .formLogin()
                .loginPage("/login")  // Customize this URL if you have a custom login page
                .successHandler((request, response, authentication) -> response.sendRedirect("/hello"))
                .permitAll();
    }

    @Bean
    public AdapterDeploymentContext adapterDeploymentContext() throws Exception {
        return new AdapterDeploymentContextFactoryBean(keycloakConfigFileLocation).getObject();
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        // TODO: You might want to configure a session authentication strategy here
        return null;
    }
}
