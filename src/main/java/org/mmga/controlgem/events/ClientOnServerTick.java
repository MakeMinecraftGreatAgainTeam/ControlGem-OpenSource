package org.mmga.controlgem.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created On 2022/7/14 22:07
 *
 * @author wzp
 * @version 1.0.0
 */
@Environment(EnvType.CLIENT)
public class ClientOnServerTick implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int i = buf.readInt();
        List<Data> ds = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            Data data = new Data(buf);
            ds.add(data);
        }
        StartClientWorldTick.data = ds;
    }

    static class Data {
        private final String name;
        private final String word;
        private final int fullTime;
        private final int time;

        Data(PacketByteBuf buf) {
            this.name = buf.readString();
            this.word = buf.readString();
            this.fullTime = buf.readInt();
            this.time = buf.readInt();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Data data = (Data) o;

            if (fullTime != data.fullTime) {
                return false;
            }
            if (time != data.time) {
                return false;
            }
            if (!name.equals(data.name)) {
                return false;
            }
            return word.equals(data.word);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + word.hashCode();
            result = 31 * result + fullTime;
            result = 31 * result + time;
            return result;
        }

        public String getName() {
            return name;
        }

        public String getWord() {
            return word;
        }

        public int getFullTime() {
            return fullTime;
        }

        public int getTime() {
            return time;
        }
    }
}
