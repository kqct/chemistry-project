package me.joshlin.chemistryproject.fluids.HydrogenGas;

import me.joshlin.chemistryproject.fluids.ModFluids;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidHydrogenGas extends BlockFluidClassic {
    public BlockFluidHydrogenGas() {
        super(ModFluids.fluidHydrogenGas, Material.WATER);
        setRegistryName("gas_hydrogen");
        setUnlocalizedName(getRegistryName().toString());
        GameRegistry.register(this);
    }

    @SideOnly(Side.CLIENT)
    public void render() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }
}
