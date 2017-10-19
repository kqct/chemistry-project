package me.joshlin.chemistryproject;

import me.joshlin.chemistryproject.proxy.CommonProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ChemistryProject.modId, name = ChemistryProject.name, version = ChemistryProject.version)
public class ChemistryProject {

    public static final String modId = "chemproject";
    public static final String name = "Josh's Chemistry Project";
    public static final String version = "1.0.0";

    @Mod.Instance(modId)
    public static ChemistryProject instance;

    @SidedProxy(clientSide = "me.joshlin.chemistryproject.proxy.ClientProxy", serverSide = "me.joshlin.chemistryproject.proxy.CommonProxy")
    public static CommonProxy proxy;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
