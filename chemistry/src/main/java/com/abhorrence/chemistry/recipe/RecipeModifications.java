package com.abhorrence.chemistry.recipe;

import net.minecraft.util.ResourceLocation;

public class RecipeModifications {

    public static void apply(RecipeEditor editor) {
        editor.remove("mekanism:rotary/hydrogen");
        editor.remove("mekanism:rotary/lithium");

        editor.remove("mekanism:module_frost_walker_unit");
        editor.remove("mekanism:metallurgic_infuser");
        editor.remove("mekanism:steel_casing");
        editor.remove("mekanism:control_circuit/basic");
        editor.remove("pneumaticcraft:amadron_tablet");
        editor.remove("mekanism:transmitter/universal_cable/basic");
        editor.remove("pneumaticcraft:explosion_crafting/compressed_iron_ingot");
        editor.remove("pneumaticcraft:explosion_crafting/compressed_iron_block");
        editor.remove("pneumaticcraft:pressure_chamber/compressed_iron_ingot");
        editor.remove("pneumaticcraft:pressure_chamber/compressed_iron_block");

        editor.remove("appliedenergistics2:inscriber/engineering_processor");
        editor.remove("appliedenergistics2:inscriber/engineering_processor_print");
        editor.remove("appliedenergistics2:inscriber/logic_processor");
        editor.remove("appliedenergistics2:inscriber/calculation_processor");
        editor.remove("appliedenergistics2:inscriber/logic_processor_print");
    }
}
