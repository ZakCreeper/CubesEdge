package fr.zak.cubesedge.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {
	
	static KeyBinding ralenti = new KeyBinding("Ralenti", Keyboard.KEY_R, "Mirror's Edge Mod");
	public static boolean keyPressedRalenti = false;
	public static float defaultSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
	
	public KeyHandler(){
		ClientRegistry.registerKeyBinding(ralenti);
	}
	
	@SubscribeEvent
	public void key(KeyInputEvent event){
		keyPressedRalenti = ralenti.isPressed();
		if(keyPressedRalenti){
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = 0.1F;
		}
	}
}
