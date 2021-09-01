package com.educationalbotters.educationalbottersapi.entities;

import com.educationalbotters.educationalbottersapi.items.Logs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Tree {
    NORMAL("Tree", Logs.LOGS),
    OAK("Oak", Logs.OAK_LOGS),
    WILLOW("Willow", Logs.WILLOW_LOGS),
    MAPLE("Maple", Logs.MAPLE_LOGS),
    YEW("Yew", Logs.YEW_LOGS),
    MAGIC("Magic", Logs.MAGIC_LOGS);

    private String name;
    private Logs logs;

    Tree(String name, Logs logs) {
        this.name = name;
        this.logs = logs;
    }

    public String getName() {
        return this.name;
    }

    public Logs getLogs() {
        return this.logs;
    }

    public static List<String> names() {
        return Arrays.stream(Tree.values()).map(Tree::getName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}
