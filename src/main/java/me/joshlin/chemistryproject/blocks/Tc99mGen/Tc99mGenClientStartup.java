package me.joshlin.chemistryproject.blocks.Tc99mGen;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class Tc99mGenClientStartup {

    public static void preInitClient() {
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("chemproject:block_tc99m_gen", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        ModelLoader.setCustomModelResourceLocation(Tc99mGenCommonStartup.itemBlockInventoryAdvanced, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }

    public static void initClientOnly() {

    }

    public static void postInitClientOnly() {

    }
}
