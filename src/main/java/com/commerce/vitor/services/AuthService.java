package com.commerce.vitor.services;

import com.commerce.vitor.entities.User;
import com.commerce.vitor.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;


    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();

        //Não contem ADMIN? e Não contem o mesmo ID de me? então lance erro
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("AAcces denied");
        }
    }
}
