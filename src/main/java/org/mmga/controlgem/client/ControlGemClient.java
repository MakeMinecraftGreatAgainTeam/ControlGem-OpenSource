package org.mmga.controlgem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.mmga.controlgem.events.ClientOnServerTick;
import org.mmga.controlgem.events.StartClientWorldTick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_WORLD_TICK;
import static org.mmga.controlgem.ControlGem.CHANNEL_ID;

/**
 * Created On 2022/7/12 19:45
 *
 * @author wzp
 * @version 1.0.0
 */
public class ControlGemClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ControlGemClient");

    @Override
    public void onInitializeClient() {
        LOGGER.info("控制水晶客户端部分开始加载");
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL_ID, new ClientOnServerTick());
        START_WORLD_TICK.register(new StartClientWorldTick());
        LOGGER.info("控制水晶客户端部分加载成功");
    }
}
