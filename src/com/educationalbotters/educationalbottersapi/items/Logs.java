package com.educationalbotters.educationalbottersapi.items;

public enum Logs {
    LOGS("Logs", 1511),
    OAK_LOGS("Oak logs", 1521),
    WILLOW_LOGS("Willow logs", 1519),
    TEAK_LOGS("Teak logs", 6333),
    MAPLE_LOGS("Maple logs", 1517),
    YEW_LOGS("Yew logs", 1515),
    MAGIC_LOGS("Magic logs", 1513);

    private String name;
    private int itemId;

    Logs(String name, int itemId) {
        this.name = name;
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public int getItemId(){
        return itemId;
    }
}
