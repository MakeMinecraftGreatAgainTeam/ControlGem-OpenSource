package org.mmga.controlgem.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import org.mmga.controlgem.threads.PlayerJobThread;
import org.mmga.controlgem.utils.TitleUtils;

import java.util.*;

import static org.mmga.controlgem.ControlGem.CHANNEL_ID;

/**
 * Created On 2022/7/13 16:38
 *
 * @author wzp
 * @version 1.0.0
 */
public class ServerWorldTickEvent implements ServerTickEvents.StartWorldTick {
    public static final Map<ServerPlayerEntity, PlayerJobThread> PLAYERS_JOBS = new HashMap<>();
    public static boolean isStartChoose = false;
    public static int tick = 0;
    public static List<String> words = new ArrayList<>();
    public static PlayerManager manager = null;
    public static int choosePlayersCount = 0;
    public static int time = 0;
    public static List<ServerPlayerEntity> players = null;
    public static List<ServerPlayerEntity> choosePlayers = null;
    public static String word = null;
    private final Text split = Text.empty().append(",").setStyle(Style.EMPTY.withColor(TextColor.parse("white")));
    public static ServerPlayerEntity sender = null;

    @Override
    public void onStartTick(ServerWorld world) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        byteBuf.writeInt(PLAYERS_JOBS.size());
        for (ServerPlayerEntity player : PLAYERS_JOBS.keySet()) {
            Text name = player.getName();
            UUID uuid = player.getUuid();
            String nameString = name.getString();
            byteBuf.writeString(nameString);
            PlayerJobThread playerJobThread = PLAYERS_JOBS.get(player);
            byteBuf.writeString(playerJobThread.getWord());
            byteBuf.writeInt(playerJobThread.getFullTime());
            byteBuf.writeInt(playerJobThread.getTime());
            MinecraftServer server = player.getServer();
            assert server != null;
            PlayerManager playerManager = server.getPlayerManager();
            ServerPlayerEntity player1 = playerManager.getPlayer(name.getString());
            if (player1 != null) {
                UUID uuid1 = player1.getUuid();
                if (uuid != uuid1) {
                    playerJobThread.setEntity(player1);
                    PLAYERS_JOBS.put(player1, playerJobThread);
                    PLAYERS_JOBS.remove(player);
                }
            }
        }
        if (isStartChoose) {
            if (tick == 0 &&
                    manager != null &&
                    choosePlayersCount != 0 &&
                    players != null &&
                    choosePlayers == null &&
                    word == null &&
                    time != 0 &&
                    sender != null) {
                //启动抽取的第一个tick
                int size = players.size();
                if (size < choosePlayersCount) {
                    //人数不足
                    init();
                    return;
                }
                //人数充足时
                //内定选好的人和词
                //打乱列表
                Collections.shuffle(players);
                ArrayList<String> strings = new ArrayList<>(words);
                Collections.shuffle(strings);
                //取出需要位数的人
                choosePlayers = new ArrayList<>();
                for (int i = 0; i < choosePlayersCount; i++) {
                    choosePlayers.add(players.get(i));
                }
                //选出第一位的词
                word = strings.get(0);
                //设置标题显示时间
                TitleUtils.setAllTitleTimes(1, 4, 1, manager);
            }
            if (tick >= 0 && tick < 80) {
                //前80tick
                //每4tick展示一次
                if (tick % 4 == 0) {
                    renderRandomResult();
                }
            }
            if (tick == 80) {
                TitleUtils.setAllTitleTimes(1, 8, 1, manager);
            }
            if (tick >= 80 && tick < 140) {
                if (tick % 8 == 0) {
                    renderRandomResult();
                }
            }
            if (tick == 140) {
                TitleUtils.setAllTitleTimes(1, 40, 1, manager);
                renderRightResult();
                for (ServerPlayerEntity player : choosePlayers) {
                    PlayerJobThread playerJobThread = new PlayerJobThread(player, word, time, sender);
                    playerJobThread.start();
                }
                init();
                return;
            }
            tick++;
        }
        List<ServerPlayerEntity> playerList = world.getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : playerList) {
            ServerPlayNetworking.send(player, CHANNEL_ID, byteBuf);
        }
    }

    public void init() {
        isStartChoose = false;
        manager = null;
        choosePlayersCount = 0;
        choosePlayers = null;
        players = null;
        word = null;
        tick = 0;
        time = 0;
        sender = null;
    }

    public void renderRandomResult() {
        Collections.shuffle(players);
        Collections.shuffle(words);
        List<ServerPlayerEntity> tempPlayers = new ArrayList<>();
        String tempWord = words.get(0);
        for (int i = 0; i < choosePlayersCount; i++) {
            tempPlayers.add(players.get(i));
        }
        renderResult(tempPlayers, tempWord, SoundEvents.BLOCK_NOTE_BLOCK_BIT);
    }

    public void renderRightResult() {
        MutableText playerNames = renderResult(choosePlayers, word, SoundEvents.ENTITY_PLAYER_LEVELUP);
        manager.broadcast(Text.translatable("tip.controlgem.done", playerNames, time, word), MessageType.SYSTEM);
    }

    public MutableText renderResult(List<ServerPlayerEntity> player, String word, SoundEvent sound) {
        MutableText playersName = MutableText.of(TextContent.EMPTY);
        int i = 0;
        int playerSize = player.size();
        for (ServerPlayerEntity serverPlayerEntity : player) {
            playersName.append(serverPlayerEntity.getName()).setStyle(Style.EMPTY.withColor(TextColor.parse("blue")));
            if (i != playerSize - 1) {
                playersName.append(split);
            }
        }
        TitleUtils.sendTitle(TitleUtils.Type.TITLE, playersName, manager);
        TitleUtils.sendTitle(TitleUtils.Type.SUBTITLE, Text.of(word), manager);
        for (ServerPlayerEntity serverPlayerEntity : manager.getPlayerList()) {
            serverPlayerEntity.playSound(sound, SoundCategory.VOICE, 1.0f, 1.0f);
        }
        return playersName;
    }
}
