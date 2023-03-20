package com.example.springwebclient.domain.product;

public record Product(
                       Long productCode,

                       // 상품 명
                       String productName,

                       // 상품 가격

                       int price) {
}
