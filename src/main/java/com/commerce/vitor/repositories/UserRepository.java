package com.commerce.vitor.repositories;

import com.commerce.vitor.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //Buscar um user pelo email
    //Usando os QueryMethods, já que é uma busca simples
    User findByEmail(String email);
}
