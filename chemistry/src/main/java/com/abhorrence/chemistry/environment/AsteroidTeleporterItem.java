package com.abhorrence.chemistry.environment;

import com.abhorrence.chemistry.ChemistryMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.nbt.CompoundNBT;

public class AsteroidTeleporterItem extends Item {

    private static final RegistryKey<World> ASTEROID =
            RegistryKey.getOrCreateKey(
                    Registry.WORLD_KEY,
                    new ResourceLocation(ChemistryMod.MOD_ID, "asteroid")
            );

    private static final String HAS_ASTEROID_POS = "chemistry_has_asteroid_pos";

    private static final String ASTEROID_X = "chemistry_asteroid_x";
    private static final String ASTEROID_Y = "chemistry_asteroid_y";
    private static final String ASTEROID_Z = "chemistry_asteroid_z";
    private static final String ASTEROID_YAW = "chemistry_asteroid_yaw";
    private static final String ASTEROID_PITCH = "chemistry_asteroid_pitch";

    private static final String RETURN_DIM = "chemistry_return_dimension";
    private static final String RETURN_X = "chemistry_return_x";
    private static final String RETURN_Y = "chemistry_return_y";
    private static final String RETURN_Z = "chemistry_return_z";
    private static final String RETURN_YAW = "chemistry_return_yaw";
    private static final String RETURN_PITCH = "chemistry_return_pitch";

    public AsteroidTeleporterItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(
            World world,
            PlayerEntity player,
            Hand hand
    ) {
        ItemStack stack = player.getHeldItem(hand);

        if (world.isRemote) {
            return ActionResult.resultSuccess(stack);
        }

        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResult.resultFail(stack);
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        MinecraftServer server = serverPlayer.getServer();

        if (server == null) {
            return ActionResult.resultFail(stack);
        }

        CompoundNBT data = serverPlayer.getPersistentData();

        if (world.getDimensionKey().equals(ASTEROID)) {
            returnFromAsteroid(serverPlayer, server, data);
        } else {
            travelToAsteroid(serverPlayer, server, data);
        }

        return ActionResult.resultSuccess(stack);
    }

    private static void travelToAsteroid(
            ServerPlayerEntity player,
            MinecraftServer server,
            CompoundNBT data
    ) {
        ServerWorld asteroid = server.getWorld(ASTEROID);

        if (asteroid == null) {
            player.sendStatusMessage(
                    new StringTextComponent("Asteroid dimension is unavailable!"),
                    true
            );
            return;
        }

        // Save the current dimension and position.
        data.putString(
                RETURN_DIM,
                player.world.getDimensionKey().getLocation().toString()
        );

        data.putDouble(RETURN_X, player.getPosX());
        data.putDouble(RETURN_Y, player.getPosY());
        data.putDouble(RETURN_Z, player.getPosZ());
        data.putFloat(RETURN_YAW, player.rotationYaw);
        data.putFloat(RETURN_PITCH, player.rotationPitch);

        // Restore the previous asteroid position or use the initial spawn.
        double x = 0.0;
        double y = 100.0;
        double z = 0.0;
        float yaw = 0.0F;
        float pitch = 0.0F;

        if (data.getBoolean(HAS_ASTEROID_POS)) {
            x = data.getDouble(ASTEROID_X);
            y = data.getDouble(ASTEROID_Y);
            z = data.getDouble(ASTEROID_Z);
            yaw = data.getFloat(ASTEROID_YAW);
            pitch = data.getFloat(ASTEROID_PITCH);
        }

        player.teleport(asteroid, x, y, z, yaw, pitch);
    }

    private static void returnFromAsteroid(
            ServerPlayerEntity player,
            MinecraftServer server,
            CompoundNBT data
    ) {
        // Save the current asteroid position.
        data.putBoolean(HAS_ASTEROID_POS, true);

        data.putDouble(ASTEROID_X, player.getPosX());
        data.putDouble(ASTEROID_Y, player.getPosY());
        data.putDouble(ASTEROID_Z, player.getPosZ());
        data.putFloat(ASTEROID_YAW, player.rotationYaw);
        data.putFloat(ASTEROID_PITCH, player.rotationPitch);

        // Restore the previous dimension.
        if (!data.contains(RETURN_DIM)) {
            player.sendStatusMessage(
                    new StringTextComponent("No return location saved!"),
                    true
            );
            return;
        }

        ResourceLocation location =
                new ResourceLocation(data.getString(RETURN_DIM));

        RegistryKey<World> dimension =
                RegistryKey.getOrCreateKey(Registry.WORLD_KEY, location);

        ServerWorld destination = server.getWorld(dimension);

        if (destination == null) {
            player.sendStatusMessage(
                    new StringTextComponent("Return dimension is unavailable!"),
                    true
            );
            return;
        }

        double x = data.getDouble(RETURN_X);
        double y = data.getDouble(RETURN_Y);
        double z = data.getDouble(RETURN_Z);

        float yaw = data.getFloat(RETURN_YAW);
        float pitch = data.getFloat(RETURN_PITCH);

        player.teleport(destination, x, y, z, yaw, pitch);
    }
}
