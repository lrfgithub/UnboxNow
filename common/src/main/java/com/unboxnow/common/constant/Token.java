package com.unboxnow.common.constant;

public enum Token {

    ACCESS {
        @Override
        public String getSecret() {
            return "UnboxNowAccess";
        }

        @Override
        public String getHeaderKey() {
            return "Access-Token";
        }

        @Override
        public int getTokenExpiry() {
            return 15;
        }

        @Override
        public int getRedisExpiry() {
            return 65;
        }
    },

    REFRESH {
        @Override
        public String getSecret() {
            return "UnboxNowRefresh";
        }

        @Override
        public String getHeaderKey() {
            return "Refresh-Token";
        }

        @Override
        public int getTokenExpiry() {
            return 60;
        }

        @Override
        public int getRedisExpiry() {
            return 65;
        }
    },

    RESET {
        @Override
        public String getSecret() {
            return "UnboxNowReset";
        }

        @Override
        public String getHeaderKey() {
            return "Reset-Token";
        }

        @Override
        public int getTokenExpiry() {
            return 30;
        }

        @Override
        public int getRedisExpiry() {
            return 35;
        }
    };

    public abstract String getSecret();

    public abstract String getHeaderKey();

    public String getRedisKey(int memberId) {
        return getHeaderKey() + ": " + memberId;
    }

    public abstract int getTokenExpiry();

    public abstract int getRedisExpiry();
}
