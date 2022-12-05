package com.commerce.vitor.controllers;


import com.commerce.vitor.dto.CustomError;
import com.commerce.vitor.dto.ProductDTO;
import com.commerce.vitor.entities.Product;
import com.commerce.vitor.services.ProductService;
import com.commerce.vitor.services.exceptions.ResourceNotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service; //Acessando a camada Service

    @GetMapping(value = "/{id}") //Para utilizar como GET, value é para parametrizar
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
            //Acessando o metodo findById dentro da camada ProductService
            ProductDTO dto = service.findById(id);
            //Customizando a responsa, 201 ok
            return ResponseEntity.ok(dto);
    }

    @GetMapping //Retorna todos os Produtos, uma busca paginada. Tipo de coleção Page.
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            Pageable pageable) {
        Page<ProductDTO> dto = service.findAll(name, pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
