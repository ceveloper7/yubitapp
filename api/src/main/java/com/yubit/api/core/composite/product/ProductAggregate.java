package com.yubit.api.core.composite.product;

import java.util.List;

public record ProductAggregate(
        Long productId,
        String name,
        int weight,
        List<RecommendationSummary> recommendations,
        List<ReviewSummary> reviews,
        ServiceAddresses serviceAddresses
) {
}
