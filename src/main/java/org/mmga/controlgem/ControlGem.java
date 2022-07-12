package org.mmga.controlgem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.mmga.controlgem.block.ControlGemOreBlock;
import org.mmga.controlgem.items.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created On 2022/7/12 19:41
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGem implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ControlGem");
    public static final ControlGemOreBlock CONTROL_CRYSTAL_ORE_BLOCK = new ControlGemOreBlock();
    public static final ItemGroup CONTROL_GEM_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("controlgem", "general"),
            () -> new ItemStack(CONTROL_CRYSTAL_ORE_BLOCK)
    );

    @Override
    public void onInitialize() {
        LOGGER.info("控制水晶开始加载");
        Identifier controlCrystalOreIdentifier = new Identifier("controlgem", "control_crystal_ore");
        Registry.register(Registry.BLOCK, controlCrystalOreIdentifier, CONTROL_CRYSTAL_ORE_BLOCK);
        Registry.register(Registry.ITEM, controlCrystalOreIdentifier, new ControlCrystalOreBlockItem(CONTROL_CRYSTAL_ORE_BLOCK));
        registerItem("control_crystal", new ControlCrystalItem());
        registerItem("inferior_control_gem", new InferiorControlGemItem());
        registerItem("basic_control_gem", new BasicControlGemItem());
        registerItem("intermediate_control_gem", new IntermediateControlGemItem());
        registerItem("advanced_control_gem", new AdvancedControlGemItem());
        registerItem("ultimate_control_gem", new UltimateControlGemItem());
        registerItem("control_gem", new ControlGemItem());
        LOGGER.info("控制水晶加载成功");
    }

    public void registerItem(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier("controlgem", path), item);
    }
}
