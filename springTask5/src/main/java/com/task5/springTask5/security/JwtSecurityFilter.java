package com.task5.springTask5.security;

import com.task5.springTask5.entity.Users;
import com.task5.springTask5.repo.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Service
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtSecurityTokenUtils jwtSecurityTokenUtils;

    UserRepo userRepo;

    public JwtSecurityFilter(JwtSecurityTokenUtils jwtSecurityTokenUtils, UserRepo userRepo) {
        this.jwtSecurityTokenUtils = jwtSecurityTokenUtils;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * by this line we will get whole Authorization header in format like -
         * "Bearer(SPACE)TOKEN"
         */
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null){
            String username = jwtSecurityTokenUtils.extractUsername(authHeader);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                Users user = userRepo.findByUsername(username);

                if (user != null && jwtSecurityTokenUtils.validateToken(authHeader,user.getUsername())){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user.getUsername(),null,
                                    List.of(new SimpleGrantedAuthority(user.getRoleType())));

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user "+ username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request,response);
    }
}
