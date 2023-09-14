//package Sujith.MailCrud.Filter;
//
//import Sujith.MailCrud.Service.UserService;
//import Sujith.MailCrud.utility.JWTUtility;
//import lombok.AllArgsConstructor;
//import lombok.NonNull;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@AllArgsConstructor
//public class JWTFilter extends OncePerRequestFilter {
//    private JWTUtility jwtUtility;
//
//    private UserService userService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String token = new String();
//
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("BearerToken")) {
//                    token = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        String username = null;
//
//        try {
//            if (!token.isEmpty()) {
//                username = jwtUtility.getUsernameFromToken(token);
//            }
//        } catch (Exception e) {
//            Cookie cookie = new Cookie("BearerToken", null);
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(0);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            e.printStackTrace();
//        }
//
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userService.loadUserByUsername(username);
//            if (Boolean.TRUE.equals(jwtUtility.validateToken(token, userDetails))) {
//                UsernamePasswordAuthenticationToken authToken
//                        = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource()
//                                .buildDetails(request)
//                );
//
//                SecurityContextHolder
//                        .getContext()
//                        .setAuthentication(authToken);
//            }
//
//        }
//        filterChain.doFilter(request, response);
//    }
//
//}
//
//
//
