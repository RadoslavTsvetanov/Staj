package uk.gov.hmcts.reform.demo.interceptors;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.demo.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private final AuthService auth;

    @Autowired
    public AuthInterceptor(AuthService auth) {
        this.auth = auth;
    }

    public Boolean checkIfRequestRequiresAuthentication(String url){
        if(url.contains("auth")){ // a bit contradictory but it is like that since auth/login shouldn't require auth
            return false;
        }
        return true;
    }


    @Override
    public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        System.out.println("llllllllllllllllllllllllllllllllllllllllll"+" " + token);
        System.out.println("pppppp"+request.getRequestURL().toString());
        if(!checkIfRequestRequiresAuthentication(request.getRequestURL().toString())){ // TODO: make a better func name
            return true;
        }
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        if (token == null || !auth.checkIfTokenExists(token)) {
            logger.warn("Unauthorized access attempt: Invalid or missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or missing token");
            return false; // Prevent the request from reaching the controller
        }

        return true; // Proceed with the request
    }
}
