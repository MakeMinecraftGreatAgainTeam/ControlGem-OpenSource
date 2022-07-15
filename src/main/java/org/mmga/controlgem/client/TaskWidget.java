package org.mmga.controlgem.client;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Created On 2022/7/14 22:46
 *
 * @author wzp
 * @version 1.0.0
 */
@Environment(EnvType.CLIENT)
public class TaskWidget extends WWidget {
    private static final int WHITE = 0xFFFFFF;
    private static final int MAIN_COLOR = 0xFF6754e6;
    private final String name;
    private final String word;
    private final int fullTime;
    private final int time;

    public TaskWidget(String name, String word, int fullTime, int time) {
        this.name = name;
        this.word = word;
        this.fullTime = fullTime;
        this.time = time;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int fontHeight = textRenderer.fontHeight;
        String text = name + ":" + word + "    " + time + "/" + fullTime;
        int width1 = textRenderer.getWidth(text);
        int maxW = width1 + 4;
        int abs = (int) Math.abs((float) this.time / this.fullTime * 100);
        int w = (int) (maxW * ((float) abs / 100));
        ScreenDrawing.coloredRect(matrices, x - 2, y - 2, w, fontHeight + 4, MAIN_COLOR);
        ScreenDrawing.drawString(matrices, text, x, y, WHITE);
    }
}
