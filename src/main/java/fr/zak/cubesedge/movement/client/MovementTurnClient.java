package fr.zak.cubesedge.movement.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import fr.zak.cubesedge.MovementClient;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class MovementTurnClient extends MovementClient {

	private KeyBinding turn = new KeyBinding("Turn", Keyboard.KEY_APOSTROPHE,
			"Cube's Edge");

	@SubscribeEvent
	public void key(KeyInputEvent event) {
		if (turn.isPressed()
				&& !((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).isTurning) {
			((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player")).isTurning = true;
		}
	}
	
	@Override
	public String getName() {
		return "Turn Client";
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {
		
	}

	@Override
	public void controlClient(EntityPlayerCustom playerCustom,
			EntityPlayer player) {
		
	}
	
}
