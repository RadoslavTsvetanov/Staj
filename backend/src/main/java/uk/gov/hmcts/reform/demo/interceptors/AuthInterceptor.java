package uk.gov.hmcts.reform.demo.interceptors;

import jakarta.servlet.http.HttpServletRequest;
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

    private boolean checkIfRequestRequiresAuthentication(String url) {
        return !url.contains("auth");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (!checkIfRequestRequiresAuthentication(request.getRequestURL().toString())) {
            return true;
        }

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        if (token == null || !auth.checkIfTokenExists(token)) {
            logger.warn("Unauthorized access attempt: Invalid or missing token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or missing token");
            return false;
        }

        return true;
    }
}
