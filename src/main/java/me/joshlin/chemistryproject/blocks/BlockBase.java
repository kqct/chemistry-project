package me.joshlin.chemistryproject.blocks;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.TooltipText;
import me.joshlin.chemistryproject.items.ItemModelProvider;
import me.joshlin.chemistryproject.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockBase extends Block implements ItemModelProvider {

    protected String name;

    public BlockBase(Material material, String name) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    @Override
    public void registerItemModel(Item item) {
        ChemistryProject.proxy.registerItemRenderer(item, 0, name);
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        Item item = stack.getItem();

        if (item == Item.getItemFromBlock(ModBlocks.blockTechnetium)) {
            TooltipText.blockTechnetium(tooltip);
        }

        if (item == Item.getItemFromBlock(ModBlocks.blockMolybdenum)) {
            TooltipText.blockMolybdenum(tooltip);
        }

        if (item == Item.getItemFromBlock(ModBlocks.oreMolybdenum)) {
            TooltipText.oreMolybdenum(tooltip);
        }
    }
}
