package tk.peanut.hydrogen.module.modules.hud;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import tk.peanut.hydrogen.events.EventRender2D;
import tk.peanut.hydrogen.module.Category;
import tk.peanut.hydrogen.module.Info;
import tk.peanut.hydrogen.module.Module;
import tk.peanut.hydrogen.utils.FontHelper;
import tk.peanut.hydrogen.utils.Utils;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static tk.peanut.hydrogen.utils.Utils.addSlide;

/**
 * Created by peanut on 18/02/2021
 */
@Info(name = "Hotbar", color = -1, category = Category.Gui, description = "Shows an advanced hotbar with information")
public class Hotbar extends Module {

    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static final DateTimeFormatter timeFormat12 = DateTimeFormatter.ofPattern("h:mm a");
    static final DateTimeFormatter timeFormat24 = DateTimeFormatter.ofPattern("HH:mm");

    public Hotbar() {
        super(0x00);
    }

    @EventTarget
    public static void drawHotbar(EventRender2D e) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        EntityPlayer entityplayer = (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity();

        float needX = (Utils.getScaledRes().getScaledWidth() / 2 - 91 + entityplayer.inventory.currentItem * 20);
        float steps = 10f;

        addSlide(needX, steps);

        LocalDateTime now = LocalDateTime.now();
        String date = dateFormat.format(now);
        String time = timeFormat24.format(now);
        String fps = String.format("FPS §7%s", mc.getDebugFPS());

        String x = String.valueOf((int) mc.thePlayer.posX);
        String y = String.valueOf((int) mc.thePlayer.posY);
        String z = String.valueOf((int) mc.thePlayer.posZ);

        String coordinates = String.format("X: §7%s §fY: §7%s §fZ: §7%s", x, y, z);

        FontHelper.hfontbold.drawStringWithShadow(date, Utils.getScaledRes().getScaledWidth() - FontHelper.hfontbold.getStringWidth(date) - 9, Utils.getScaledRes().getScaledHeight() - 12, Color.white);
        FontHelper.hfontbold.drawStringWithShadow(time, Utils.getScaledRes().getScaledWidth() - FontHelper.hfontbold.getStringWidth(time) - 22, Utils.getScaledRes().getScaledHeight() - 23, Color.white);

        FontHelper.hfontbold.drawStringWithShadow(coordinates, 2, Utils.getScaledRes().getScaledHeight() - 12, Color.white);
        FontHelper.hfontbold.drawStringWithShadow(fps, 2, Utils.getScaledRes().getScaledHeight() - 23, Color.white);

    }

    public static double x = 400.0D;

    public static void addUntil(double needX, double steps) {
        if (x != needX) {
            if (x < needX)
                if (x <= needX - steps) {
                    x += steps;
                } else if (x > needX - steps) {
                    x = needX;
                }
            if (x > needX)
                if (x >= needX + steps) {
                    x -= steps;
                } else if (x < needX + steps) {
                    x = needX;
                }
        }
    }

}
