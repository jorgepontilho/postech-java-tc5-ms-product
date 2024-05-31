package com.postech.msproduct.interfaces;

import com.postech.msproduct.dto.ProductDTO;
import java.util.List;

public interface IProductGateway {
    public ProductDTO createProduct(ProductDTO product);

    public ProductDTO updateProduct(ProductDTO product);

    public boolean deleteProduct(Integer id);

    public ProductDTO findById(Integer id);

    public List<ProductDTO> listAll();
}
