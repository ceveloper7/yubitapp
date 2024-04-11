package com.yubit.microservices.core.product.services;

import com.yubit.api.core.product.Product;
import com.yubit.api.core.product.ProductService;
import com.yubit.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public ProductServiceImpl(ServiceUtil serviceUtil){
        this.serviceUtil = serviceUtil;
    }
    @Override
    public Product getProduct(Long productId) {
        return new Product(productId, "name-"+productId, 123, serviceUtil.getServiceAddress());
    }
}
