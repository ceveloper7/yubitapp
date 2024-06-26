package com.yubit.api.core.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * esta interface se llama para obtener la informacion de un producto agregado
 */
public interface ProductCompositeService {
    @GetMapping(
            value = "/product-composite/{productId}",
            produces = "application/json"
    )
    ProductAggregate getProduct(@PathVariable int productId);
}
