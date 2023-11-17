package DataBaseConnection.demo.Security;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeRepo;
import DataBaseConnection.demo.Filter.CustomAuthenticationFilter;
import DataBaseConnection.demo.Filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Import(CorsConfig.class)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final EmployeeRepo employeeRepo;

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        //By default, it allows requests from any origin (*).
        //It allows common HTTP methods such as GET, POST, and OPTIONS.
        //It allows common HTTP headers.
        //It allows credentials to be included in cross-origin requests.
        http.cors(Customizer.withDefaults());
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/login").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/employees/{email}/**").hasAnyAuthority("ADMIN"));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(POST, "/employees").hasAnyAuthority("ADMIN"));
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/employees").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers( "/tasks/**").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/roles").hasAnyAuthority("ADMIN"));
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/signout").permitAll());
        // we're passing the authorization filter as the first filter, and we want to specify that it's for the UsernamePasswordAuthenticationFilter class
        http.addFilter(new CustomAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, employeeRepo));
        http.addFilterAfter(new CustomAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
