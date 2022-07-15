package org.mmga.controlgem.threads;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.message.MessageType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.mmga.controlgem.commands.ReportCommand;
import org.mmga.controlgem.events.ServerWorldTickEvent;
import org.mmga.controlgem.utils.TitleUtils;

import java.util.*;

/**
 * Created On 2022/7/13 23:43
 *
 * @author wzp
 * @version 1.0.0
 */
public class PlayerJobThread extends Thread {
    public static HashMap<ServerPlayerEntity, Integer> scores = new HashMap<>();
    private final ServerPlayerEntity source;
    private ServerPlayerEntity entity;
    private final String word;
    private int time;
    private final int full_time;
    private boolean isFailed;

    public PlayerJobThread(ServerPlayerEntity entity, String word, int time, ServerPlayerEntity source) {
        this.entity = entity;
        this.word = word;
        this.time = time;
        this.source = source;
        this.full_time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerJobThread that = (PlayerJobThread) o;

        if (time != that.time) {
            return false;
        }
        if (isFailed != that.isFailed) {
            return false;
        }
        if (!entity.equals(that.entity)) {
            return false;
        }
        if (!source.equals(that.source)) {
            return false;
        }
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        int result = entity.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + word.hashCode();
        result = 31 * result + time;
        result = 31 * result + (isFailed ? 1 : 0);
        return result;
    }

    @Override
    public void run() {
        ServerWorldTickEvent.PLAYERS_JOBS.put(this.getEntity(), this);
        MinecraftServer server = entity.getServer();
        assert server != null;
        PlayerManager manager = server.getPlayerManager();
        Text name = entity.getName();
        String show = name.getString() + ":" + word;
        while (this.time != 0 && !isFailed) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.time--;
        }
        if (!isFailed) {
            manager.broadcast(Text.translatable("tip.controlgem.success", name), MessageType.SYSTEM);
            scores.put(entity, scores.getOrDefault(entity, 0) + 1);
            scores.put(source, scores.getOrDefault(source, 0) - 1);
            EntityAttributeInstance health = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (health != null) {
                float healthy = (float) (health.getValue() + 2);
                health.setBaseValue(healthy);
                if (entity.getHealth() < healthy) {
                    entity.setHealth(healthy);
                }
            }
        } else {
            scores.put(entity, scores.getOrDefault(entity, 0) - 1);
            scores.put(source, scores.getOrDefault(source, 0) + 1);
        }
        ServerScoreboard serverScoreboard = server.getScoreboard();
        ScoreboardObjective scoreObjective = serverScoreboard.getObjective("score");
        if (scoreObjective == null) {
            scoreObjective = serverScoreboard.addObjective("score", ScoreboardCriterion.DUMMY, Text.empty(), ScoreboardCriterion.RenderType.INTEGER);
            serverScoreboard.setObjectiveSlot(Scoreboard.LIST_DISPLAY_SLOT_ID, scoreObjective);
        }
        for (ServerPlayerEntity player : manager.getPlayerList()) {
            if (!isFailed) {
                player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.VOICE, 1.0f, 1.0f);
            }
            int score = scores.getOrDefault(player, 0);
            serverScoreboard.getPlayerScore(player.getName().getString(), scoreObjective).setScore(score);
            if (score == 10) {
                TitleUtils.setAllTitleTimes(10, 80, 10, manager);
                TitleUtils.sendTitle(TitleUtils.Type.TITLE, Text.translatable("tip.controlgem.win", player.getName()), manager);
                List<ServerPlayerEntity> playerList = manager.getPlayerList();
                for (ServerPlayerEntity p : playerList) {
                    p.changeGameMode(GameMode.SPECTATOR);
                }
            }
        }
        ServerWorldTickEvent.PLAYERS_JOBS.remove(this.getEntity());
        ReportCommand.playerCount.put(entity, 0);
        for (UUID uuid : ReportCommand.playersReport.keySet()) {
            ArrayList<ServerPlayerEntity> serverPlayerEntities = ReportCommand.playersReport.get(uuid);
            serverPlayerEntities.remove(entity);
        }
    }

    public ServerPlayerEntity getEntity() {
        return entity;
    }

    public String getWord() {
        return word;
    }

    public void setEntity(ServerPlayerEntity entity) {
        this.entity = entity;
    }

    public int getFullTime() {
        return full_time;
    }

    public int getTime() {
        return time;
    }

    public void failed() {
        this.isFailed = true;
        EntityAttributeInstance health = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (health != null) {
            float healthy = (float) (health.getValue() - 2);
            health.setBaseValue(healthy);
            if (entity.getHealth() > healthy) {
                entity.setHealth(healthy);
            }
        }
        PlayerInventory inventory = entity.getInventory();
        inventory.removeStack(new Random().nextInt(36));
    }
}
