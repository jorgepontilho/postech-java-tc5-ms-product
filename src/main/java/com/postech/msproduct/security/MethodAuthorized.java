package com.postech.msproduct.security;

import com.postech.msproduct.security.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MethodAuthorized {
    String method;
    UserRole userRole;
}