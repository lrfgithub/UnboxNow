package com.unboxnow.product.messaging;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.ProductItem;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import com.unboxnow.product.dto.ProductDTO;
import com.unboxnow.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class RedisRespondProcessor {

    private final ThreadPoolTaskExecutor threadTaskExecutor;

    private final ProductService productService;

    private final Producer producer;

    @Autowired
    public RedisRespondProcessor(ThreadPoolTaskExecutor threadTaskExecutor,
                                 ProductService productService,
                                 Producer producer) {
        this.threadTaskExecutor = threadTaskExecutor;
        this.productService = productService;
        this.producer = producer;
    }

    public void getProduct(Message request) {
        threadTaskExecutor.submit(() -> {
            RetrieverMessage<ProductItem> response = new RetrieverMessage<>(request.getEntityId());
            try {
                ProductDTO dto = productService.findById(request.getEntityId());
                response.setValid(true);
                response.setData(new ProductItem(
                        dto.getId(),
                        dto.generateAssembledTitle(),
                        dto.getSku(),
                        dto.generateFinalPrice()
                        ));
            } catch (NotFoundException ex) {
                response.setValid(false);
            }
            producer.publish(response, Topic.FETCH_PRODUCT, request.getId());
        });
    }
}
