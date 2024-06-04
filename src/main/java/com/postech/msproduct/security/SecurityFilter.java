package com.postech.msproduct.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.postech.msproduct.gateway.ProductGateway;
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

        SecurityUser securityUser = ProductGateway.getUserFromToken(token);
        if (securityUser == null) {
            request.setAttribute("error", "Bearer token inválido");
            return;
        }

        if (!checkAuthorization(request.getMethod(), securityUser.getRole())) {
            request.setAttribute("error", "Método [" + request.getMethod()
                    + "] não autorizado [" + securityUser + "]");
        }
    }

    private boolean checkAuthorization(String method, String securityEnumUserRole) {
        List<SecurityMethodAuthorized> methodAuthLst = new ArrayList<>();
        methodAuthLst.add(new SecurityMethodAuthorized("GET", "USER"));
        methodAuthLst.add(new SecurityMethodAuthorized("GET", "ADMIN"));
        methodAuthLst.add(new SecurityMethodAuthorized("POST", "ADMIN"));
        methodAuthLst.add(new SecurityMethodAuthorized("PUT", "ADMIN"));
        methodAuthLst.add(new SecurityMethodAuthorized("DELETE", "ADMIN"));

        for (SecurityMethodAuthorized methodAuth : methodAuthLst) {
            if (methodAuth.getMethod().equals(method)
                    && methodAuth.getRole().equals(securityEnumUserRole)) {
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