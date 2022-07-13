package org.mmga.controlgem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.mmga.controlgem.block.ControlCrystalOreBlock;
import org.mmga.controlgem.commands.ReportCommand;
import org.mmga.controlgem.events.ServerWorldTickEvent;
import org.mmga.controlgem.items.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created On 2022/7/12 19:41
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGem implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ControlGem");
    public static final ControlCrystalOreBlock CONTROL_CRYSTAL_ORE_BLOCK = new ControlCrystalOreBlock();
    public static final ItemGroup CONTROL_GEM_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("controlgem", "general"),
            () -> new ItemStack(CONTROL_CRYSTAL_ORE_BLOCK)
    );
    public static final ConfiguredFeature<?, ?> CONTROL_CRYSTAL_ORE_OVERWORLD_CONFIGURED_FEATURE =
            new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, CONTROL_CRYSTAL_ORE_BLOCK.getDefaultState(), 5));
    public static final PlacedFeature CONTROL_CRYSTAL_ORE_OVERWORLD_PLACED_FEATURE = new PlacedFeature(
            RegistryEntry.of(CONTROL_CRYSTAL_ORE_OVERWORLD_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(6),
                    SquarePlacementModifier.of(),
                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(15))
            ));
    public static final Item INFERIOR_CONTROL_GEM = new InferiorControlGemItem();
    public static final Item BASIC_CONTROL_GEM = new BasicControlGemItem();
    public static final Item INTERMEDIATE_CONTROL_GEM = new IntermediateControlGemItem();
    public static final Item ADVANCED_CONTROL_GEM = new AdvancedControlGemItem();
    public static final Item ULTIMATE_CONTROL_GEM = new UltimateControlGemItem();
    public static final Item SUPER_CONTROL_GEM = new SuperControlGemItem();

    @Override
    public void onInitialize() {
        LOGGER.info("控制水晶开始加载");
        Identifier controlCrystalOreIdentifier = new Identifier("controlgem", "control_crystal_ore");
        //注册方块物品
        Registry.register(Registry.BLOCK, controlCrystalOreIdentifier, CONTROL_CRYSTAL_ORE_BLOCK);
        Registry.register(Registry.ITEM, controlCrystalOreIdentifier, new ControlCrystalOreBlockItem(CONTROL_CRYSTAL_ORE_BLOCK));
        registerItem("control_crystal", new ControlCrystalItem());
        registerItem("inferior_control_gem", INFERIOR_CONTROL_GEM);
        registerItem("basic_control_gem", BASIC_CONTROL_GEM);
        registerItem("intermediate_control_gem", INTERMEDIATE_CONTROL_GEM);
        registerItem("advanced_control_gem", ADVANCED_CONTROL_GEM);
        registerItem("ultimate_control_gem", ULTIMATE_CONTROL_GEM);
        registerItem("super_control_gem", SUPER_CONTROL_GEM);
        registerItem("control_gem", new ControlGemItem());
        //注册指令
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> dispatcher.register(CommandManager.literal("r").then(CommandManager.argument("target", EntityArgumentType.player())).executes(new ReportCommand())));
        ServerTickEvents.END_WORLD_TICK.register(new ServerWorldTickEvent());
        //注册控制矿石生成
        Identifier controlCrystalOreOverworldIdentifier = new Identifier("controlgem", "control_crystal_ore_overworld");
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, controlCrystalOreOverworldIdentifier, CONTROL_CRYSTAL_ORE_OVERWORLD_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, controlCrystalOreOverworldIdentifier, CONTROL_CRYSTAL_ORE_OVERWORLD_PLACED_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, controlCrystalOreOverworldIdentifier));
        ServerWorldTickEvent.words.add("含水说话");
        ServerWorldTickEvent.words.add("一分钟死5次");
        ServerWorldTickEvent.words.add("唱歌");
        LOGGER.info("控制水晶加载成功");
    }

    public void registerItem(String path, Item item) {
        Registry.register(Registry.ITEM, new Identifier("controlgem", path), item);
    }
}
