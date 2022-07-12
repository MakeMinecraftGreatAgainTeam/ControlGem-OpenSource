package org.mmga.controlgem.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.mmga.controlgem.ControlGem;

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

    public UsedControlGemItem(int playerCount, int keepTime) {
        super(new Item.Settings().group(ControlGem.CONTROL_GEM_ITEM_GROUP).maxCount(1));
        this.playerCount = playerCount;
        this.keepTime = keepTime;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player instanceof ServerPlayerEntity serverPlayer) {

        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.used_control_gem", playerCount, keepTime));
    }
}
