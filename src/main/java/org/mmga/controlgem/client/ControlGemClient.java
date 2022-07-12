package org.mmga.controlgem.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LOGGER.info("控制水晶客户端部分加载成功");
    }
}
