package com.educationalbotters.educationalbottersapi.items;

public enum Axe {
    BRONZE_AXE("Bronze axe", 1351),
    IRON_AXE("Iron axe", 1349),
    STEEL_AXE("Steel axe", 1353),
    BLACK_AXE("Black axe", 1361),
    MITHRIL_AXE("Mithril axe", 1355),
    ADAMANT_AXE("Adamant axe", 1357),
    RUNE_AXE("Rune axe", 1359),
    DRAGON_AXE("Dragon axe", 6739);

    private int itemId;
    private String name;

    Axe(String name, int itemId) {
        this.itemId = itemId;
        this.name = name;
    }

    public int getItemId() {
       return itemId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
