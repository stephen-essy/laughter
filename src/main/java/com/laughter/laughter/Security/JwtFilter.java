package com.laughter.laughter.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.laughter.laughter.Service.UserDetailsServiceImplementation;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException, java.io.IOException{
        final String authHeader=request.getHeader("Authorization");
        String email=null;
        String token=null;

        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            email=jwtUtil.extractEmail(token);   
        }
        if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= userDetailsService.loadUserByUsername(email);
            
            if(jwtUtil.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } 
        filterChain.doFilter(request, response);
    }
}
