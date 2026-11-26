
package com.abhorrence.chemistry.environment;

import com.abhorrence.chemistry.ChemistryMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = ChemistryMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class AsteroidSurvivalHandler {

    private static final ResourceLocation ASTEROID_DIMENSION =
            new ResourceLocation(ChemistryMod.MOD_ID, "asteroid");

    private static final String EXPOSURE_TICKS =
            "chemistry_asteroid_exposure";

    private static final String WAS_IN_ASTEROID =
            "chemistry_was_in_asteroid";

    private static final int MAX_EXPOSURE_TICKS = 300;

    private static final int EFFECT_DURATION = 40;

    private AsteroidSurvivalHandler() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        PlayerEntity player = event.player;

        // Only execute on the server.
        if (player.world.isRemote) {
            return;
        }

        // Creative and spectator players are unaffected.
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        boolean inAsteroid = player.world
                .getDimensionKey()
                .getLocation()
                .equals(ASTEROID_DIMENSION);

        CompoundNBT data = player.getPersistentData();

        // Player has left the asteroid dimension.
        if (!inAsteroid) {
            if (data.getBoolean(WAS_IN_ASTEROID)) {
                resetExposure(player, data);
            }

            return;
        }

        data.putBoolean(WAS_IN_ASTEROID, true);

        boolean hasSuit =
                MekaSuitHelper.hasFullChargedMekaSuit(player);

        // Oxygen consumption.
        if (!hasSuit) {
            consumeOxygen(player);
        }

        // A charged MekaSuit protects against environmental exposure.
        if (hasSuit) {
            removeExposureEffects(player);
            return;
        }

        // Increase exposure time.
        int exposure = data.getInt(EXPOSURE_TICKS) + 1;
        data.putInt(EXPOSURE_TICKS, exposure);

        // Apply blindness and nausea.
        if (exposure == 1 || exposure % 20 == 0) {
            applyExposureEffects(player);
        }

        // Kill the player after 15 seconds.
        if (exposure >= MAX_EXPOSURE_TICKS) {
            player.setHealth(0.0F);
        }
    }

    private static void consumeOxygen(PlayerEntity player) {
        /*
         * Outside water, vanilla restores 4 air points every tick.
         * Subtracting 5 here produces a net loss of 1 point per tick.
         */
        int nextAir = player.getAir() - 5;

        if (nextAir <= -20) {
            player.attackEntityFrom(DamageSource.DROWN, 2.0F);
            player.setAir(0);
        } else {
            player.setAir(nextAir);
        }
    }

    private static void applyExposureEffects(PlayerEntity player) {
        player.addPotionEffect(new EffectInstance(
                Effects.BLINDNESS,
                EFFECT_DURATION,
                0,
                true,
                false
        ));

        player.addPotionEffect(new EffectInstance(
                Effects.NAUSEA,
                EFFECT_DURATION,
                0,
                true,
                false
        ));
    }

    private static void removeExposureEffects(PlayerEntity player) {
        player.removePotionEffect(Effects.BLINDNESS);
        player.removePotionEffect(Effects.NAUSEA);
    }

    private static void resetExposure(
            PlayerEntity player,
            CompoundNBT data
    ) {
        data.putInt(EXPOSURE_TICKS, 0);
        data.putBoolean(WAS_IN_ASTEROID, false);

        removeExposureEffects(player);
    }
}
