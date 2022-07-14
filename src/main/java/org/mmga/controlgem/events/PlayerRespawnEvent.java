package org.mmga.controlgem.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import org.mmga.controlgem.threads.PlayerJobThread;

/**
 * Created On 2022/7/14 12:42
 *
 * @author wzp
 * @version 1.0.0
 */
public class PlayerRespawnEvent implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        EntityAttributeInstance newHealth = newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        EntityAttributeInstance oldHealth = oldPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (newHealth != null && oldHealth != null) {
            double baseValue = oldHealth.getBaseValue();
            newHealth.setBaseValue(baseValue);
            if (newPlayer.getHealth() > baseValue) {
                newPlayer.setHealth((float) baseValue);
            }
        }
        for (ServerPlayerEntity player : ServerWorldTickEvent.PLAYERS_JOBS.keySet()) {
            if (player.getUuid().equals(oldPlayer.getUuid())) {
                PlayerJobThread playerJobThread = ServerWorldTickEvent.PLAYERS_JOBS.get(player);
                playerJobThread.setEntity(newPlayer);
                ServerWorldTickEvent.PLAYERS_JOBS.put(newPlayer, playerJobThread);
            }
        }
    }
}
