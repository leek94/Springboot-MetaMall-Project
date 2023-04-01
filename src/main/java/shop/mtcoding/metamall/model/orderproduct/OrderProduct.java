package shop.mtcoding.metamall.model.orderproduct;

import shop.mtcoding.metamall.model.product.Product;

import java.time.LocalDateTime;

public class OrderProduct { // 주문 상품
    private Long id;
    private Product product;
    private Integer count; // 상품 주문 개수
    private Integer orderPrice; // 상품 주문 금액
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
