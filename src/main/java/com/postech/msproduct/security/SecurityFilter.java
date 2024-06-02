package com.postech.msproduct.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.postech.msproduct.exceptions.NotFoundException;
import com.postech.msproduct.gateway.ProductGateway;
import com.postech.msproduct.security.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token == null) {
            throw new NotFoundException("Usuário [token] inválido");
        }
        UserRole userRole = ProductGateway.getUserRoleFromToken(token);
        if (userRole == null) {
            throw new NotFoundException("Usuário [token] inválido");
        }
        if (!checkAuthorization(request.getMethod(), userRole)) {
            throw new ServletException("checkAuthorization - Usuário [token] inválido");
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAuthorization(String method, UserRole userRole) {
        List<MappingAuthorization> mappingAuthLst = new ArrayList<>();
        mappingAuthLst.add(new MappingAuthorization("GET", UserRole.USER));
        mappingAuthLst.add(new MappingAuthorization("GET", UserRole.ADMIN));
        mappingAuthLst.add(new MappingAuthorization("POST", UserRole.ADMIN));
        mappingAuthLst.add(new MappingAuthorization("PUT", UserRole.ADMIN));
        mappingAuthLst.add(new MappingAuthorization("DELETE", UserRole.ADMIN));

        for (MappingAuthorization mappingAuth : mappingAuthLst) {
            if (mappingAuth.getMethod().equals(method)
                    && mappingAuth.getUserRole().equals(userRole)) {
                return true;
            }
        }
        return false;
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}