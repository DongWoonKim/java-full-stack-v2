package com.example.spring.orderservice.event;

import lombok.Builder;

@Builder
public record OrderDispatchedMessage(
        Long orderId
) {
}
