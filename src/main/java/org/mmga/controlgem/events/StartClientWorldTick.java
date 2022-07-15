package org.mmga.controlgem.events;

import io.github.cottonmc.cotton.gui.client.CottonHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import org.mmga.controlgem.client.TaskWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created On 2022/7/15 11:26
 *
 * @author wzp
 * @version 1.0.0
 */
@Environment(EnvType.CLIENT)
public class StartClientWorldTick implements ClientTickEvents.StartWorldTick {
    public static List<ClientOnServerTick.Data> data = new ArrayList<>();
    private final ArrayList<TaskWidget> tasks = new ArrayList<>();

    @Override
    public void onStartTick(ClientWorld world) {
        for (TaskWidget task : tasks) {
            CottonHud.remove(task);
        }
        tasks.clear();
        int i = data.size();
        for (int i1 = 0; i1 < i; i1++) {
            ClientOnServerTick.Data d = data.get(i1);
            String name = d.getName();
            String word = d.getWord();
            int fullTime = d.getFullTime();
            int time = d.getTime();
            TaskWidget taskWidget = new TaskWidget(name, word, fullTime, time);
            tasks.add(taskWidget);
            CottonHud.add(taskWidget, 10, 10 + 15 * i1);
        }
        data.clear();
    }
}
