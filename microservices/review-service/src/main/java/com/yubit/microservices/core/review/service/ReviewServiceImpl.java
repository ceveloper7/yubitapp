package com.yubit.microservices.core.review.service;

import com.yubit.api.core.review.Review;
import com.yubit.api.core.review.ReviewService;
import com.yubit.api.exceptions.InvalidInputException;
import com.yubit.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ServiceUtil serviceUtil;

    public ReviewServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if(productId < 1){
            throw new InvalidInputException("Invalid product id " + productId);
        }

        if(productId == 113){
            LOGGER.debug("No reviews found for productId {} ", productId);
            return new ArrayList<>();
        }

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
        reviews.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
        reviews.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

        LOGGER.debug("/reviews response size {}", reviews.size());
        return reviews;
    }
}
