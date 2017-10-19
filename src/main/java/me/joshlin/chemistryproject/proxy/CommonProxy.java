package me.joshlin.chemistryproject.proxy;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.blocks.ModBlocks;
import me.joshlin.chemistryproject.blocks.Tc99mGen.Tc99mGenCommonStartup;
import me.joshlin.chemistryproject.fluids.ModFluids;
import me.joshlin.chemistryproject.items.ModItems;
import me.joshlin.chemistryproject.recipe.ModRecipes;
import me.joshlin.chemistryproject.world.ModWorldGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {

    }

    public void preInit() {
        System.out.println(ChemistryProject.name + " is loading!");
        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
        Tc99mGenCommonStartup.preInitCommon();
    }

    public void init() {
        ModRecipes.init();
        Tc99mGenCommonStartup.initCommon();
    }

    public void postInit() {
        Tc99mGenCommonStartup.postInitCommon();
    }

    abstract public boolean playerIsInCreativeMode(EntityPlayer player);

    /**
     * is this a dedicated server?
     * @return true if this is a dedicated server, false otherwise
     */
    abstract public boolean isDedicatedServer();
}
