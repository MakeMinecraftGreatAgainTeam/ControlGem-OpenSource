package org.mmga.controlgem.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.mmga.controlgem.ControlGem;

/**
 * Created On 2022/7/12 20:37
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGemOreBlockItem extends BlockItem {
    public ControlGemOreBlockItem(Block block) {
        super(block, new Item.Settings().group(ControlGem.CONTROL_GEM_ITEM_GROUP));
    }
}
