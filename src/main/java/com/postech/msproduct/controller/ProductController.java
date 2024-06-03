package com.postech.msproduct.controller;

import com.postech.msproduct.dto.ProductDTO;
import com.postech.msproduct.entity.Product;
import com.postech.msproduct.gateway.ProductGateway;
import com.postech.msproduct.usecase.ProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductGateway productGateway;

    public ProductController(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @PostMapping
    @Operation(summary = "Create a new product with a DTO", responses = {
            @ApiResponse(description = "The new product was created", responseCode = "201")
    })
    public ResponseEntity<?> createProduct(HttpServletRequest request, @Valid @RequestBody ProductDTO productDTO) {
        log.info("PostMapping - createProduct for product [{}]", productDTO.getName());
        if (request.getAttribute("error") != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(request.getAttribute("error"));
        }
        try {
            ProductUseCase.validarProduto(productDTO);
            ProductDTO productCreated = productGateway.createProduct(productDTO);
            return new ResponseEntity<>(productCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Get all products", responses = {
            @ApiResponse(description = "List of all products", responseCode = "200")
    })
    public ResponseEntity<?> listAllProducts() {
        log.info("GetMapping - listAllProducts");
        return new ResponseEntity<>(productGateway.listAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get only product by ID", responses = {
            @ApiResponse(description = "The product by ID", responseCode = "200", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(description = "Product Not Found", responseCode = "404", content = @Content(schema = @Schema(type = "string", example = "Produto não encontrado.")))
    })
    public ResponseEntity<?> findProduct(@PathVariable Integer id) {
        log.info("GetMapping - FindProduct");
        ProductDTO productDTO = productGateway.findById(id);
        if (productDTO != null) {
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Request for update a product by ID", responses = {
            @ApiResponse(description = "The products was updated", responseCode = "200", content = @Content(schema = @Schema(implementation = Product.class)))
    })
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductDTO productDTO) {
        log.info("PutMapping - updateProduct");
        try {
            ProductDTO productOld = productGateway.findById(id);
            ProductUseCase.validarProduto(productOld);

            productDTO.setId(id);
            ProductUseCase.validarProduto(productDTO);

            productDTO = productGateway.updateProduct(productDTO);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID", responses = {
            @ApiResponse(description = "The product was deleted", responseCode = "204")
    })
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        ProductDTO produtcDTO = productGateway.findById(id);
        ProductUseCase.validarDeleteProduto(produtcDTO);
        productGateway.deleteProduct(id);
        return new ResponseEntity<>("Produto removido.", HttpStatus.OK);
    }

    @GetMapping("/{id}/{qtty}")
    @Operation(summary = "Get the availability of a product by id and the quantity wanted", responses = {
            @ApiResponse(description = "True if the qtty is found", responseCode = "200")
    })
    public Boolean isProductAvailableById(@PathVariable int id, @PathVariable int qtty) {
        return productGateway.isProductAvailableById(id, qtty);
    }

    @PutMapping("/updateStockIncrease/{id}/{quantity}")
    @Operation(summary = "Increase the stock for one product by ID", responses = {
            @ApiResponse(description = "The stock was updated", responseCode = "200")
    })
    public ResponseEntity<?> updateStockIncrease(@Valid @PathVariable int id, @PathVariable int quantity) {
        ProductDTO productDTO = productGateway.updateStockIncrease(id, quantity);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/updateStockDecrease/{id}/{quantity}")
    @Operation(summary = "Decrease the stock for one product by ID", responses = {
            @ApiResponse(description = "The stock was updated", responseCode = "200")
    })
    public ResponseEntity<?> updateStockDecrease(@Valid @PathVariable int id, @PathVariable int quantity) {
        ProductDTO productDTO = productGateway.updateStockDecrease(id, quantity);
        return ResponseEntity.ok(productDTO);
    }

}
