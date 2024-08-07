package com.yubit.microservices.core.product.services;

import com.yubit.api.core.product.Product;
import com.yubit.api.core.product.ProductService;
import com.yubit.api.exceptions.InvalidInputException;
import com.yubit.api.exceptions.NotFoundException;
import com.yubit.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller product service que responde al llamado de Product Composite Integration
 */
@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public ProductServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }
    @Override
    public Product getProduct(int productId) {
        LOGGER.debug("/product return the found product for productId={}", productId);
        if(productId < 1){
            throw new InvalidInputException("Invalid productId:" + productId);
        }

        // si no se encontro el product
        if(productId == 13){
            throw new NotFoundException("No product found for productId:" + productId);
        }

        return new Product(productId, "name-"+productId, 123, serviceUtil.getServiceAddress());
    }
}
