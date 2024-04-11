package com.yubit.api.core.recommendation;

import com.yubit.api.core.product.Product;

public record Recommendation(
        Long id,
        Long productId,
        Long authorId,
        int rate,
        String content,
        String serviceAddress
) {
}
