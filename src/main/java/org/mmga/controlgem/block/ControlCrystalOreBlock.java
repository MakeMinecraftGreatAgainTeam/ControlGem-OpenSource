package org.mmga.controlgem.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

/**
 * Created On 2022/7/12 20:32
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlCrystalOreBlock extends OreBlock {
    public ControlCrystalOreBlock() {
        super(FabricBlockSettings.of(Material.METAL).hardness(3.0f).requiresTool(), UniformIntProvider.create(1, 3));
    }
}
