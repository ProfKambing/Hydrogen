package me.peanut.hydrogen.injection.mixins;

import com.darkmagician6.eventapi.EventManager;
import me.peanut.hydrogen.Hydrogen;
import me.peanut.hydrogen.events.EventKey;
import me.peanut.hydrogen.events.EventMouseClick;
import me.peanut.hydrogen.events.EventTick;
import me.peanut.hydrogen.file.files.*;
import me.peanut.hydrogen.injection.interfaces.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public class MixinMinecraft implements IMixinMinecraft {
    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    @Mutable
    public Session session;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void minecraftConstructor(GameConfiguration gameConfig, CallbackInfo ci) {
        new Hydrogen();
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startGame(CallbackInfo ci) {
        Hydrogen.getClient().startClient();
        KeybindFile.loadKeybinds();
        SettingsButtonFile.loadState();
        SettingsComboBoxFile.loadState();
        SettingsSliderFile.loadState();
        ClickGuiFile.loadClickGui();
        ModuleFile.loadModules();
        VisibleFile.loadState();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void onKey(CallbackInfo ci) {
        if (Keyboard.getEventKeyState() && currentScreen == null && !Hydrogen.getClient().panic) {
            EventManager.call(new EventKey(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void clickMouse(CallbackInfo ci) {
        if(!Hydrogen.getClient().panic) {
            EventMouseClick e = new EventMouseClick();
            EventManager.register(e);
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        if(!Hydrogen.getClient().panic) {
            Hydrogen.getClient().stopClient();
            KeybindFile.saveKeybinds();
            SettingsButtonFile.saveState();
            SettingsComboBoxFile.saveState();
            SettingsSliderFile.saveState();
            ClickGuiFile.saveClickGui();
            ModuleFile.saveModules();
            VisibleFile.saveState();
        }
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift = At.Shift.BEFORE))
    private void onTick(final CallbackInfo callbackInfo) {
        if(!Hydrogen.getClient().panic) {
            EventTick e = new EventTick();
            EventManager.call(e);
        }
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }


}
