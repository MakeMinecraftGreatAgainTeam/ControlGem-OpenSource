package org.mmga.controlgem.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

/**
 * Created On 2022/7/12 20:32
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlCrystalOreBlock extends Block {
    public ControlCrystalOreBlock() {
        super(FabricBlockSettings.of(Material.METAL).hardness(3.0f).requiresTool());
    }
}
