//package Sujith.MailCrud.Filter;
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSVerifier;
//import com.nimbusds.jose.crypto.RSASSAVerifier;
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URL;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.RSAPublicKeySpec;
//import java.text.ParseException;
//import java.util.Base64;
//import java.util.Date;
//
//public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String jwtToken = extractJwtToken(request);
//
//        try {
//            customJwtTokenVerification(jwtToken);
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String extractJwtToken(HttpServletRequest request) {
//
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }
//
//
//    public static void customJwtTokenVerification(String jwtToken) throws IOException, ParseException, JOSEException, NoSuchAlgorithmException, InvalidKeySpecException {
//
//        URL jwksURL = new URL("http://localhost:8080/realms/mailapplication/protocol/openid-connect/certs");
//
//        JWKSet jwkSet = JWKSet.load(jwksURL);
//
//        SignedJWT signedJWT = SignedJWT.parse(jwtToken);
//
//        JWSHeader jwsHeader = signedJWT.getHeader();
//
//        JWK jwk = jwkSet.getKeyByKeyId(jwsHeader.getKeyID());
//
//        System.out.println(jwk.toString());
//
//        if (jwk != null) {
//
//            RSAKey rsaKey =  jwk.toRSAKey();
//
//            byte[] modulusBytes = Base64.getUrlDecoder().decode(rsaKey.getModulus().toString());
//
//            byte[] exponentBytes = Base64.getUrlDecoder().decode(rsaKey.getPublicExponent().toString());
//
//            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new java.math.BigInteger(1, modulusBytes),
//
//                    new java.math.BigInteger(1, exponentBytes));
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            RSAPublicKey publicKey= (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
//
//            JWSVerifier verifier = new RSASSAVerifier(publicKey);
//
//            System.out.println(signedJWT.toString());
//
//            if (signedJWT.verify(verifier)) {
//
//                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
//
//
//                String issuer = claimsSet.getIssuer();
//                System.out.println("issuer"+issuer);
//                String audience = claimsSet.getAudience().get(0);
//
//                Date expiration = claimsSet.getExpirationTime();
//                if ("http://localhost:8080/realms/mailapplication".equals(issuer) &&
//                        expiration != null && expiration.after(new Date())) {
//
//                    System.out.println("Custom JWT token is valid");
//
//                } else {
//
//                    System.out.println("Custom JWT token validation failed");
//
//                }
//
//            } else {
//                System.out.println("Custom JWT token verification failed");
//            }
//        } else {
//            System.out.println("Public key not found or not RSA");
//        }
//    }
//}
