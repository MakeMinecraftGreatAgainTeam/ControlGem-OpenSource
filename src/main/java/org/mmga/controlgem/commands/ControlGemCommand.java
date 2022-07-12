package org.mmga.controlgem.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * Created On 2022/7/12 20:10
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGemCommand implements Command<Object> {
    @Override
    public int run(CommandContext<Object> context) throws CommandSyntaxException {

        return 0;
    }
}
