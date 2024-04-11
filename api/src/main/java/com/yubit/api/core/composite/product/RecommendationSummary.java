package com.yubit.api.core.composite.product;

public record RecommendationSummary(
        int recommendationId,
        String author,
        int rate
) {
}
