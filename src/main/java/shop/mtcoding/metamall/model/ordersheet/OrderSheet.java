package shop.mtcoding.metamall.model.ordersheet;

import shop.mtcoding.metamall.model.orderproduct.OrderProduct;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSheet { // 주문서
    private Long id;
    private User user; // 주문자
    private List<OrderProduct> orderProductList; // 총 주문 상품 리스트
    private Integer totalPrice; // 총 주문 금액 (총 주문 상품 리스트의 orderPrice 합)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
