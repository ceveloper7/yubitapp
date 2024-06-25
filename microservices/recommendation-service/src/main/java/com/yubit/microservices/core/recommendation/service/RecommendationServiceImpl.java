package com.yubit.microservices.core.recommendation.service;

import com.yubit.api.core.recommendation.Recommendation;
import com.yubit.api.core.recommendation.RecommendationService;
import com.yubit.api.exceptions.InvalidInputException;
import com.yubit.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ServiceUtil serviceUtil;

    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        if(productId < 1){
            throw new InvalidInputException("Invalid product id" + productId);
        }

        if(productId == 113){
            LOGGER.debug("No recommendations found for productId {}", productId);
            return new ArrayList<>();
        }

        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(new Recommendation(productId, 1, "Author 1", 1, "", serviceUtil.getServiceAddress()));
        recommendations.add(new Recommendation(productId, 2, "Author 2", 2, "", serviceUtil.getServiceAddress()));
        recommendations.add(new Recommendation(productId, 3, "Author 3", 3, "", serviceUtil.getServiceAddress()));

        LOGGER.debug("/recommendation response size:{}", recommendations.size());
        return recommendations;
    }
}
