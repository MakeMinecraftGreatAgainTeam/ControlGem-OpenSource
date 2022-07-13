package org.mmga.controlgem.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created On 2022/7/12 23:51
 *
 * @author wzp
 * @version 1.0.0
 */
public class ReportCommand implements Command<ServerCommandSource> {
    Map<ServerPlayerEntity, Integer> playerCount = new HashMap<>();
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        int count = playerCount.getOrDefault(target, 0) + 1;
        playerCount.put(target, count);
        MinecraftServer server = target.getServer();
        assert server != null;
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        int size = playerList.size();
        if (count >= size / 2) {
            playerManager.broadcast(Text.translatable("tip.controlgem.failed", target.getName()), MessageType.SYSTEM);
            playerCount.remove(target);
        }
        return 1;
    }
}
