package me.joshlin.chemistryproject.items;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.TooltipText;
import me.joshlin.chemistryproject.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

public class ItemBase extends Item implements ItemModelProvider {

    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    @Override
    public void registerItemModel(Item item) {
        ChemistryProject.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        Item item = stack.getItem();

        if (item == ModItems.ingotTechnetium) {
            TooltipText.ingotTechnetium(tooltip);
        }

        if (item == ModItems.ingotMolybdenum) {
            TooltipText.ingotMolybdenum(tooltip);
        }

        if (item == ModItems.bottleHydrogen) {
            TooltipText.bottleHydrogen(tooltip);
        }

        if (item == ModItems.bottleOxygen) {
            TooltipText.bottleOxygen(tooltip);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityIn;

            for (ItemStack s : player.inventory.mainInventory) {
                if (s != null && s.getItem() == ModItems.ingotTechnetium) {
                    player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("poison")));
                }
            }
        }
    }
}
