package com.educationalbotters.customscripts.WoodCutter.script;

import com.educationalbotters.educationalbottersapi.entities.Tree;
import com.educationalbotters.educationalbottersapi.items.Axe;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public class WoodcuttingConfigurationService {

    public static WoodcuttingConfiguration getConfiguration(Tree tree, Axe axe) {
        WoodcuttingConfiguration configuration = new WoodcuttingConfiguration();
        configuration.setTree(tree);
        configuration.setAxe(axe);
        configuration.setNearestBank(getNearestBankForTree(tree));
        configuration.setWoodcuttingArea(getWoodcuttingAreaForTree(tree));
        return configuration;
    }

    // TODO: Implement the rest of the trees
    public static Area getNearestBankForTree(Tree tree) {
        System.out.println("tree is: " + tree);
        if (tree == Tree.NORMAL) {
            return Banks.DRAYNOR;
        }

        if (tree == Tree.OAK) {
            return Banks.VARROCK_WEST;
        }

        return null;
    }

    // TODO: Implement the rest of the trees
    public static Area getWoodcuttingAreaForTree(Tree tree) {
        if (tree == Tree.NORMAL) {
            return WoodcuttingConstants.DRAYNOR_NORMAL_TREES;
        }

        if (tree == Tree.OAK) {
            return WoodcuttingConstants.VARROCK_OAK_TREES;
        }

        return null;
    }
}
