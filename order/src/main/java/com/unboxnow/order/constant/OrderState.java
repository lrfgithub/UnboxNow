package com.unboxnow.order.constant;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum OrderState {

    CANCELLING {
        @Override
        public OrderState nextState() {
            return this;
        }

        @Override
        public String getValue() {
            return "Order cancelled";
        }
    },
    CONFIRMING {
        @Override
        public OrderState nextState() {
            return PREPARING;
        }

        @Override
        public String getValue() {
            return "Confirming order";
        }
    },
    PREPARING {
        @Override
        public OrderState nextState() {
            return SHIPMENT;
        }

        @Override
        public String getValue() {
            return "Preparing Order";
        }
    },
    SHIPMENT {
        @Override
        public OrderState nextState() {
            return TRANSIT;
        }

        @Override
        public String getValue() {
            return "Start shipment";
        }
    },
    TRANSIT {
        @Override
        public OrderState nextState() {
            return COMPLETED;
        }

        @Override
        public String getValue() {
            return "In transit";
        }
    },
    COMPLETED {
        @Override
        public OrderState nextState() {
            return this;
        }

        @Override
        public String getValue() {
            return "Order completed";
        }
    };

    public abstract OrderState nextState();

    public abstract String getValue();

    private static final Map<String, OrderState> stateMap;

    static {
        Map<String, OrderState> map = new ConcurrentHashMap<>(8);
        for (OrderState state : OrderState.values()) {
            String key = state.getValue().replaceAll("\\s+", "").toLowerCase();
            map.put(key, state);
        }
        stateMap = Collections.unmodifiableMap(map);
    }

    public OrderState abort() {
        return CANCELLING;
    }

    public static OrderState fromValue(String value) {
        String key = value.replaceAll("\\s+", "").toLowerCase();
        return stateMap.get(key);
    }
}
