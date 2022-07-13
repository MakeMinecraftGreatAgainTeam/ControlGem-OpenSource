package org.mmga.controlgem.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * Created On 2022/7/13 15:57
 *
 * @author wzp
 * @version 1.0.0
 */
public class TitleUtils {
    public static void sendTitle(@NotNull Type titleType, @NotNull Text content, @NotNull PlayerManager playerManager) {
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        for (ServerPlayerEntity serverPlayerEntity : playerList) {
            serverPlayerEntity.networkHandler.sendPacket(titleType.constructor.apply(content));
        }
    }

    public static void setAllTitleTimes(int fadeIn, int stay, int fadeOut, @NotNull PlayerManager playerManager) {
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        TitleFadeS2CPacket packet = new TitleFadeS2CPacket(fadeIn, stay, fadeOut);
        for (ServerPlayerEntity serverPlayerEntity : playerList) {
            serverPlayerEntity.networkHandler.sendPacket(packet);
        }
    }

    public enum Type {
        /**
         * 主标题
         */
        TITLE(TitleS2CPacket::new),
        /**
         * 副标题
         */
        SUBTITLE(SubtitleS2CPacket::new);
        private final Function<Text, Packet<?>> constructor;

        Type(Function<Text, Packet<?>> constructor) {
            this.constructor = constructor;
        }
    }
}
