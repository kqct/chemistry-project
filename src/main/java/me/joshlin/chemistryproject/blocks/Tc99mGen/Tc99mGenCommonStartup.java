package me.joshlin.chemistryproject.blocks.Tc99mGen;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.GuiHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Tc99mGenCommonStartup {
    public static Block blockInventoryAdvanced;
    public static ItemBlock itemBlockInventoryAdvanced;

    public static void preInitCommon() {
        blockInventoryAdvanced = new Tc99mGenBlock().setUnlocalizedName("block_tc99m_gen");
        blockInventoryAdvanced.setRegistryName("block_tc99m_gen");
        GameRegistry.register(blockInventoryAdvanced);

        itemBlockInventoryAdvanced = new ItemBlock(blockInventoryAdvanced);
        itemBlockInventoryAdvanced.setRegistryName(blockInventoryAdvanced.getRegistryName());
        GameRegistry.register(itemBlockInventoryAdvanced);

        GameRegistry.registerTileEntity(Tc99mGenTileEntity.class, "block_tc99m_gen_tile_entity");

        NetworkRegistry.INSTANCE.registerGuiHandler(ChemistryProject.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new Tc99mGenGuiHandler(), Tc99mGenGuiHandler.getGuiID());
    }

    public static void initCommon() {}

    public static void postInitCommon() {

    }
}
