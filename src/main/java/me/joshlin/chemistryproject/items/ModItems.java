package me.joshlin.chemistryproject.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemBase ingotTechnetium;
    public static ItemBase ingotMolybdenum;

    public static ItemBase bottleHydrogen;
    public static ItemBase bottleOxygen;

    public static void init() {
        ingotTechnetium = register(new ItemBase("ingot_technetium").setCreativeTab(CreativeTabs.MATERIALS));
        ingotMolybdenum = register(new ItemBase("ingot_molybdenum").setCreativeTab(CreativeTabs.MATERIALS));

        bottleHydrogen = register(new ItemBase("bottle_hydrogen").setCreativeTab(CreativeTabs.MISC));
        bottleOxygen = register(new ItemBase("bottle_oxygen").setCreativeTab(CreativeTabs.MISC));
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemModelProvider) {
            ((ItemModelProvider)item).registerItemModel(item);
        }
        if (item instanceof ItemOreDict) {
            ((ItemOreDict)item).initOreDict();
        }

        return item;
    }
}
