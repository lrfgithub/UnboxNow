package com.unboxnow.inventory.constant;

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
            return "(.*)/inventory-addresses/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_SHIPMENT(Method.GET, Endpoint.ADDRESS, Attribute.SHIPMENT, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventory-addresses/shipment/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_SHIPMENT_FROM(Method.GET, Endpoint.ADDRESS, Attribute.SHIPMENT_FROM, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventory-addresses/shipment-from/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_SHIPMENT_TO(Method.GET, Endpoint.ADDRESS, Attribute.SHIPMENT_TO, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventory-addresses/shipment-to/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_WAREHOUSE(Method.GET, Endpoint.ADDRESS, Attribute.WAREHOUSE, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventory-addresses/warehouse/(\\d+)";
        }
    },

    FIND_ALL_INVENTORIES(Method.GET, Endpoint.INVENTORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_INVENTORY(Method.GET, Endpoint.INVENTORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventories/(\\d+)";
        }
    },
    FIND_INVENTORY_BY_WAREHOUSE(Method.GET, Endpoint.INVENTORY, Attribute.WAREHOUSE, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventories/warehouse/(\\d+)";
        }
    },
    FIND_INVENTORY_BY_SKU(Method.GET, Endpoint.INVENTORY, Attribute.SKU, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventories/sku/(.*)";
        }
    },
    CREATE_INVENTORY(Method.POST, Endpoint.INVENTORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_INVENTORY(Method.PUT, Endpoint.INVENTORY, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_INVENTORY(Method.PATCH, Endpoint.INVENTORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventories/(\\d+)";
        }
    },
    DELETE_INVENTORY(Method.DELETE, Endpoint.INVENTORY, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/inventories/(\\d+)";
        }
    },

    FIND_ALL_SHIPMENTS(Method.GET, Endpoint.SHIPMENT, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_SHIPMENT(Method.GET, Endpoint.SHIPMENT, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/shipments/(\\d+)";
        }
    },
    FIND_SHIPMENT_BY_DEPARTURE(Method.GET, Endpoint.SHIPMENT, Attribute.DEPARTURE, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/shipments/departure/(\\d+)";
        }
    },
    FIND_SHIPMENT_BY_DESTINATION(Method.GET, Endpoint.SHIPMENT, Attribute.DESTINATION, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/shipments/destination/(\\d+)";
        }
    },
    DELETE_SHIPMENT(Method.DELETE, Endpoint.SHIPMENT, Attribute.ID, Role.ADMIN) {
        @Override
        public String getPattern() {
            return "(.*)/shipments/(\\d+)";
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
            return "(.*)/shipment-items/(\\d+)";
        }
    },
    FIND_ITEM_BY_SHIPMENT(Method.GET, Endpoint.ITEM, Attribute.SHIPMENT, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/shipment-items/shipment/(\\d+)";
        }
    },
    FIND_ITEM_BY_SKU(Method.GET, Endpoint.ITEM, Attribute.SKU, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/shipment-items/sku/(.*)";
        }
    },

    FIND_ALL_WAREHOUSES(Method.GET, Endpoint.WAREHOUSE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_WAREHOUSE(Method.GET, Endpoint.WAREHOUSE, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/warehouses/(\\d+)";
        }
    },
    FIND_WAREHOUSE_BY_ADDRESS(Method.GET, Endpoint.WAREHOUSE, Attribute.ADDRESS, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/warehouses/address/(\\d+)";
        }
    },
    CREATE_WAREHOUSE(Method.POST, Endpoint.WAREHOUSE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_WAREHOUSE(Method.PUT, Endpoint.WAREHOUSE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_WAREHOUSE(Method.PATCH, Endpoint.WAREHOUSE, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/warehouses/(\\d+)";
        }
    },
    DELETE_WAREHOUSE(Method.DELETE, Endpoint.WAREHOUSE, Attribute.ID, Role.ADMIN) {
        @Override
        public String getPattern() {
            return "(.*)/warehouses/(\\d+)";
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

        ADDRESS {
            @Override
            public String getPattern() {
                return "(.*)/inventory-addresses(.*)";
            }
        },
        INVENTORY {
            @Override
            public String getPattern() {
                return "(.*)/inventories(.*)";
            }
        },
        SHIPMENT {
            @Override
            public String getPattern() {
                return "(.*)/shipments(.*)";
            }
        },
        ITEM {
            @Override
            public String getPattern() {
                return "(.*)/shipment-items(.*)";
            }
        },
        WAREHOUSE {
            @Override
            public String getPattern() {
                return "(.*)/warehouses(.*)";
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

        SHIPMENT {
            @Override
            public String getPattern() {
                return "(.*)/shipment(.*)";
            }
        },
        SHIPMENT_FROM {
            @Override
            public String getPattern() {
                return "(.*)/shipment-from(.*)";
            }
        },
        SHIPMENT_TO {
            @Override
            public String getPattern() {
                return "(.*)/shipment-to(.*)";
            }
        },
        WAREHOUSE {
            @Override
            public String getPattern() {
                return "(.*)/warehouse(.*)";
            }
        },
        SKU {
            @Override
            public String getPattern() {
                return "(.*)/sku(.*)";
            }
        },
        DEPARTURE {
            @Override
            public String getPattern() {
                return "(.*)/departure(.*)";
            }
        },
        DESTINATION {
            @Override
            public String getPattern() {
                return "(.*)/destination(.*)";
            }
        },
        ADDRESS {
            @Override
            public String getPattern() {
                return "(.*)/address(.*)";
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
