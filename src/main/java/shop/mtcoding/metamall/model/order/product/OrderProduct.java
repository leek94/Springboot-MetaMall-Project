package shop.mtcoding.metamall.model.order.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.mtcoding.metamall.model.order.sheet.OrderSheet;
import shop.mtcoding.metamall.model.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter // DTO 만들면 삭제해야됨
@Getter
@Table(name = "order_product_tb")
@Entity
public class OrderProduct { // 주문 상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // checkpoint -> 무한
    @JsonIgnoreProperties({"seller"})
    @ManyToOne
    private Product product;
    @Column(nullable = false)
    private Integer count; // 상품 주문 개수
    @Column(nullable = false)
    private Integer orderPrice; // 상품 주문 금액
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // checkpoint -> 무한 참조
    @ManyToOne
    private OrderSheet orderSheet;

    //checkpoint -> 편의 메서드 만드는 이유
    public void syncOrderSheet(OrderSheet orderSheet){
        this.orderSheet = orderSheet;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public OrderProduct(Long id, Product product, Integer count, Integer orderPrice, LocalDateTime createdAt, LocalDateTime updatedAt, OrderSheet orderSheet) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.orderPrice = orderPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderSheet = orderSheet;
    }
}
