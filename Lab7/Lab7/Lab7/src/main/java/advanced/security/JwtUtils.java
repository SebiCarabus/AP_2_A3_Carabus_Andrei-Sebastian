package advanced.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private String jwtSecret = "cheia_mea_secreta_care_este_acum_destul_de_lunga_pentru_sha256";
    private int jwtExpirationMs = 86400000;

    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+this.jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
