package com.postech.msproduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    Integer id;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    String category;
    String urlImage;
    @NotNull
    @Min(value = 0)
    Double price;
    @NotNull
    @Min(value = 0)
    @NotNull
    int quantity;
}
