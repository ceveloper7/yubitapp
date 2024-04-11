package com.yubit.api.core.review;

public record Review(
        Long id,
        Long productId,
        String author,
        String subject,
        String content,
        String serviceAddress
) {
}
