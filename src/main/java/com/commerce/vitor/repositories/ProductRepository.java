package com.commerce.vitor.repositories;

import com.commerce.vitor.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//Parametros<TiopDaEntidade, TipoDoIdDaEntidade>
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Essa classe é responsável por acessar o BD
    //Depois é chamada em Service
    //Extende a classe JpaRepository para acessar todos os metodos dela.

    //Consulta JPQL para aproveitar o resultado paginado com Pageable
    @Query("SELECT obj FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Product> searchByName(String name, Pageable pageable);
    //Método customizado, quando o ultimo argumento for o Pageable vai retornar um objeto paginado



}
