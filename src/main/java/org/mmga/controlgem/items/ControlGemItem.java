package org.mmga.controlgem.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class ControlGemItem extends Item {
    public ControlGemItem() {
        super(new Item.Settings().group(ControlGem.CONTROL_GEM_ITEM_GROUP));
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.kill();
        player.sendMessage(Text.of("谁让你没事瞎玩的？"));
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
