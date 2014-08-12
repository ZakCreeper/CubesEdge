package fr.zak.cubesedge.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class KeyHandler {
	
	static KeyBinding ralenti = new KeyBinding("Ralenti", Keyboard.KEY_R, "Cube's Edge");
	static KeyBinding turn = new KeyBinding("Turn", Keyboard.KEY_APOSTROPHE, "Cube's Edge");
	public static boolean keyPressedRalenti = false;
	public static float defaultSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
	
	public KeyHandler(){
		ClientRegistry.registerKeyBinding(ralenti);
		ClientRegistry.registerKeyBinding(turn);
	}
	
	@SubscribeEvent
	public void key(KeyInputEvent event){
		keyPressedRalenti = ralenti.isPressed();
		if(keyPressedRalenti){
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = 0.1F;
		}
		if(turn.isPressed() && !((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Player Custom")).isTurning){
			((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Player Custom")).isTurning = true;
		}
	}
}
