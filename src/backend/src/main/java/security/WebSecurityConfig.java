/** java/security/WebSecurityConfig.java
 * 
 * @author School of Computing and Information Systems, The University of 
 *         Melbourne 
 * 
 * The following code has been taken from the steps outlined in CIS Unimelb's 
 * "Milestone 0: Hello World" within their "React Project Development Setup", as
 * of 2025.12.06.
 * 
 * <p> Notes: {@link https://cis-projects.github.io/swen90007_course_notes/}
 * 
 * <p> Code Source:
 * {@link https://cis-projects.github.io/swen90007_course_notes/react-example/swen90007-react-example-primer-milestone-0.html#presentation-layer}
 */ 
package security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String PROPERTY_CORS_ORIGINS_UI = "CORS_ORIGINS_UI";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(System.getProperty(PROPERTY_CORS_ORIGINS_UI)));
        configuration.setAllowedMethods(Arrays.asList("GET"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}