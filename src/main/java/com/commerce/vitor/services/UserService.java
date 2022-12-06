package com.commerce.vitor.services;

import com.commerce.vitor.entities.User;
import com.commerce.vitor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //Precisa dessa interface implementada aqui para o framework spring fuincionar tudo corretamente com o security
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Buscar do banco de dados um usuário pelo email
        User user = repository.findByEmail(username);
        //Se o user for igual null, ou seja, não encontrado, lance uma exceção
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }
}
