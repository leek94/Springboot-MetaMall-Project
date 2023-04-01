package shop.mtcoding.metamall.model.product;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name; // 상품 이름
    private Integer price; // 상품 가격
    private Integer qty; // 상품 재고
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
