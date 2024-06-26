package com.yubit.api.core.composite.product;

/**
 * especificamos lo que nos interesa del Review
 */
public record ReviewSummary(
        int reviewId,
        String author,
        String subject
) {
}
