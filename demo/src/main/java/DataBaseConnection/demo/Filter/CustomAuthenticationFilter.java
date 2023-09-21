package DataBaseConnection.demo.Filter;

import DataBaseConnection.demo.Security.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
        // use an algorithm to sign the jwt (access & refresh)
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());   // algorithm used to sign token


        String refresh_token = JWT.create().withSubject(user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);   // we don't need roles for the refresh token */

        // Use this HashMap if you want to include the user info as part of response
        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("access_token", access_token);
        responseJson.put("refresh_token", refresh_token);
        responseJson.put("user", userMap);

        // when user sends request, these should be part of the header
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseJson); // ObjectMapper provides functionality for reading and writing JSON
    }

}
