package com.commerce.vitor.services;

import com.commerce.vitor.dto.CategoryDTO;
import com.commerce.vitor.entities.Category;
import com.commerce.vitor.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    //Retorna uma lista paginada
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> result = repository.findAll();
        return result.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

}
