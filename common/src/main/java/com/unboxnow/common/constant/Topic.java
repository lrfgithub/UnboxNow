package com.unboxnow.common.constant;

public enum Topic {

    FETCH_PRODUCT(Module.CART, Module.PRODUCT, TopicType.REQUEST, TopicTool.REDIS),
    UPDATE_PRODUCT(Module.PRODUCT, Module.CART, TopicType.UPDATE, TopicTool.REDIS),
    FETCH_MEMBER(Module.CART, Module.USER, TopicType.REQUEST, TopicTool.REDIS),
    UPDATE_MEMBER(Module.USER, Module.CART, TopicType.UPDATE, TopicTool.REDIS),
    FETCH_ADDRESS(Module.ORDER, Module.USER, TopicType.REQUEST, TopicTool.KAFKA),
    CHECK_QUANTITY(Module.CART, Module.INVENTORY, TopicType.REQUEST, TopicTool.REDIS),
    CHECK_OUT(Module.CART, Module.ORDER, TopicType.REQUEST, TopicTool.KAFKA),
    LOCK_QUANTITY(Module.ORDER, Module.INVENTORY, Infix.LOCK, TopicType.REQUEST, TopicTool.KAFKA),
    START_SHIPMENT(Module.ORDER, Module.INVENTORY, Infix.SHIPMENT, TopicType.REQUEST, TopicTool.KAFKA),
    START_LOCKING(Infix.LOCK, TopicType.UPDATE, TopicTool.REDIS),
    STOP_LOCKING(Module.INVENTORY, Module.ORDER, TopicType.UPDATE, TopicTool.KAFKA);

    private final Module sender;

    private final Module receiver;

    private final Infix infix;

    public final TopicType topicType;

    public final TopicTool tool;

    Topic(Infix infix, TopicType topicType, TopicTool tool) {
        this.sender = null;
        this.receiver = null;
        this.infix = infix;
        this.topicType = topicType;
        this.tool = tool;
    }

    Topic(Module sender, Module receiver, TopicType topicType, TopicTool tool) {
        this.sender = sender;
        this.receiver = receiver;
        this.infix = null;
        this.topicType = topicType;
        this.tool = tool;
    }

    Topic(Module sender, Module receiver, Infix infix, TopicType topicType, TopicTool tool) {
        this.sender = sender;
        this.receiver = receiver;
        this.infix = infix;
        this.topicType = topicType;
        this.tool = tool;
    }

    public String getName() {
        String senderStr = sender == null? "" : sender + "_";
        String receiverStr = receiver == null? "" : receiver + "_";
        String infixStr = infix == null? "" : infix + "_";
        return senderStr + "TO_" + receiverStr + infixStr + topicType;
    }

    public String getResponseName() {
        if (topicType != TopicType.REQUEST) return null;
        String senderStr = receiver == null? "" : receiver + "_";
        String receiverStr = sender == null? "" : sender + "_";
        String infixStr = infix == null? "" : infix + "_";
        return senderStr + "TO_" + receiverStr + infixStr + TopicType.RESPONSE;
    }

    public String getCounter() {
        String senderStr = sender == null? "" : sender + "_";
        String receiverStr = receiver == null? "" : receiver + "_";
        String infixStr = infix == null? "" : infix + "_";
        return senderStr + "TO_" + receiverStr + infixStr + "COUNTER";
    }

    public enum Module {
        PRODUCT, CART, INVENTORY, USER, ORDER
    }

    private enum Infix {
        SHIPMENT, LOCK
    }

    public enum TopicType {
        REQUEST, RESPONSE, UPDATE
    }

    public enum TopicTool {
        REDIS, KAFKA
    }

}
