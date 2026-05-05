package home.project.am.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
	private static final String SECRET_KEY = "ETFBL_IP_secret_key:admin:nemanja";

    private Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }
    public String generateToken(String userName, String role) {
    	return Jwts.builder()
               .setSubject(userName)
               .claim("role", role)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + 1800000)) // Token traje pola sata
               .signWith(generateKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token je nevažeći
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
