package com.abhorrence.chemistry.environment;

import com.abhorrence.chemistry.ChemistryMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldEnvironment {

    public static final double DEFAULT_TEMPERATURE = 300.0;
    public static final double ASTEROID_TEMPERATURE = 10.0;

    private static final ResourceLocation ASTEROID_DIMENSION =
            new ResourceLocation(ChemistryMod.MOD_ID, "asteroid");

    public static double getTemperature(World world, BlockPos pos) {
        if (world.getDimensionKey().getLocation().equals(ASTEROID_DIMENSION)) {
            return ASTEROID_TEMPERATURE;
        }

        return DEFAULT_TEMPERATURE;
    }
}