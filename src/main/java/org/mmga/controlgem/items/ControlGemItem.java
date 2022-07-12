package org.mmga.controlgem.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.mmga.controlgem.ControlGem;

/**
 * Created On 2022/7/12 23:31
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGemItem extends Item {
    public ControlGemItem() {
        super(new Settings().maxCount(1).group(ControlGem.CONTROL_GEM_ITEM_GROUP));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.sendMessage(Text.translatable("tip.control_gem.used"));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
