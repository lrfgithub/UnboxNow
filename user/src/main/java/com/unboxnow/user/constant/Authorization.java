package com.unboxnow.user.constant;

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
            return "(.*)/user-addresses/(\\d+)";
        }
    },
    FIND_ADDRESS_BY_MEMBER(Method.GET, Endpoint.ADDRESS, Attribute.MEMBER, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/user-addresses/member/(\\d+)";
        }
    },
    CREATE_ADDRESS(Method.POST, Endpoint.ADDRESS, null, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_ADDRESS(Method.PUT, Endpoint.ADDRESS, null, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    DELETE_ADDRESS(Method.DELETE, Endpoint.ADDRESS, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return FIND_ADDRESS.getPattern();
        }
    },

    FIND_ALL_MEMBERS(Method.GET, Endpoint.MEMBER, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_MEMBER(Method.GET, Endpoint.MEMBER, Attribute.ID, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/members/(\\d+)";
        }
    },
    FIND_MEMBER_BY_USERNAME(Method.GET, Endpoint.MEMBER, Attribute.USERNAME, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/members/username/(.*)";
        }
    },
    LOGIN(Method.POST, Endpoint.MEMBER, Attribute.LOGIN, null) {
        @Override
        public String getPattern() {
            return "(.*)/members/login(.*)";
        }
    },
    REGISTER(Method.POST, Endpoint.MEMBER, Attribute.REGISTER, null) {
        @Override
        public String getPattern() {
            return "(.*)/members/register(.*)";
        }
    },
    UPDATE_MEMBER(Method.PUT, Endpoint.MEMBER, null, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_PASSWORD(Method.POST, Endpoint.MEMBER, Attribute.UPDATE_PASSWORD, Role.CUSTOMER) {
        @Override
        public String getPattern() {
            return "(.*)/members/update-password(.*)";
        }
    },
    FORGET_PASSWORD(Method.POST, Endpoint.MEMBER, Attribute.FORGET_PASSWORD, null) {
        @Override
        public String getPattern() {
            return "(.*)/members/forget-password/(.*)";
        }
    },
    RESET_PASSWORD(Method.POST, Endpoint.MEMBER, Attribute.RESET_PASSWORD, null) {
        @Override
        public String getPattern() {
            return "(.*)/members/reset-password/(.*)";
        }
    },
    DEACTIVATE_MEMBER(Method.PATCH, Endpoint.MEMBER, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_MEMBER.getPattern();
        }
    },
    DELETE_MEMBER(Method.DELETE, Endpoint.MEMBER, Attribute.ID, Role.ADMIN) {
        @Override
        public String getPattern() {
            return FIND_MEMBER.getPattern();
        }
    },

    FIND_ALL_MEMBER_ROLES(Method.GET, Endpoint.MEMBER_ROLE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_MEMBER_ROLE(Method.GET, Endpoint.MEMBER_ROLE, Attribute.MEMBER_ROLE_ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/member-roles/member-role-id(.*)";
        }
    },
    FIND_MEMBER_ROLE_BY_MEMBER(Method.GET, Endpoint.MEMBER_ROLE, Attribute.MEMBER, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/member-roles/member/(\\d+)";
        }
    },
    FIND_MEMBER_ROLE_BY_ROLE(Method.GET, Endpoint.MEMBER_ROLE, Attribute.ROLE, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/member-roles/role/(\\d+)";
        }
    },
    CREATE_MEMBER_ROLE(Method.POST, Endpoint.MEMBER_ROLE, null, Role.ADMIN) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    DELETE_MEMBER_ROLE(Method.DELETE, Endpoint.MEMBER_ROLE, null, Role.ADMIN) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },

    FIND_ALL_ROLES(Method.GET, Endpoint.ROLE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    FIND_ROLE(Method.GET, Endpoint.ROLE, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/roles/(\\d+)";
        }
    },
    FIND_ROLE_BY_TITLE(Method.GET, Endpoint.ROLE, Attribute.TITLE, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return "(.*)/roles/title/(.*)";
        }
    },
    CREATE_ROLE(Method.POST, Endpoint.ROLE, null, Role.ADMIN) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    UPDATE_ROLE(Method.PUT, Endpoint.ROLE, null, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return getEndpoint().getPattern();
        }
    },
    PATCH_ROLE(Method.PATCH, Endpoint.ROLE, Attribute.ID, Role.OPERATOR) {
        @Override
        public String getPattern() {
            return FIND_ROLE.getPattern();
        }
    },
    DELETE_ROLE(Method.DELETE, Endpoint.ROLE, Attribute.ID, Role.ADMIN) {
        @Override
        public String getPattern() {
            return FIND_ROLE.getPattern();
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
        return switch (authorization) {
            case FIND_ADDRESS_BY_MEMBER, FIND_MEMBER -> {
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
            case CREATE_ADDRESS, UPDATE_ADDRESS, UPDATE_MEMBER, UPDATE_PASSWORD, RESET_PASSWORD -> true;
            default -> false;
        };
    }

    public static int getCalibrationCode(Authorization authorization, boolean isCustomer) {
        if (authorization == null) return 0;
        return switch (authorization) {
            case UPDATE_PASSWORD, RESET_PASSWORD -> 1;
            case UPDATE_MEMBER -> isCustomer? 2 : 0;
            case CREATE_ADDRESS, UPDATE_ADDRESS -> isCustomer? 3 : 0;
            default -> 0;
        };
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
                return "(.*)/user-addresses(.*)";
            }
        },
        MEMBER {
            @Override
            public String getPattern() {
                return "(.*)/members(.*)";
            }
        },
        MEMBER_ROLE {
            @Override
            public String getPattern() {
                return "(.*)/member-roles(.*)";
            }
        },
        ROLE {
            @Override
            public String getPattern() {
                return "(.*)/roles(.*)";
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

        LOGIN {
            @Override
            public String getPattern() {
                return "(.*)/login(.*)";
            }
        },
        REGISTER {
            @Override
            public String getPattern() {
                return "(.*)/register(.*)";
            }
        },
        UPDATE_PASSWORD {
            @Override
            public String getPattern() {
                return "(.*)/update-password(.*)";
            }
        },
        FORGET_PASSWORD {
            @Override
            public String getPattern() {
                return "(.*)/forget-password(.*)";
            }
        },
        RESET_PASSWORD {
            @Override
            public String getPattern() {
                return "(.*)/reset-password(.*)";
            }
        },
        MEMBER {
            @Override
            public String getPattern() {
                return "(.*)/member(.*)";
            }
        },
        MEMBER_ROLE_ID {
            @Override
            public String getPattern() {
                return "(.*)/member-role-id(.*)";
            }
        },
        ROLE {
            @Override
            public String getPattern() {
                return "(.*)/role(.*)";
            }
        },
        USERNAME {
            @Override
            public String getPattern() {
                return "(.*)/username(.*)";
            }
        },
        TITLE {
            @Override
            public String getPattern() {
                return "(.*)/title(.*)";
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
