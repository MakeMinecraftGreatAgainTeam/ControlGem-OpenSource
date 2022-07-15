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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.mmga.controlgem.events.ServerWorldTickEvent;
import org.mmga.controlgem.threads.PlayerJobThread;

import java.util.*;

/**
 * Created On 2022/7/12 23:51
 *
 * @author wzp
 * @version 1.0.0
 */
public class ReportCommand implements Command<ServerCommandSource> {
    public static final Map<ServerPlayerEntity, Integer> playerCount = new HashMap<>();
    public static final Map<UUID, ArrayList<ServerPlayerEntity>> playersReport = new HashMap<>();

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        PlayerJobThread playerJobThread = ServerWorldTickEvent.PLAYERS_JOBS.get(target);
        ServerCommandSource source = context.getSource();
        if (playerJobThread == null) {
            source.sendFeedback(Text.translatable("tip.controlgem.unknown"), false);
            return 1;
        }
        int count = playerCount.getOrDefault(target, 0) + 1;
        playerCount.put(target, count);
        UUID sender = source.getChatMessageSender().uuid();
        ArrayList<ServerPlayerEntity> list = playersReport.getOrDefault(sender, new ArrayList<>());
        if (list.contains(target)) {
            source.sendFeedback(Text.translatable("tip.controlgem.report.already"), false);
            return 1;
        }
        list.add(target);
        playersReport.put(sender, list);
        MinecraftServer server = target.getServer();
        assert server != null;
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        int size = playerList.size();
        if (count >= size / 2.0f) {
            playerJobThread.failed();
            playerManager.broadcast(Text.translatable("tip.controlgem.failed", target.getName()), MessageType.SYSTEM);
            for (ServerPlayerEntity player : playerManager.getPlayerList()) {
                player.playSound(SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.VOICE, 1.0f, 1.0f);
            }
        }
        source.sendFeedback(Text.translatable("tip.controlgem.report.success", target.getName()), true);
        return 1;
    }
}
