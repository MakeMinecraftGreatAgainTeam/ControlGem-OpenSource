package org.mmga.controlgem.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.mmga.controlgem.utils.TitleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created On 2022/7/13 16:38
 *
 * @author wzp
 * @version 1.0.0
 */
public class ServerWorldTickEvent implements ServerTickEvents.EndWorldTick {
    public static boolean isChou = false;
    public static int tickChou = 0;
    public static ServerPlayerEntity luckPlayer = null;
    public static String luckWord = null;
    public static List<String> words = new ArrayList<>();
    public static List<ServerPlayerEntity> playerList = null;

    @Override
    public void onEndTick(ServerWorld world) {
        Random random = new Random();
        if (isChou && playerList != null) {
            MinecraftServer server = world.getServer();
            PlayerManager playerManager = server.getPlayerManager();
            if (tickChou >= 0 && tickChou < 80) {
                if (luckPlayer == null && luckWord == null) {
                    int size = playerList.size();
                    if (size == 1) {
                        luckPlayer = playerList.get(0);
                    } else if (size == 0) {
                        playerManager.broadcast(Text.translatable("tip.controlgem.insufficient"), MessageType.SYSTEM);
                        luckPlayer = null;
                        luckWord = null;
                        playerList = null;
                        tickChou = 0;
                    } else {
                        luckPlayer = playerList.get(random.nextInt(size - 1));
                    }
                    luckWord = words.get(random.nextInt(words.size()));
                } else {
                    if (tickChou % 4 == 0) {
                        showRandom(playerList, random, playerManager);
                    }
                }
            }
            if (tickChou >= 80 && tickChou < 140) {
                if (tickChou % 8 == 0) {
                    showRandom(playerList, random, playerManager);
                }
            }
            if (tickChou >= 140 && tickChou < 160) {
                if (tickChou % 10 == 0) {
                    showRandom(playerList, random, playerManager);
                }
            }
            if (tickChou == 160) {
                TitleUtils.setAllTitleTimes(1, 20, 1, playerManager);
                TitleUtils.sendTitle(TitleUtils.Type.TITLE, luckPlayer.getName(), playerManager);
                TitleUtils.sendTitle(TitleUtils.Type.SUBTITLE, Text.of(luckWord), playerManager);
                luckPlayer = null;
                luckWord = null;
                playerList = null;
                tickChou = 0;
            }
            tickChou++;
        }
    }

    public void showRandom(List<ServerPlayerEntity> playerList, Random random, PlayerManager playerManager) {
        int size = playerList.size();
        ServerPlayerEntity show;
        if (size == 1) {
            show = playerList.get(0);
        } else {
            show = playerList.get(random.nextInt(size - 1));
        }
        TitleUtils.setAllTitleTimes(1, 20, 1, playerManager);
        TitleUtils.sendTitle(TitleUtils.Type.TITLE, show.getName(), playerManager);
        TitleUtils.sendTitle(TitleUtils.Type.SUBTITLE, Text.of(words.get(random.nextInt(words.size()))), playerManager);
    }
}
