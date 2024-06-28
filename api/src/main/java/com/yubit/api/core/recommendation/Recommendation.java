package com.yubit.api.core.recommendation;

import com.yubit.api.core.product.Product;

public record Recommendation(
        int productId,
        int recommendationId,
        String author,
        int rate,
        String content,
        String serviceAddress
) {
}
