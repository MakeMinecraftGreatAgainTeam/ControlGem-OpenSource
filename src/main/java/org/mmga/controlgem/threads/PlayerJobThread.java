package org.mmga.controlgem.threads;

import net.minecraft.server.network.ServerPlayerEntity;
import org.mmga.controlgem.events.ServerWorldTickEvent;

/**
 * Created On 2022/7/13 23:43
 *
 * @author wzp
 * @version 1.0.0
 */
public class PlayerJobThread extends Thread {
    private final ServerPlayerEntity entity;
    private final String word;
    private int time;

    public PlayerJobThread(ServerPlayerEntity entity, String word, int time) {
        this.entity = entity;
        this.word = word;
        this.time = time;
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
        if (!entity.equals(that.entity)) {
            return false;
        }
        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        int result = entity.hashCode();
        result = 31 * result + word.hashCode();
        result = 31 * result + time;
        return result;
    }

    @Override
    public void run() {
        ServerWorldTickEvent.PLAYERS_JOBS.put(this.getEntity(), this);
        while (this.time != 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.time--;
        }
        ServerWorldTickEvent.PLAYERS_JOBS.remove(this.getEntity());
    }

    public ServerPlayerEntity getEntity() {
        return entity;
    }

    public String getWord() {
        return word;
    }

    public int getTime() {
        return time;
    }
}
