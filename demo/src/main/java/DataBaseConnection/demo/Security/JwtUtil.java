package DataBaseConnection.demo.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JwtUtil {

    private String jwtCookie;

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());   // algorithm used to sign token
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authentication.getAuthorities());

        String access_token =  JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                // sign token
                .sign(algorithm);
        log.info(access_token);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);

        return access_token;
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        // get the token and remove "Bearer " from the string
        String token = null;
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    public String getUserNameFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        // get username that comes with token
        String username = decodedJWT.getSubject();
        return username;
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from("jwtToken", null).path("/").build();
    }
}
