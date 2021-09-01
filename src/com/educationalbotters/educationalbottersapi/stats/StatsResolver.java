package com.educationalbotters.educationalbottersapi.stats;

import com.educationalbotters.educationalbottersapi.items.Axe;

// TODO: For now this will be one giga-large class but there should be a way to break these up into something nicer.
public class StatsResolver {

    public static Axe getBestAxeForLevel(int level) {
       if(level < 0 || level > 100) {
           throw new IllegalArgumentException("A level must be between 0-99");
       }

       if(level < 6) {
           return Axe.IRON_AXE;
       }

       if(level < 11) {
           return Axe.STEEL_AXE;
       }

       if(level < 21) {
           return Axe.BLACK_AXE;
       }

       if(level < 31) {
           return Axe.MITHRIL_AXE;
       }

       if(level < 41) {
           return Axe.ADAMANT_AXE;
       }

       return Axe.DRAGON_AXE;
    }

}
