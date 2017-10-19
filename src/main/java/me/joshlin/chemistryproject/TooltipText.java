package me.joshlin.chemistryproject;

import java.util.List;

public class TooltipText {

    // Items
    public static void ingotTechnetium(List<String> tooltip) {
        tooltip.add("\u00A75"+ "[Kr]5s²4d⁵");
        tooltip.add("Technetium was officially discovered by Carlo Perrier and Emilio Segrè in 1937, in Sicily, Italy.");
    }

    public static void ingotMolybdenum(List<String> tooltip) {
        tooltip.add("\u00A75"+ "[Kr]5s¹4d⁵");
        tooltip.add("Although technetium has never naturally occurred on earth, its spectral lines have been observed in certain types of stars.");
    }

    public static void bottleHydrogen(List<String> tooltip) {
        tooltip.add("\u00A75"+ "1s¹");
        tooltip.add("Hydrogen, officially recognized as an independent element in 1776 by Henry Cavendish, is one of the most important elements in our daily lives.");
    }

    public static void bottleOxygen(List<String> tooltip) {
        tooltip.add("\u00A75"+ "[He]2s²2p⁴");
        tooltip.add("Although hydrogen is found in many different materials, and is used in many different applications, it is commonly found paired with oxygen to create water.");
    }

    // Blocks
    public static void blockTechnetium(List<String> tooltip) {
        tooltip.add("Technetium is the lightest radioactive element on the periodic table.");
    }

    public static void blockMolybdenum(List<String> tooltip) {
        tooltip.add("Because molybdenum is much more stable than technetium, it is transported in technetium's place, after which technetium is produced on the spot.");
    }

    public static void oreMolybdenum(List<String> tooltip) {
        tooltip.add("Technetium is artificially produced; it decays from molybdenum-99.");
    }

    public static void Tc99mGen(List<String> tooltip) {
        tooltip.add("Technetium-99m generators, also known as \"moly cows\", are used to transform molybdenum-98 into technetium-99m.");
    }
}
