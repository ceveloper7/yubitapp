package com.yubit.api.core.review;

// invitados a revistar un producto
public record Review(
        int productId,
        int id,
        String author,
        String subject,
        String content,
        String serviceAddress
) {
}
