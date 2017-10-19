package me.joshlin.chemistryproject.blocks.Tc99mGen;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.TooltipText;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class Tc99mGenBlock extends BlockContainer {

    public Tc99mGenBlock() {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        TooltipText.Tc99mGen(tooltip);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new Tc99mGenTileEntity();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(ChemistryProject.instance, Tc99mGenGuiHandler.getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    // render using a BakedModel
    // required because the default (super method) is INVISIBLE for BlockContainers.
    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }
}

//https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/BlockInventoryFurnace.java