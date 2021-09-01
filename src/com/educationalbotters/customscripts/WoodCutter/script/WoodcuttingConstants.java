package com.educationalbotters.customscripts.WoodCutter.script;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public class WoodcuttingConstants {

    public static final Area DRAYNOR_NORMAL_TREES = new Area(new Position[]{
            // South-East
            new Position(3116, 3216, 0),
            // South-West
            new Position(3106, 3215, 0),
            // North-West
            new Position(3107, 3223, 0),
            // North-East
            new Position(3120, 3224, 0)
    });

    public static final Area VARROCK_OAK_TREES = new Area(new Position[] {
            new Position(3170, 3423, 0),
            new Position(3170, 3407, 0),
            new Position(3159, 3406, 0),
            new Position(3159, 3421, 0)
    });

}
