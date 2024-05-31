package com.postech.msproduct.entity.enums;

public enum ProductCategory {
    PERFUMARIA("Perfumaria e cosméticos"),

    ALIMENTOS("Alimentos e bebidas"),

    CASA("Casa e decoração"),

    SAUDE("Saúde e bem-estar"),

    UTIL("Utensílios domésticos"),

    PETS("Produtos para pets"),

    ESCRITORIO("Equipamentos de escritório");

    private String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
