package com.educationalbotters.educationalbottersapi;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.utility.ConditionalSleep;

public class Sleep {

    public static void untilEntityDoesNotExistOrSecondsElapsed(Entity entity, int secondsElapsed)  {
         new ConditionalSleep(secondsElapsed * 1000) {
             @Override
             public boolean condition() {
                 return !entity.exists();
             }
         }.sleep();
   }

   public static void untilInAreaOrSecondsElapsed(Player player, Area area, int secondsElapsed) {
       new ConditionalSleep(secondsElapsed * 1000) {
           @Override
           public boolean condition() {
               return area.contains(player);
           }
       }.sleep();
   }
}
