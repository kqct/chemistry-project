package me.joshlin.chemistryproject.fluids;

import me.joshlin.chemistryproject.fluids.HydrogenGas.BlockFluidHydrogenGas;
import me.joshlin.chemistryproject.fluids.HydrogenGas.FluidHydrogenGas;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {

   public static FluidHydrogenGas fluidHydrogenGas;
   public static BlockFluidHydrogenGas blockFluidHydrogenGas;

   public static void init() {
       fluidHydrogenGas = new FluidHydrogenGas();
       blockFluidHydrogenGas = new BlockFluidHydrogenGas();
       FluidRegistry.addBucketForFluid(fluidHydrogenGas);
   }

   public static void renderFluids() {
       blockFluidHydrogenGas.render();
   }


}
