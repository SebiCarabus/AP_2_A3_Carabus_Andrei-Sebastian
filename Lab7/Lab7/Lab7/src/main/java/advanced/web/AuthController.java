package advanced.web;

import advanced.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    public record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        if ("admin".equals(loginRequest.username()) && "parola123".equals(loginRequest.password())) {

            String token = jwtUtils.generateJwtToken(loginRequest.username());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password are incorrect");
    }
}
