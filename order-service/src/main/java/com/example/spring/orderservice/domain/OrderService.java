package com.example.spring.orderservice.domain;

import com.example.spring.orderservice.book.Book;
import com.example.spring.orderservice.book.BookClient;
import com.example.spring.orderservice.event.OrderAcceptedMessage;
import com.example.spring.orderservice.event.OrderDispatchedMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.spring.orderservice.domain.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderService.class);
    private final BookClient bookClient;
    private final OrderRepository repository;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public Flux<Order> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Mono<Order> submitOrder(String isbn, int quantity) {
//        return Mono.just(buildRejectedOrder(isbn, quantity))
//                .flatMap(repository::save);
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
                .flatMap(repository::save)
                .doOnNext(this::publishOrderAcceptedEvent);

    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux
                .flatMap(message ->
                        orderRepository.findById(message.orderId()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private void publishOrderAcceptedEvent(Order order) {
        System.out.println("order.status().equals(ACCEPTED) " + order.status());
        if (!order.status().equals(ACCEPTED)) {
            return;
        }

        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Publishing order accepted event: {}", order.id());

        boolean result = streamBridge.send("acceptOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending order accepted event: {}, Order id: {}", result, order.id());
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return Order.builder()
                .id(existingOrder.id())
                .bookIsbn(existingOrder.bookIsbn())
                .bookName(existingOrder.bookName())
                .bookPrice(existingOrder.bookPrice())
                .quantity(existingOrder.quantity())
                .status(DISPATCHED)
                .createdDate(existingOrder.createdDate())
                .lastModifiedDate(existingOrder.lastModifiedDate())
                .version(existingOrder.version())
                .build();
    }

    private static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.builder()
                .bookIsbn(book.isbn())
                .bookName(book.title() + "-" + book.author())
                .bookPrice(book.price())
                .quantity(quantity)
                .status(ACCEPTED)
                .build();
    }

    private static Order buildRejectedOrder(String isbn, int quantity) {
        return Order.builder()
                .bookIsbn(isbn)
                .quantity(quantity)
                .status(REJECTED)
                .build();
    }
}
