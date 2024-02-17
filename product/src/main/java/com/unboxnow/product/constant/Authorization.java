package com.unboxnow.product.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Authorization {

    // for brand controller
    FIND_ALL_BRANDS(Method.GET, Endpoint.BRAND, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_BRAND(Method.GET, Endpoint.BRAND, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/brands/(\\d+)";
        }
    },
    CREATE_BRAND(Method.POST, Endpoint.BRAND, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_BRAND(Method.PUT, Endpoint.BRAND, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_BRAND(Method.PATCH, Endpoint.BRAND, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_BRAND.getPattern();
        }
    },
    DELETE_BRAND(Method.PATCH, Endpoint.BRAND, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_BRAND.getPattern();
        }
    },

    // for category controller
    FIND_ALL_CATEGORIES(Method.GET, Endpoint.CATEGORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_CATEGORY(Method.GET, Endpoint.CATEGORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/categories/(\\d+)";
        }
    },
    CREATE_CATEGORY(Method.POST, Endpoint.CATEGORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_CATEGORY(Method.PUT, Endpoint.CATEGORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_CATEGORY(Method.PATCH, Endpoint.CATEGORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_CATEGORY.getPattern();
        }
    },
    DELETE_CATEGORY(Method.PATCH, Endpoint.CATEGORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_CATEGORY.getPattern();
        }
    },

    // for discount controller
    FIND_ALL_DISCOUNTS(Method.GET, Endpoint.DISCOUNT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_DISCOUNT(Method.GET, Endpoint.DISCOUNT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/discounts/(\\d+)";
        }
    },
    CREATE_DISCOUNT(Method.POST, Endpoint.DISCOUNT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_DISCOUNT(Method.PUT, Endpoint.DISCOUNT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_DISCOUNT(Method.PATCH, Endpoint.DISCOUNT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_DISCOUNT.getPattern();
        }
    },
    DELETE_DISCOUNT(Method.PATCH, Endpoint.DISCOUNT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_DISCOUNT.getPattern();
        }
    },

    // for product controller
    FIND_ALL_PRODUCTS(Method.GET, Endpoint.PRODUCT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_PRODUCT(Method.GET, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/products/(\\d+)";
        }
    },
    FIND_PRODUCT_BY_Brand(Method.GET, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/products/brand/(\\d+)";
        }
    },
    FIND_PRODUCT_BY_CATEGORY(Method.GET, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/products/category/(\\d+)";
        }
    },
    FIND_PRODUCT_BY_DISCOUNT(Method.GET, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/products/discount/(\\d+)";
        }
    },
    FIND_PRODUCT_BY_Sku(Method.GET, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/products/sku/(.*)";
        }
    },
    CREATE_PRODUCT(Method.POST, Endpoint.PRODUCT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_PRODUCT(Method.PUT, Endpoint.PRODUCT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_PRODUCT(Method.PATCH, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_PRODUCT.getPattern();
        }
    },
    DELETE_PRODUCT(Method.PATCH, Endpoint.PRODUCT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_PRODUCT.getPattern();
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

    public static boolean isAuthorized(String methodStr, String url, List<String> roles) {
        Authorization authorization = getAuthorization(methodStr, url);
        if (authorization == null) return false;
        if (authorization.getRole() == null) return true;
        return roles.contains(authorization.getRole().getValue());
    }

    public static String getValueFromUrl(Authorization authorization, String url) {
        Pattern r = Pattern.compile(authorization.getPattern());
        Matcher matcher = r.matcher(url);
        return matcher.find()? matcher.group(2) : null;
    }

    public enum Method {
        GET, PUT, POST, PATCH, DELETE;

        public static Method fromString(String str) {
            return Method.valueOf(str.replaceAll("\\s+", "").toUpperCase());
        }
    }

    public enum Endpoint {

        BRAND {
            @Override
            public String getPattern() {
                return "(.*)/brands(.*)";
            }
        },
        CATEGORY {
            @Override
            public String getPattern() {
                return "(.*)/categories(.*)";
            }
        },
        DISCOUNT {
            @Override
            public String getPattern() {
                return "(.*)/discounts(.*)";
            }
        },
        PRODUCT {
            @Override
            public String getPattern() {
                return "(.*)/products(.*)";
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

        BRAND {
            @Override
            public String getPattern() {
                return "(.*)/brand(.*)";
            }
        },
        CATEGORY {
            @Override
            public String getPattern() {
                return "(.*)/category(.*)";
            }
        },
        DISCOUNT {
            @Override
            public String getPattern() {
                return "(.*)/discount(.*)";
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
