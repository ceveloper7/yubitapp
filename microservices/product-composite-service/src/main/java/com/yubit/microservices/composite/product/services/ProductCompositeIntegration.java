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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Componente de integracion que permite llamar a los otros 3 servicios
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
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") int recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort
    ){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
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

    // metodo que llama a product-service
    @Override
    public Product getProduct(int productId) {
        try{
            // construimos el url para product
            String url = this.productServiceUrl + productId;
            LOGGER.debug("will call getProduct API on URL: {}", url);

            // indicamos al restTemplate que retorne un objeto Product
            Product product = restTemplate.getForObject(url, Product.class);
            LOGGER.debug("Found a product with id: {}", product.productId());
            return product;
        }
        catch(HttpClientErrorException ex){
            switch (Objects.requireNonNull(HttpStatus.resolve(ex.getStatusCode().value()))) {
                case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
                case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));
                default -> throw ex;
            }

//            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
//                throw new NotFoundException(getErrorMessage(ex));
//            }
//
//            if(ex.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)){
//                throw new InvalidInputException(getErrorMessage(ex));
//            }

//            LOGGER.warn("Got an unexpected HTTP error {}, will rethrow it", ex.getStatusCode());
//            LOGGER.warn("Error body: {}", ex.getResponseBodyAsString());
//            throw ex;
        }
    }

    // metodo que llama a recommendation service
    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try{
            String url = recommendationServiceUrl + productId;
            LOGGER.debug("will call getRecommendations API on URL: {}", url);
            // hacemos un GET al url para obtener una lista de recomendaciones
            List<Recommendation> recommendations = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {
            }).getBody();
            LOGGER.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
            return recommendations;
        }
        catch(Exception ex){
            LOGGER.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    // metodo que llama a review service
    @Override
    public List<Review> getReviews(int productId) {
        try{
            String url = reviewServiceUrl + productId;
            LOGGER.debug("will call getReviews API on URL: {}", url);
            List<Review> reviews = restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>(){})
                    .getBody();
            LOGGER.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;
        }
        catch(Exception ex){
            LOGGER.warn("Got an exception while requesting reviews, return zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }
}
