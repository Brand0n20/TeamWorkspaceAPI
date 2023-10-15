package DataBaseConnection.demo.Filter;

import DataBaseConnection.demo.Employee.Employee;
import DataBaseConnection.demo.Employee.EmployeeRepo;
import DataBaseConnection.demo.Security.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private EmployeeRepo employeeRepo;

    private JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, EmployeeRepo employeeRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is {}", username);
        log.info("Password is {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);

    }

    // method called if login is successful
    // uses a cache to see log in attempts within a specific window
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // getting successful logged-in user
        User user = (User) authentication.getPrincipal();
        Map<String, Object> userMap = new HashMap<>();;
        userMap.put("username", user.getUsername());
        userMap.put("roles", user.getAuthorities());
        String access_token = jwtUtil.generateToken(authentication);
        Cookie jwtCookie = new Cookie("jwtToken", access_token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        // use an algorithm to sign the jwt (access & refresh)
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());   // algorithm used to sign token


        response.addCookie(jwtCookie);

        // when user sends request, these should be part of the header
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", access_token);
        //responseBody.put("refresh_token", refresh_token);
        responseBody.put("email", user.getUsername());
        String employeeName = Optional.ofNullable(employeeRepo.findByEmail(user.getUsername())).map(Employee::getName).orElse("Employee not found");
        responseBody.put("employee_name", employeeName);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        // new ObjectMapper().writeValue(response.getOutputStream(), jwtCookie); // ObjectMapper provides functionality for reading and writing JSON

    }

}
