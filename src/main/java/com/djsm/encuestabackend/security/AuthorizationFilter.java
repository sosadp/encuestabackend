package com.djsm.encuestabackend.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){

        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token!=null){
            token = token.replace(SecurityConstants.TOKEN_PREFIX,"");

            // esto es el email que estamos usando como user name
            String subject = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token).getBody().getSubject();

            // si el token es valido o esta vigente aun
            if (subject != null){

                return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());
            }
        }
        return null;
    }
}
