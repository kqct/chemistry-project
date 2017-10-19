package me.joshlin.chemistryproject.recipe;

import me.joshlin.chemistryproject.blocks.ModBlocks;
import me.joshlin.chemistryproject.blocks.Tc99mGen.Tc99mGenCommonStartup;
import me.joshlin.chemistryproject.fluids.ModFluids;
import me.joshlin.chemistryproject.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    public static void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockTechnetium,
                "TTT", "TTT", "TTT", 'T', "ingotTechnetium"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockMolybdenum,
                "MMM", "MMM", "MMM", 'M', "ingotMolybdenum"));
        GameRegistry.addRecipe(new ShapedOreRecipe(Tc99mGenCommonStartup.blockInventoryAdvanced,
                "IOI", "IMI", "IOI", 'I', Items.IRON_INGOT, 'O', Blocks.OBSIDIAN, 'M', ModItems.ingotMolybdenum));
        GameRegistry.addSmelting(ModBlocks.oreMolybdenum, new ItemStack(ModItems.ingotMolybdenum),1.0f);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bottleHydrogen, 2), new ItemStack(Items.WATER_BUCKET.setContainerItem(ModItems.bottleOxygen)));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.WATER_BUCKET), new ItemStack(ModItems.bottleHydrogen), new ItemStack(ModItems.bottleHydrogen), new ItemStack(ModItems.bottleOxygen));
        GameRegistry.addShapelessRecipe(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.fluidHydrogenGas),
                new ItemStack(ModItems.bottleHydrogen), new ItemStack(ModItems.bottleHydrogen), new ItemStack(ModItems.bottleHydrogen));
    }
}
