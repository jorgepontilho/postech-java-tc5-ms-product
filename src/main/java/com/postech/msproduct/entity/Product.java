package com.postech.msproduct.entity;

import com.postech.msproduct.dto.ProductDTO;
import com.postech.msproduct.entity.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_Product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String description;
    String category;
    String urlImage;
    Double price;
    int quantity;

    public Product(ProductDTO ProductDTO) {
        this.id = ProductDTO.getId();
        this.name = ProductDTO.getName();
        this.description = ProductDTO.getDescription();
        this.urlImage = ProductDTO.getUrlImage();
        this.price = ProductDTO.getPrice();
        this.quantity = ProductDTO.getQuantity();
        setCategory(ProductDTO.getCategory());
    }

    public ProductDTO toDTO() {
        return new ProductDTO(
                this.id,
                this.name,
                this.description,
                this.category,
                this.urlImage,
                this.price,
                this.quantity
        );
    }

    public void setCategory(String category) {
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (category.equals(productCategory.toString())) {
                this.category = category;
            }
        }
    }
}

