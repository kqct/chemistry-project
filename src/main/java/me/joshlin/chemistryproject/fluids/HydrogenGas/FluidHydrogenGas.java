package me.joshlin.chemistryproject.fluids.HydrogenGas;

import me.joshlin.chemistryproject.ChemistryProject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHydrogenGas extends Fluid {
    public FluidHydrogenGas() {
        super("gas_hydrogen", new ResourceLocation(ChemistryProject.modId, "fluids/gas_hydrogen_still"),
                new ResourceLocation(ChemistryProject.modId, "fluids/gas_hydrogen_flow"));
        FluidRegistry.registerFluid(this);
    }
}
