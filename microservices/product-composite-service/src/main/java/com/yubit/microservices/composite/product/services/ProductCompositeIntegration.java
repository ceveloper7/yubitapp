package com.yubit.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubit.api.core.product.Product;
import com.yubit.api.core.product.ProductService;
import com.yubit.api.core.recommendation.Recommendation;
import com.yubit.api.core.recommendation.RecommendationService;
import com.yubit.api.core.review.Review;
import com.yubit.api.core.review.ReviewService;
import com.yubit.api.exceptions.InvalidInputException;
import com.yubit.api.exceptions.NotFoundException;
import com.yubit.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Capa de integracion que permite llamar a los otros 3 servicios
 */
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    // ResTemplate permite llamar a otros servicios
    private final RestTemplate restTemplate;

    // Jackson nos permite trabajar con objetos JSON
    private final ObjectMapper objectMapper;

    // url de los diferentes servicios
    private final String productServiceUrl;
    private final String recommedationServiceUrl;
    private final String reviewServiceUrl;

    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") String productServicePort,
            @Value("${app.recommendation-service.host}") String recommedationServiceHost,
            @Value("${app.recommendation-service.port}") String recommedationServicePort,
            @Value("${app.review-service.port}") String reviewServiceHost,
            @Value("${app.review-service.host}") String reviewServicePort
    ){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.recommedationServiceUrl = "http://" + recommedationServiceHost + ":" + productServicePort + "/recommendation?productId=";
        this.reviewServiceUrl = "htpp://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
    }

    private String getErrorMessage(HttpClientErrorException ex){
        try{
            // leemos la respuesta y llenamos el objeto HttpErrorInfo
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch(IOException e){
            return e.getMessage();
        }
    }

    @Override
    public Product getProduct(Long productId) {
        try{
            // construimos el url para product
            String url = productServiceUrl + productId;
            LOGGER.debug("will call getProduct API on URL: {}", url);

            // indicamos al restTemplate que retorne un objeto Product
            Product product = restTemplate.getForObject(url, Product.class);
            LOGGER.debug("Found a product with id: {}", product.id());
            return product;
        }
        catch(HttpClientErrorException ex){
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundException(getErrorMessage(ex));
            }

            if(ex.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)){
                throw new InvalidInputException(getErrorMessage(ex));
            }

            LOGGER.warn("Got an unexpected HTTP error {}, will rethrow it", ex.getStatusCode());
            LOGGER.warn("Error body: {}", ex.getResponseBodyAsString());
            throw ex;
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return null;
    }

    @Override
    public List<Review> getReviews(int productId) {
        return null;
    }
}
