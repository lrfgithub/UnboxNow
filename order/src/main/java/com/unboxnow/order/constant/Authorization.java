package com.unboxnow.order.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Authorization {

    FIND_ALL_ADDRESSES(Method.GET, Endpoint.ADDRESS, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_ADDRESS(Method.GET, Endpoint.ADDRESS, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/order-addresses/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_ORDER(Method.GET, Endpoint.ADDRESS, Attribute.ORDER, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/order-addresses/order/(\\d+)";
        }
    },

    FIND_ALL_ORDERS(Method.GET, Endpoint.ORDER, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_ORDER(Method.GET, Endpoint.ORDER, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/orders/(\\d+)";
        }
    },
    FIND_ORDER_BY_ADDRESS(Method.GET, Endpoint.ORDER, Attribute.ADDRESS, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/orders/address/(\\d+)";
        }
    },
    FIND_ORDER_BY_MEMBER(Method.GET, Endpoint.ORDER, Attribute.MEMBER, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/orders/member/(\\d+)";
        }
    },
    SUBMIT_ADDRESS(Method.POST, Endpoint.ORDER, null, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    SUBMIT_PAYMENT(Method.POST, Endpoint.ORDER, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return FIND_ORDER.getPattern();
        }
    },
    CANCEL_ORDER(Method.PUT, Endpoint.ORDER, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return FIND_ORDER.getPattern();
        }
    },

    FIND_ALL_ITEMS(Method.GET, Endpoint.ITEM, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_ITEM(Method.GET, Endpoint.ITEM, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/order-items/(\\d+)";
        }
    },
    FIND_ITEM_BY_ORDER(Method.GET, Endpoint.ITEM, Attribute.ORDER, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/order-items/order/(\\d+)";
        }
    },
    FIND_ITEM_BY_SKU(Method.GET, Endpoint.ITEM, Attribute.SKU, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/order-items/sku/(.*)";
        }
    },
    DEACTIVATE_ITEM(Method.PUT, Endpoint.ITEM, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_ITEM.getPattern();
        }
    };

    private final Method method;

    private final Endpoint endpoint;

    private final Attribute attribute;

    private final Role role;

    Authorization(Method method, Endpoint endpoint, Attribute attribute, Role role) {
        this.method = method;
        this.endpoint = endpoint;
        this.attribute = attribute;
        this.role = role;
    }

    public Method getMethod() {
        return method;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Role getRole() {
        return role;
    }

    public abstract String getPattern();

    private static final Map<Map<String, String>, Authorization> authMap;

    static {
        Map<Map<String, String>, Authorization> map = new HashMap<>();
        for (Authorization auth : Authorization.values()) {
            Map<String, String> props = new HashMap<>();
            props.put("Method", auth.getMethod().toString());
            props.put("Endpoint", auth.getEndpoint().toString());
            if (auth.getAttribute() != null) {
                props.put("attribute", auth.getAttribute().toString());
            }
            map.put(props, auth);
        }
        authMap = Collections.unmodifiableMap(map);
    }

    public static Authorization getAuthorization(String methodStr, String url) {
        Endpoint endpoint = Endpoint.fromString(url);
        Method method = Method.fromString(methodStr);
        if (endpoint == null || method == null) return null;
        Pattern r = Pattern.compile(endpoint.getPattern());
        Matcher matcher = r.matcher(url);
        String suffix;
        Attribute attribute = null;
        if (matcher.find()) {
            suffix = matcher.group(2);
            attribute = Attribute.fromString(suffix);
        }
        Map<String, String> props = new HashMap<>();
        props.put("Method", method.toString());
        props.put("Endpoint", endpoint.toString());
        if (attribute != null) {
            props.put("attribute", attribute.toString());
        }
        return authMap.get(props);
    }

    public static boolean isAuthorized(String methodStr, String url, int memberId, List<String> roles) {
        Authorization authorization = getAuthorization(methodStr, url);
        if (authorization == null) return false;
        if (authorization.getRole() == null) return true;
        boolean containsRole = roles.contains(authorization.getRole().getValue());
        if (authorization != FIND_ORDER_BY_MEMBER) {
            return containsRole;
        }
        if (!containsRole) return false;
        if (!Role.isCustomer(roles)) return true;
        String value = getValueFromUrl(authorization, url);
        return value != null && memberId == Integer.parseInt(value);
    }

    public static String getValueFromUrl(Authorization authorization, String url) {
        Pattern r = Pattern.compile(authorization.getPattern());
        Matcher matcher = r.matcher(url);
        return matcher.find()? matcher.group(2) : null;
    }

    public static boolean isCalibrationRequired(Authorization authorization) {
        return authorization == SUBMIT_ADDRESS;
    }

    public static int getCalibrationCode(Authorization authorization, boolean isCustomer) {
        if (authorization == null) return 0;
        return (authorization == SUBMIT_ADDRESS && isCustomer)? 1 : 0;
    }

    public enum Method {
        GET, PUT, POST, PATCH, DELETE;

        public static Method fromString(String str) {
            return Method.valueOf(str.replaceAll("\\s+", "").toUpperCase());
        }
    }

    public enum Endpoint {

        ADDRESS {
            @Override
            public String getPattern() {
                return "(.*)/order-addresses(.*)";
            }
        },
        ORDER {
            @Override
            public String getPattern() {
                return "(.*)/orders(.*)";
            }
        },
        ITEM {
            @Override
            public String getPattern() {
                return "(.*)/order-items(.*)";
            }
        };

        public abstract String getPattern();

        public static Endpoint fromString(String str) {
            if (str.isBlank()) return null;
            for (Endpoint endpoint : Endpoint.values()) {
                if (str.matches(endpoint.getPattern())) return endpoint;
            }
            return null;
        }
    }

    public enum Attribute {

        ORDER {
            @Override
            public String getPattern() {
                return "(.*)/order(.*)";
            }
        },
        ADDRESS {
            @Override
            public String getPattern() {
                return "(.*)/address(.*)";
            }
        },
        MEMBER {
            @Override
            public String getPattern() {
                return "(.*)/member(.*)";
            }
        },
        SKU {
            @Override
            public String getPattern() {
                return "(.*)/sku(.*)";
            }
        },
        ID {
            @Override
            public String getPattern() {
                return "(.*)/(\\d+)";
            }
        };

        public abstract String getPattern();

        public static Attribute fromString(String suffix) {
            if (suffix.isEmpty()) return null;
            for (Attribute attribute : Attribute.values()) {
                if (suffix.matches(attribute.getPattern())) return attribute;
            }
            return null;
        }
    }

    public enum Role {

        ADMIN {
            @Override
            public String getValue() {
                return "admin";
            }
        },
        OPERATOR {
            @Override
            public String getValue() {
                return "operator";
            }
        },
        CUSTOMER {
            @Override
            public String getValue() {
                return "customer";
            }
        };

        public abstract String getValue();

        public static boolean isCustomer(List<String> roles) {
            if (roles.isEmpty()) return false;
            if (roles.size() > 1) return false;
            return roles.contains(CUSTOMER.getValue());
        }
    }
}
