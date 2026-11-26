
package com.abhorrence.chemistry.environment;

import mekanism.common.capabilities.Capabilities;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public final class MekaSuitHelper {

    private MekaSuitHelper() {}

    public static boolean hasFullChargedMekaSuit(PlayerEntity player) {
        return isValidPiece(player.getItemStackFromSlot(EquipmentSlotType.HEAD))
                && isValidPiece(player.getItemStackFromSlot(EquipmentSlotType.CHEST))
                && isValidPiece(player.getItemStackFromSlot(EquipmentSlotType.LEGS))
                && isValidPiece(player.getItemStackFromSlot(EquipmentSlotType.FEET));
    }

    private static boolean isValidPiece(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemMekaSuitArmor)) {
            return false;
        }

        return stack.getCapability(Capabilities.STRICT_ENERGY_CAPABILITY)
                .map(handler -> {
                    if (handler.getEnergyContainerCount() == 0) {
                        return false;
                    }

                    return !handler.getEnergy(0).isZero();
                })
                .orElse(false);
    }
}
