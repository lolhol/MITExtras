package com.extra.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class MITconfig extends Vigilant {
    @Property(
            type = PropertyType.SLIDER, name = "nukerRange", description = "Customise how far away the nuker breaks blocks (best is probably like 3 or 2)", category = "Garden Nuker", max = 5
    )
    public static int nukerRange = 4;

    @Property(
            type = PropertyType.SLIDER, name = "Grass ESP Distance", description = "Customise how far ull be able to see grass and wood and stuff like dat", category = "Garden Nuker", max = 30
    )
    public static int grassESPRange = 10;

    @Property(
            type = PropertyType.SWITCH, name = "Turn on/off ESP", description = "Basically garden ESP. I DO NOT KNOW how to make an optimised ESP so this might (50%) drop ur fps!", category = "Garden Nuker")
    public static boolean turnOnESPRange = false;

    @Property(
            type = PropertyType.SWITCH, name = "Turn on/off current block ESP", description = "Turn on/off the nuker displaying the currently nuked block (mainly for debuging but it looks cool 2)", category = "Garden Nuker")
    public static boolean currentNukedBlockESPSwitch = false;

    //----------------------------------------------------------------
    //                              Crop Nuker
    //----------------------------------------------------------------

    @Property(
            type = PropertyType.SELECTOR, name = "Nuker Crop Type", description = "Customise what crops the crop nuker will nuke.", category = "Crop Nuker", options = {"Wheat", "Carrot", "Potato", "Warts", "Pumpkin", "Melon", "Cocoa"})
    public static int cropNukerType = 0;

    @Property(
            type = PropertyType.SELECTOR, name = "Crop Nuker Blocks/Second", description = "Customise how fast the nuker breaks blocks. (Ps: yes, ik this is slow but i still dono how to make it faster.)", category = "Crop Nuker", options = {"10 BPS", "5 BPS"}
    )
    public static int cropNukerBPS = 1;

    public static MITconfig INSTANCE = new MITconfig();

    public MITconfig() {
        super(new File("./config/MiningInTwoConf.toml"));
        initialize();
    }

}
