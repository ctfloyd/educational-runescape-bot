package com.educationalbotters.customscripts.ChickenSlaughter.script;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Zec", name = "Chicken Slaughter", info = "To Slaughter all that cluck", version = 0.1, logo = "")
public final class ChickenSlaughter extends Script {
    @Override
    public final int onLoop() throws InterruptedException {
        NPC Chicken = getNpcs().closest("Chicken");
        if (Chicken != null) {
            boolean inCombat = combat.isFighting();
            int chickenHP = Chicken.getHealthPercent();
            if(!inCombat && chickenHP != 0){
                    Chicken.interact("Attack");
                    log("Slaughtering Chicken");
            }
        }


        HandleHP();

        return 1000;
    }


    public void HandleHP (){
        boolean hasTuna = PlayerHasTuna();
        if(!hasTuna){
            stop();
        }
        int playerHP = myPlayer().getHealthPercent();
        log(playerHP);
        if(playerHP <100){
            inventory.interact("Eat", "Tuna");
            log("Healing!!");

        }
    }

    public boolean PlayerHasTuna(){
        boolean checkFood = inventory.contains("Tuna");
        return checkFood;
    }
}




