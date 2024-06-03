package com.postech.msproduct.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        ValidRequest(request);
        filterChain.doFilter(request, response);
    }

    private void ValidRequest(HttpServletRequest request) {
        var token = this.recoverToken(request);
        if (token == null) {
            request.setAttribute("error", "Bearer token inválido");
            return;
        }

        UserRole userRole = ProductGateway.getUserRoleFromToken(token);
        if (userRole == null) {
            request.setAttribute("error", "Usuário [token] inválido");
            return;
        }

        if (!checkAuthorization(request.getMethod(), userRole)) {
            request.setAttribute("error", "Método [" + request.getMethod()
                    + "] não autorizado ao perfil [" + userRole + "]");
        }
    }

    private boolean checkAuthorization(String method, UserRole userRole) {
        List<MethodAuthorized> methodAuthLst = new ArrayList<>();
        methodAuthLst.add(new MethodAuthorized("GET", UserRole.USER));
        methodAuthLst.add(new MethodAuthorized("GET", UserRole.ADMIN));
        methodAuthLst.add(new MethodAuthorized("POST", UserRole.ADMIN));
        methodAuthLst.add(new MethodAuthorized("PUT", UserRole.ADMIN));
        methodAuthLst.add(new MethodAuthorized("DELETE", UserRole.ADMIN));

        for (MethodAuthorized methodAuth : methodAuthLst) {
            if (methodAuth.getMethod().equals(method)
                    && methodAuth.getUserRole().equals(userRole)) {
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