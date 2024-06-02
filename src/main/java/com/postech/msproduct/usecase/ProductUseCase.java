package com.postech.msproduct.usecase;

import com.postech.msproduct.dto.ProductDTO;
import com.postech.msproduct.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductUseCase {

    public static void validarProduto(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("Produto inválido.");
        }
        if (productDTO.getCategory() != null && new Product(productDTO).getCategory() == null) {
            throw new IllegalArgumentException("Categoria [" + productDTO.getCategory() + "] inválida.");
        }
    }

    public static void validarDeleteProduto(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
    }
}
