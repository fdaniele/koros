package it.koros.producer.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class SecurityUtil {

    private static final String JWT_SECRET = "projectworkunipegasokorosdidanielefiorio2025";
    private static final String HMAC_SECRET = "hmacsecretunipegasfiorio";

    public static boolean verifyJwt(String jwt) {
        try {
            //Verifica del token JWT
            Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);

            return true;
        } catch (Exception e) {
            System.err.println("Errore nella verifica di sicurezza: " + e.getMessage());
            return false;
        }
    }

    public static boolean verifyHmac(String payload, String hmac) {
        try {
            //Verifica del HMAC SHA-256
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(HMAC_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String calculatedHmac = Base64.getEncoder().encodeToString(hash);

            return calculatedHmac.equals(hmac);
        } catch (Exception e) {
            System.err.println("Errore nella verifica di sicurezza: " + e.getMessage());
            return false;
        }
    }

    public static String generateJwt(String payload) {
        // JWT creation
        try {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
            String jwt;
            return jwt = Jwts.builder()
                .setSubject("koros-producer")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(600)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        } catch (Exception e) {
            throw new RuntimeException("Errore nella generazione del messaggio sicuro", e);
        }
    }

    public static String generateHmac(String payload) {
        // HMAC SHA-256 signature
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(HMAC_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella generazione del messaggio sicuro", e);
        }
    }
}
