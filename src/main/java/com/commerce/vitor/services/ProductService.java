package com.commerce.vitor.services;

import com.commerce.vitor.dto.ProductDTO;
import com.commerce.vitor.dto.ProductMinDTO;
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

    //Repository para buscar no BD
    @Autowired
    private ProductRepository repository;

    //Aqui Implementa a Busca no banco de dados
    //Recebe de argumento um ID
    //Retorna um Product DTO a partir do ID
    //Acessa o BD converte e retorna
    @Transactional(readOnly = true) //Uma operação de leitura, para ficar mais rápido
    public ProductDTO findById(Long id) {
        /* Passo a Passo
        //Acessando o banco de dados e retornando o ID que foi passado
        Optional<Product> result = repository.findById(id);
        //Pegando o result
        Product product = result.get();
        //convertendo para DTO
        ProductDTO dto = new ProductDTO(product); //Instanciando com o Construtor Simples
        return  dto;
         */
        //Jeito Simplificado, de 4 linhas para 2
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado")); //findById acessa o BD faz consulta SQL
        //orElseThrow, e para tentar fazer a opreção, caso não consiga lance um erro personalizado

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

    //Operação POST, inserir no BD
    @Transactional
    public ProductDTO insert(ProductDTO dto){
        //Instanciando, Recebendo do corpo da requisição
        Product entity = new Product();
        //Método
        copyDtoToEntity(dto, entity);
        //salvando no BD
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


    /*  Método copyDtoToEntity é para não precisar ficar repetindo esses comandos abaixo
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
    */
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
    }

}
