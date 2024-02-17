package com.unboxnow.cart.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Authorization {

    FIND_BY_MEMBER(Method.GET, Endpoint.CART, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/carts/(\\d+)";
        }
    },
    ADD_ITEM(Method.POST, Endpoint.CART, Attribute.ADD, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/carts/add(.*)";
        }
    },
    SUB_ITEM(Method.POST, Endpoint.CART, Attribute.SUB, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/carts/sub(.*)";
        }
    },
    PLACE_ORDER(Method.POST, Endpoint.CART, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return FIND_BY_MEMBER.getPattern();
        }
    },
    DELETE_ITEM(Method.DELETE, Endpoint.CART, null, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    DELETE_ITEMS_BY_MEMBER(Method.DELETE, Endpoint.CART, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return FIND_BY_MEMBER.getPattern();
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
        boolean containsRole = roles.contains(authorization.getRole().getValue());
        return switch (authorization) {
            case FIND_BY_MEMBER, PLACE_ORDER, DELETE_ITEMS_BY_MEMBER -> {
                if (!containsRole) yield false;
                if (!Role.isCustomer(roles)) yield true;
                String value = getValueFromUrl(authorization, url);
                yield value != null && memberId == Integer.parseInt(value);
            }
            default -> containsRole;
        };
    }

    public static String getValueFromUrl(Authorization authorization, String url) {
        Pattern r = Pattern.compile(authorization.getPattern());
        Matcher matcher = r.matcher(url);
        return matcher.find()? matcher.group(2) : null;
    }

    public static boolean isCalibrationRequired(Authorization authorization) {
        if (authorization == null) return false;
        return switch (authorization) {
            case ADD_ITEM, SUB_ITEM, DELETE_ITEM -> true;
            default -> false;
        };
    }

    public enum Method {
        GET, PUT, POST, PATCH, DELETE;

        public static Method fromString(String str) {
            return Method.valueOf(str.replaceAll("\\s+", "").toUpperCase());
        }
    }

    public enum Endpoint {

        CART {
            @Override
            public String getPattern() {
                return "(.*)/carts(.*)";
            }
        };

        public abstract String getPattern();

        public static Endpoint fromString(String str) {
            if (str.isBlank()) return null;
            else if (str.matches(CART.getPattern())) return CART;
            return null;
        }
    }

    public enum Attribute {

        ADD {
            @Override
            public String getPattern() {
                return "(.*)/add(.*)";
            }
        },
        SUB {
            @Override
            public String getPattern() {
                return "(.*)/sub(.*)";
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
