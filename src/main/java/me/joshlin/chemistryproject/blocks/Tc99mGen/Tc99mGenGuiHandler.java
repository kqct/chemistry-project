package me.joshlin.chemistryproject.blocks.Tc99mGen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class Tc99mGenGuiHandler implements IGuiHandler {

    private static final int GUIID_TC99M = 99;
    public static int getGuiID() {
        return GUIID_TC99M;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != getGuiID()) {
            System.err.println("Invalid ID: expected " + getGuiID() + ", received " + ID);
        }

        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);
        if (tileEntity instanceof Tc99mGenTileEntity) {
            Tc99mGenTileEntity tc99mGenTileEntity = (Tc99mGenTileEntity) tileEntity;
            return new Tc99mGenContainer(player.inventory, tc99mGenTileEntity);
        }
        return null;
    }

    // Gets the client side element for the given gui id this should return a gui
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != getGuiID()) {
            System.err.println("Invalid ID: expected " + getGuiID() + ", received " + ID);
        }

        BlockPos xyz = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(xyz);
        if (tileEntity instanceof Tc99mGenTileEntity) {
            Tc99mGenTileEntity tc99mGenTileEntity = (Tc99mGenTileEntity) tileEntity;
            return new Tc99mGenGui(player.inventory, tc99mGenTileEntity);
        }
        return null;
    }
}

// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace/GuiHandlerMBE31.java
