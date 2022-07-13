package org.mmga.controlgem.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mmga.controlgem.ControlGem;
import org.mmga.controlgem.events.ServerWorldTickEvent;

import java.util.List;

/**
 * Created On 2022/7/12 22:34
 *
 * @author wzp
 * @version 1.0.0
 */
public class UsedControlGemItem extends Item {
    private final int playerCount;
    private final int keepTime;
    private final boolean self;

    public UsedControlGemItem(int playerCount, int keepTime, boolean self) {
        super(new Item.Settings().group(ControlGem.CONTROL_GEM_ITEM_GROUP).maxCount(1));
        this.playerCount = playerCount;
        this.keepTime = keepTime;
        this.self = self;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Text name = serverPlayer.getName();
            MinecraftServer server = serverPlayer.getServer();
            if (server != null) {
                PlayerManager playerManager = server.getPlayerManager();
                List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
                for (ServerPlayerEntity serverPlayerEntity : playerList) {
                    ItemCooldownManager itemCooldownManager = serverPlayerEntity.getItemCooldownManager();
                    itemCooldownManager.set(ControlGem.INFERIOR_CONTROL_GEM, 2 * 20);
                    itemCooldownManager.set(ControlGem.BASIC_CONTROL_GEM, 2 * 20);
                    itemCooldownManager.set(ControlGem.INTERMEDIATE_CONTROL_GEM, 2 * 20);
                    itemCooldownManager.set(ControlGem.ADVANCED_CONTROL_GEM, 2 * 20);
                    itemCooldownManager.set(ControlGem.ULTIMATE_CONTROL_GEM, 2 * 20);
                    itemCooldownManager.set(ControlGem.SUPER_CONTROL_GEM, 2 * 20);
                }
                playerManager.broadcast(Text.translatable("tip.used_control_gem.used", name), MessageType.SYSTEM);
                if (!self) {
                    playerList.remove(serverPlayer);
                }
                ServerWorldTickEvent.playerList = playerList;
                ServerWorldTickEvent.isChou = true;
            }
            return TypedActionResult.success(new ItemStack(Items.AIR));
        } else {
            return super.use(world, player, hand);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.self) {
            tooltip.add(Text.translatable("tooltip.used_control_gem.self", playerCount, keepTime));
        } else {
            tooltip.add(Text.translatable("tooltip.used_control_gem.other", playerCount, keepTime));
        }

    }
}
