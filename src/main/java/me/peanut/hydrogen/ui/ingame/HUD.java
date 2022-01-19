package me.peanut.hydrogen.ui.ingame;

import com.darkmagician6.eventapi.EventTarget;
import me.peanut.hydrogen.events.EventUpdate;
import me.peanut.hydrogen.font.FontHelper;
import me.peanut.hydrogen.module.Category;
import me.peanut.hydrogen.module.Info;
import me.peanut.hydrogen.module.Module;
import me.peanut.hydrogen.Hydrogen;
import me.peanut.hydrogen.settings.Setting;
import me.peanut.hydrogen.ui.ingame.style.Style;
import me.peanut.hydrogen.ui.ingame.style.styles.Classic;
import me.peanut.hydrogen.ui.ingame.style.styles.New;

import java.util.ArrayList;

@Info(name = "HUD", description = "The overlay", category = Category.Gui)
public class HUD extends Module {

    public Style style;

    // this module basically only exists to have general settings, also to disable the hud alltogether

    public HUD() {
        ArrayList<String> style = new ArrayList<>();
        style.add("Classic");
        style.add("New");
        ArrayList<String> time = new ArrayList<>();
        time.add("24H");
        time.add("12H");

        addSetting(new Setting("Time Format", this, "24H", time));
        addSetting(new Setting("Style", this, "New", style));
        this.style = new New();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        this.updateStyles();
    }

    public void updateStyles() {
        switch(h2.settingsManager.getSettingByName(this, "Style").getMode()) {
            case "Classic":
                Hydrogen.getClient().moduleManager.getModules().sort((m1, m2) -> Integer.compare(mc.fontRendererObj.getStringWidth(m2.getName()), mc.fontRendererObj.getStringWidth(m1.getName())));
                h2.hud.style = new Classic();
                break;
            case "New":
                Hydrogen.getClient().moduleManager.getModules().sort((m1, m2) -> Integer.compare(FontHelper.sf_l.getStringWidth(m2.getName()), FontHelper.sf_l.getStringWidth(m1.getName())));
                h2.hud.style = new New();
                break;
        }
    }

}
