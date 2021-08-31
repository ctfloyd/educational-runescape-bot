package com.educationalbotters.customscripts.WoodCutter.script;

import com.educationalbotters.EducationalBottersApi.Constants;
import com.educationalbotters.EducationalBottersApi.Interactions;
import com.educationalbotters.EducationalBottersApi.Sleep;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

@ScriptManifest(author = "Floyd", name = "Woodcutter", info = "Chop them trees!", version = 0.1, logo = "")
public final class Woodcutter extends Script {

    private WoodcutterGui gui;
    private WoodcuttingConfiguration woodcuttingConfiguration;


    // TODO: GUI scripts would be good candidate to extract to the API
    @Override
    public void onStart() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new WoodcutterGui();
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            log("An unexpected error occurred while creating the GUI");
            e.printStackTrace();
            stop();
            return;
        }

        // User closed the gui without clicking the start button
        if(!gui.isStarted()) {
            stop();
            return;
        }

        woodcuttingConfiguration = WoodcuttingConfigurationService.getConfiguration(gui.getSelectedTree(), gui.getSelectedAxe());
    }

    @Override
    public int onLoop() throws InterruptedException {
        int nextUpdate = random(3 * Constants.TICK, 5 * Constants.TICK);

        if(!inventoryOnlyHasLogsAndAxe() || getInventory().isFull()) {
            bankItems();
            return nextUpdate;
        }

        if(!isAtWoodcuttingLocation()) {
            walkToWoodcuttingLocation();
            return nextUpdate;
        }

        chopTree();
        return nextUpdate;
    }

    @Override
    public void onExit() {
        if(gui != null) {
            gui.close();
        }
    }

    private void chopTree() {
        // Already performing an action
       if(myPlayer().isAnimating() || myPlayer().isMoving()) {
           return;
       }

       RS2Object tree = getObjects().closest(
               woodcuttingConfiguration.getWoodcuttingArea(),
               woodcuttingConfiguration.getTree().getName()
       );

       if(tree != null) {
           tree.interact(Interactions.CHOP_DOWN.getName());
           Sleep.untilEntityDoesNotExitOrSecondsElapsed(tree, 5);
       }
    }

    private boolean isAtWoodcuttingLocation() {
        return woodcuttingConfiguration.getWoodcuttingArea().contains(myPosition());
    }

    private void walkToWoodcuttingLocation() {
        getWalking().webWalk(woodcuttingConfiguration.getWoodcuttingArea());
        Sleep.untilInAreaOrSecondsElapsed(myPlayer(), woodcuttingConfiguration.getWoodcuttingArea(), 20);
    }

    private void bankItems() throws InterruptedException {
        Area nearestBankToWoodcuttingArea = woodcuttingConfiguration.getNearestBank();
        if(!nearestBankToWoodcuttingArea.contains(myPosition())) {
            getWalking().webWalk(nearestBankToWoodcuttingArea);
            Sleep.untilInAreaOrSecondsElapsed(myPlayer(), nearestBankToWoodcuttingArea, 20);
        }

        Bank bank = getBank();
        if(bank == null) {
            log("Expected to be near a bank, but was not. Exiting!");
            stop();
        }

        bank.open();
        bank.depositAll();
        bank.withdraw(woodcuttingConfiguration.getAxe().getItemId(), 1);
    }

    private boolean inventoryOnlyHasLogsAndAxe() {
        return getInventory().onlyContains(
                woodcuttingConfiguration.getAxe().getItemId(),
                woodcuttingConfiguration.getTree().getLogs().getItemId()
        );
    }
}
