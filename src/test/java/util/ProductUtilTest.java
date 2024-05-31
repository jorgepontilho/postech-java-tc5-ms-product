package util;

import com.postech.msproduct.dto.ProductDTO;
import com.postech.msproduct.entity.Product;

public class ProductUtilTest {

    public static Product createProduct() {
        return new Product(createProductDTO());
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(
                1000,
                "Computador",
                "DEll Corei7",
                "ESCRITORIO",
                "www.image.com/123",
                1000.99,
                10
        );
    }
}
