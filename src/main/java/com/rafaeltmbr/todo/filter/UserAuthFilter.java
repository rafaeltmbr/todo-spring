package com.rafaeltmbr.todo.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rafaeltmbr.todo.shared.Config;
import com.rafaeltmbr.todo.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


/**
 * Checks whether the user is authenticated with Basic Auth.
 */
@Component
public class UserAuthFilter extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!shouldAuthenticatePath(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        var decodedAuthorization = decodedBasicAuthHeader(request.getHeader("Authorization"));
        if (decodedAuthorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var foundUser = userRepository.findByUsername(decodedAuthorization[0]);
        if (foundUser == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var hashMatch = BCrypt.verifyer().verify(decodedAuthorization[1].toCharArray(), foundUser.getPassword());
        if (!hashMatch.verified) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        request.setAttribute("userId", foundUser.getId());
        filterChain.doFilter(request, response);
    }


    /**
     * Checks if the given path should be authenticated based on its prefix.
     *
     * @param path the path to be checked.
     * @return whether authentication should be performed for the given path.
     */
    private Boolean shouldAuthenticatePath(String path) {
        for (var prefix : Config.authenticatedPathsPrefix) {
            if (path.startsWith(prefix)) return true;
        }

        return false;
    }


    /**
     * Attempt to decode the given Authorization header with Basic Auth.
     *
     * @param authorizationHeader the Authorization header to be decoded.
     * @return If the authorization parameters are present, an array with the decoded username and password respectively
     * is returned. Null is returned otherwise.
     */
    private String[] decodedBasicAuthHeader(String authorizationHeader) {
        try {
            var encodedAuthorization = authorizationHeader.substring("Basic".length()).trim();
            var decodedAuthorization = new String(Base64.getDecoder().decode(encodedAuthorization)).split(":");
            return decodedAuthorization.length == 2 ? decodedAuthorization : null;
        } catch (Exception e) {
            return null;
        }
    }
}
