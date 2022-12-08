package com.commerce.vitor.dto;

import com.commerce.vitor.entities.Category;
import com.commerce.vitor.entities.Product;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


//Objeto para carregar dados de produtos
//DTO somente Getters está suficiente
public class ProductDTO {

    private Long id;
    @Size(min = 3, max = 80, message = "Nome entre 3 a 80 caracteres")
    @NotBlank(message = "Campo requerido") //NotBlank não aceita receber null, string vazia ou string somente com espaço em branco
    private String name;
    @Size(min = 10, message = "Descrição precista ter no minimo 10 caracteres")
    @NotBlank(message = "Descrição requirida")
    private String description;
    @NotNull(message = "Campo Requirido")
    @Positive(message = "O preço deve ser positivo")
    private Double price;
    private String imgUrl;
    @NotEmpty(message = "Deve ter pelo menos uma categoria")
    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(){

    }

    //Ambos construtores abaixos representam a mesma coisa
    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    //este Construtor é para facilitar quando precisar instanciar
    //Sobrecargga de construtor
    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        for (Category cat : entity.getCategories()) {
            categories.add(new CategoryDTO(cat));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

}
