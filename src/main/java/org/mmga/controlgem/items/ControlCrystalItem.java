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
 * Created On 2022/7/12 20:42
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlCrystalItem extends Item {
    public ControlCrystalItem() {
        super(new Item.Settings().group(ControlGem.CONTROL_GEM_ITEM_GROUP));
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.kill();
            serverPlayer.sendMessage(Text.translatable("tip.control_crystal.used"));
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
