package com.unboxnow.user.messaging;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import com.unboxnow.user.dto.MemberDTO;
import com.unboxnow.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class RedisRespondProcessor {

    private final ThreadPoolTaskExecutor threadTaskExecutor;

    private final MemberService memberService;

    private final Producer producer;

    @Autowired
    public RedisRespondProcessor(ThreadPoolTaskExecutor threadTaskExecutor,
                                 MemberService memberService,
                                 Producer producer) {
        this.threadTaskExecutor = threadTaskExecutor;
        this.memberService = memberService;
        this.producer = producer;
    }

    public void getMember(Message request) {
        threadTaskExecutor.submit(() -> {
            RetrieverMessage<Boolean> response = new RetrieverMessage<>(request.getEntityId());
            try {
                MemberDTO dto = memberService.findById(request.getEntityId());
                response.setValid(true);
                response.setData(dto.getActive());
            } catch (NotFoundException ex) {
                response.setValid(false);
            }
            producer.publish(response, Topic.FETCH_MEMBER, request.getId());
        });
    }
}
