package com.yubit.microservices.composite.product.services;

import com.yubit.api.core.composite.product.*;
import com.yubit.api.core.product.Product;
import com.yubit.api.core.recommendation.Recommendation;
import com.yubit.api.core.review.Review;
import com.yubit.api.exceptions.NotFoundException;
import com.yubit.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;

    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        Product product = integration.getProduct(productId);
        if (product == null) {
            throw new NotFoundException("No product found for productId: " + productId);
        }

        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations,
                                                    List<Review> reviews, String serviceAddress) {
        // 1. product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        List<RecommendationSummary> recommendationSummaries =
                recommendations.stream()
                        .map(recommendation -> new RecommendationSummary(
                                recommendation.getRecommendationId(),
                                recommendation.getAuthor(),
                                recommendation.getRate(),
                                recommendation.getContent()
                        ))
                        .collect(Collectors.toList());

        List<ReviewSummary> reviewSummaries = (reviews==null) ? null : reviews.stream()
                .map(review -> new ReviewSummary(
                        review.getReviewId(),
                        review.getAuthor(),
                        review.getSubject(),
                        review.getContent()))
                .collect(Collectors.toList());

        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";

        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress,productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight,  recommendationSummaries, reviewSummaries, serviceAddresses);
    }
}
