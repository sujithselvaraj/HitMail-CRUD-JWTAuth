//package Sujith.MailCrud.utility;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//import io.jsonwebtoken.Jwts;
//
//
//@Component
//public class JWTUtility implements Serializable
//{
//    private static final long serialVersionUID = 234234523523L;
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    //retrieve username from jwt token
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//
//
//    }
//
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//    }
//
//
//    private Boolean isTokenExpired(String token) {
//        return getExpirationDateFromToken(token).before(new Date());
//
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, userDetails.getUsername());
//    }
//
//    @SneakyThrows
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//        Date expiration = new Date(System.currentTimeMillis() + 86400000);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(expiration)
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}
