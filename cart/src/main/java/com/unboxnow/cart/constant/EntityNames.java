package com.unboxnow.cart.constant;

public final class EntityNames {

    public static String PRODUCT = "product";

    public static String MEMBER = "member";

    public static String INVENTORY = "inventory";

    public static String getCartKey(int memberId) {
        return MEMBER + "Id: " + memberId;
    }

    public static String getInventoryKey(String messageId) {
        return INVENTORY + ": " + messageId;
    }
}
