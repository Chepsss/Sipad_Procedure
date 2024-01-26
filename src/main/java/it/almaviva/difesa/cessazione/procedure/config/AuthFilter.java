package it.almaviva.difesa.cessazione.procedure.config;

import it.almaviva.difesa.cessazione.procedure.constant.Constant;
import it.almaviva.difesa.cessazione.procedure.exception.InvalidTokenException;
import it.almaviva.difesa.cessazione.procedure.model.CustomUserDetail;
import it.almaviva.difesa.cessazione.procedure.model.common.RestApiError;
import it.almaviva.difesa.cessazione.procedure.service.rest.AuthServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter implements Filter {

    public static final List<String> EXCLUDED_SPECIFIC_PATHS = List.of(
            "/v2/api-docs",
            "/swagger-ui.html"
    );

    public static final List<String> EXCLUDED_ABSOLUTE_PATHS = List.of(
            "/swagger-resources/**",
            "/webjars/**",
            "/h2-console/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**"
    );

    @Autowired
    AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (EXCLUDED_SPECIFIC_PATHS.contains(path) || EXCLUDED_ABSOLUTE_PATHS.stream().anyMatch(p -> path.startsWith(p.replace("/**", "")))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader(Constant.AUTH_HEADER);

        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            InvalidTokenException e = new InvalidTokenException("", HttpStatus.UNAUTHORIZED, null);
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
            return;
        }

        String jwtToken = requestTokenHeader.substring(7);

        CustomUserDetail userDetails;
        try {
            userDetails = authServiceClient.getUserDetails(jwtToken);
            userDetails.setToken(jwtToken);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            InvalidTokenException ex = new InvalidTokenException(e.getMessage(), HttpStatus.UNAUTHORIZED, List.of(e.getMessage()));
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");

        RestApiError apiError = new RestApiError(status, ex);
        try {
            String json = apiError.convertToJson();
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
