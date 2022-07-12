package org.mmga.controlgem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.mmga.controlgem.block.ControlGemOreBlock;
import org.mmga.controlgem.items.ControlGemItem;
import org.mmga.controlgem.items.ControlGemOreBlockItem;
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
    public static final ControlGemOreBlock CONTROL_GEM_ORE_BLOCK = new ControlGemOreBlock();
    public static final ItemGroup CONTROL_GEM_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("controlgem", "general"),
            () -> new ItemStack(CONTROL_GEM_ORE_BLOCK)
    );

    @Override
    public void onInitialize() {
        LOGGER.info("控制水晶开始加载");
        Identifier controlGemOreIdentifier = new Identifier("controlgem", "control_gem_ore");
        Registry.register(Registry.BLOCK, controlGemOreIdentifier, CONTROL_GEM_ORE_BLOCK);
        Registry.register(Registry.ITEM, controlGemOreIdentifier, new ControlGemOreBlockItem(CONTROL_GEM_ORE_BLOCK));
        Registry.register(Registry.ITEM, new Identifier("controlgem", "control_gem"), new ControlGemItem());
        LOGGER.info("控制水晶加载成功");
    }
}
