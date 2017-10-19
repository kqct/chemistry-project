package me.joshlin.chemistryproject.proxy;

import me.joshlin.chemistryproject.ChemistryProject;
import me.joshlin.chemistryproject.blocks.Tc99mGen.Tc99mGenClientStartup;
import me.joshlin.chemistryproject.fluids.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ChemistryProject.modId + ":" + id, "inventory"));
    }

    @Override
    public void preInit() {
        super.preInit();
        Tc99mGenClientStartup.preInitClient();
        ModFluids.renderFluids();
    }

    @Override
    public void init() {
        super.init();
        Tc99mGenClientStartup.initClientOnly();
    }

    @Override
    public void postInit() {
        super.postInit();
        Tc99mGenClientStartup.postInitClientOnly();
    }

    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
            return entityPlayerMP.interactionManager.isCreative();
        } else if (player instanceof EntityPlayerSP) {
            return Minecraft.getMinecraft().playerController.isInCreativeMode();
        }
        return false;
    }

    @Override
    public boolean isDedicatedServer() {return false;}
}
