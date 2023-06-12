package shop.mtcoding.metamall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.metamall.core.annotation.MySessionStore;
import shop.mtcoding.metamall.core.exception.Exception400;
import shop.mtcoding.metamall.core.exception.Exception403;
import shop.mtcoding.metamall.core.session.SessionUser;
import shop.mtcoding.metamall.dto.ResponseDTO;
import shop.mtcoding.metamall.dto.order.OrderRequest;
import shop.mtcoding.metamall.model.order.product.OrderProduct;
import shop.mtcoding.metamall.model.order.product.OrderProductRepository;
import shop.mtcoding.metamall.model.order.sheet.OrderSheet;
import shop.mtcoding.metamall.model.order.sheet.OrderSheetRepository;
import shop.mtcoding.metamall.model.product.Product;
import shop.mtcoding.metamall.model.product.ProductRepository;
import shop.mtcoding.metamall.model.user.User;
import shop.mtcoding.metamall.model.user.UserRepository;

import javax.validation.Valid;
import java.util.List;

/**
 * 주문하기(고객), 주문목록보기(고객), 주문목록보기(판매자), 주문취소하기(고객), 주문취소하기(판매자)
 */
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderProductRepository orderProductRepository;
    private final OrderSheetRepository orderSheetRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<?> save(@RequestBody @Valid OrderRequest.SaveDTO saveDTO, Errors errors, @MySessionStore SessionUser sessionUser) {
        // 1. 세션값으로 유저 찾기
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(
                        () -> new Exception400("id", "해당 유저를 찾을 수 없습니다")
                );

        // 2. 상품 찾기
        List<Product> productListPS =
                productRepository.findAllById(saveDTO.getIds());

        // 3. 주문 상품
        List<OrderProduct> orderProductListPS = saveDTO.toEntity(productListPS);

        // 4. 주문서 만들기
        Integer totalPrice = orderProductListPS.stream().mapToInt((orderProduct)-> orderProduct.getOrderPrice()).sum(); // 가격 더할라고
        OrderSheet orderSheet = OrderSheet.builder()
                .user(userPS)
                .totalPrice(totalPrice)
                .build();
        OrderSheet orderSheetPS = orderSheetRepository.save(orderSheet);

        // 5. 주문서에 상품추가하고 재고감소하기
        orderProductListPS.stream().forEach(
                (orderProductPS -> {
                    orderSheetPS.addOrderProduct(orderProductPS);
                    orderProductPS.getProduct().updateQty(orderProductPS.getCount());
                })
        );

        // 6. 응답하기
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(orderSheetPS);
        return ResponseEntity.ok().body(responseDto);
    }

    // 유저 주문서 조회
    @GetMapping("/orders")
    public ResponseEntity<?> findByUserId(@MySessionStore SessionUser sessionUser){
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findByUserId(sessionUser.getId());
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(orderSheetListPS);
        return ResponseEntity.ok().body(responseDto);
    }

    // 그림 설명 필요!!
    // 배달의 민족은 하나의 판매자에게서만 주문을 할 수 있다. (다른 판매자의 상품이 담기면, 하나만 담을 수 있게 로직이 변한다)
    // 쇼핑몰은 여러 판매자에게서 주문을 할 수 있다.

    // 판매자 주문서 조회
    @GetMapping("/seller/orders")
    public ResponseEntity<?> findBySellerId(){
        // 판매자는 한명이기 때문에 orderProductRepository.findAll() 해도 된다.
        List<OrderSheet> orderSheetListPS = orderSheetRepository.findAll();
        ResponseDTO<?> responseDto = new ResponseDTO<>().data(orderSheetListPS);
        return ResponseEntity.ok().body(responseDto);
    }

    // 유저 주문 취소
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @MySessionStore SessionUser sessionUser){
        // 1. 주문서 찾기
        OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(
                ()-> new Exception400("id", "해당 주문을 찾을 수 없습니다")
        );

        // 2. 해당 주문서의 주인 여부 확인
        if(!orderSheetPS.getUser().getId().equals(sessionUser.getId())){
            throw new Exception403("권한이 없습니다");
        }

        // 3. 재고 변경하기
        orderSheetPS.getOrderProductList().stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
        });

        // 4. 주문서 삭제하기 (casecade 옵션)
        orderSheetRepository.delete(orderSheetPS);

        // 5. 응답하기
        ResponseDTO<?> responseDto = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDto);
    }

    // 판매자 주문 취소
    @DeleteMapping("/seller/orders/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id){
        // 1. 주문서 찾기
        OrderSheet orderSheetPS = orderSheetRepository.findById(id).orElseThrow(
                ()-> new Exception400("id", "해당 주문을 찾을 수 없습니다")
        );

        // 2. 재고 변경하기
        orderSheetPS.getOrderProductList().stream().forEach(orderProduct -> {
            orderProduct.getProduct().rollbackQty(orderProduct.getCount());
        });

        // 3. 주문서 삭제하기 (casecade 옵션)
        orderSheetRepository.delete(orderSheetPS);

        // 4. 응답하기
        ResponseDTO<?> responseDto = new ResponseDTO<>();
        return ResponseEntity.ok().body(responseDto);
    }
}