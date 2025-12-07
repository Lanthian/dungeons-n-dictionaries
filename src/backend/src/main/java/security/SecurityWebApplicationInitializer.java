/** java/security/SecurityWebApplicationInitializer.java
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

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }
}