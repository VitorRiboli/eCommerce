package com.commerce.vitor.services;

import com.commerce.vitor.dto.CategoryDTO;
import com.commerce.vitor.dto.ProductDTO;
import com.commerce.vitor.dto.ProductMinDTO;
import com.commerce.vitor.entities.Category;
import com.commerce.vitor.entities.Product;
import com.commerce.vitor.repositories.ProductRepository;
import com.commerce.vitor.services.exceptions.DataBaseException;
import com.commerce.vitor.services.exceptions.ResourceNotFoundException;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// pode ser tanto @Component ou @Service
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {

        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return new ProductDTO(product);
    }

    //Retorna uma lista paginada
    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable){
        //Como é paginação, não se utiliza o List, e sim o Page, tipo de coleção especial para está função
        Page<Product> page = repository.searchByName(name, pageable);
        //O tipo Page não precisa converter para stream, e nem converte-la no final para .toList();
        return page.map(x -> new ProductMinDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return  new ProductDTO(entity);
    }


    //Metodo PUT, atualizar no BD
    @Transactional      //Long id para saber qual atulizar, ProductDTO que é o corpo da requisição com os dados de atualização
    public ProductDTO update(Long id, ProductDTO dto){
        try {
            //Instanciar produto que vai ser att com o ID.
            Product entity = repository.getReferenceById(id); //getReferenceById não acessa o BD, so prepara o Objeto, objeto monitorado pela JPA
            //Copiando para o produto os dados do dto, o corpo da requisição
            copyDtoToEntity(dto, entity);
            //Salvando
            entity = repository.save(entity);
            //retornando
            return  new ProductDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();

        for (CategoryDTO catDto : dto.getCategories()) {
            Category cat = new Category();
            cat.setId(catDto.getId());
            entity.getCategories().add(cat);
        }
    }

}
