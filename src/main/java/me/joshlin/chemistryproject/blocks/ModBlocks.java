package me.joshlin.chemistryproject.blocks;

import me.joshlin.chemistryproject.items.ItemModelProvider;
import me.joshlin.chemistryproject.items.ItemOreDict;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static BlockBase blockMolybdenum;
    public static BlockBase blockTechnetium;
    public static BlockOre oreMolybdenum;

    public static void init() {
        blockTechnetium = register(new BlockBase(Material.IRON, "block_technetium").setCreativeTab(CreativeTabs.MATERIALS));
        blockMolybdenum = register(new BlockBase(Material.IRON, "block_molybdenum").setCreativeTab(CreativeTabs.MATERIALS));
        oreMolybdenum = register(new BlockOre("ore_molybdenum", "oreMolybdenum").setCreativeTab(CreativeTabs.MATERIALS));

    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        if (itemBlock != null) {
            GameRegistry.register(itemBlock);

            if (block instanceof ItemModelProvider) {
                ((ItemModelProvider)block).registerItemModel(itemBlock);
            }
            if (block instanceof ItemOreDict) {
                ((ItemOreDict)block).initOreDict();
            }
            if (itemBlock instanceof ItemOreDict) {
                ((ItemOreDict)itemBlock).initOreDict();
            }
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        return register(block, itemBlock);
    }
}
