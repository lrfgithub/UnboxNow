package com.unboxnow.user.messaging;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.exception.IllegalMessageException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.CarrierMessage;
import com.unboxnow.common.message.RetrieverMessage;
import com.unboxnow.user.dto.AddressDTO;
import com.unboxnow.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaRequestProcessor {

    private final AddressService addressService;

    private final Producer producer;

    @Autowired
    public KafkaRequestProcessor(AddressService addressService, Producer producer) {
        this.addressService = addressService;
        this.producer = producer;
    }

    public void getAddress(CarrierMessage<Integer> request) {
        RetrieverMessage<UniversalAddress> response = new RetrieverMessage<>(request.getEntityId());
        try {
            Integer addressId = request.getData();
            if (addressId == null) {
                throw new IllegalMessageException(Topic.FETCH_ADDRESS.getName(), request.getId(), "data");
            }
            AddressDTO dto = addressService.findById(addressId);
            response.setValid(true);
            UniversalAddress address = new UniversalAddress(
                    dto.getName(),
                    dto.getMobile(),
                    dto.getEmail(),
                    dto.getAddress1(),
                    dto.getAddress2(),
                    dto.getCity(),
                    dto.getState(),
                    dto.getZip()
            );
            response.setData(address);
        } catch (NotFoundException ex) {
            response.setValid(false);
        }
        producer.publish(response, Topic.FETCH_ADDRESS, request.getId());
    }
}
