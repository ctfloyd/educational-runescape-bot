package com.educationalbotters.customscripts.WoodCutter.script;

import com.educationalbotters.EducationalBottersApi.Items.Axe;
import com.educationalbotters.EducationalBottersApi.Entities.Tree;
import org.osbot.rs07.api.map.Area;

public class WoodcuttingConfiguration {

    private Tree tree;
    private Axe axe;
    private Area nearestBank;
    private Area woodcuttingArea;

    public void setNearestBank(Area nearestBank) {
        this.nearestBank = nearestBank;
    }

    public Area getNearestBank() {
        return nearestBank;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Axe getAxe() {
        return axe;
    }

    public void setAxe(Axe axe) {
        this.axe = axe;
    }

    public Area getWoodcuttingArea() {
        return woodcuttingArea;
    }

    public void setWoodcuttingArea(Area woodcuttingArea) {
        this.woodcuttingArea = woodcuttingArea;
    }
}
