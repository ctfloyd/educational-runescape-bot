package com.educationalbotters.customscripts.WoodCutter.script;

import com.educationalbotters.EducationalBottersApi.Entities.Tree;
import com.educationalbotters.EducationalBottersApi.Items.Axe;
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
        if (tree == Tree.NORMAL) {
            return Banks.DRAYNOR;
        }

        return null;
    }

    // TODO: Implement the rest of the trees
    public static Area getWoodcuttingAreaForTree(Tree tree) {
        if (tree == Tree.NORMAL) {
            return WoodcuttingConstants.DRAYNOR_NORMAL_TREES;
        }

        return null;
    }
}
