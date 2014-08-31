package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement("Slow")
public class MovementSlow extends MovementVar {

	@Override
	public void control(EntityPlayerCustom playerCustom, EntityPlayer player) {
		if (keyPressedSlow && !playerCustom.slow) {
			ObfuscationReflectionHelper.setPrivateValue(Timer.class,
					((Timer) ObfuscationReflectionHelper.getPrivateValue(
							Minecraft.class, Minecraft.getMinecraft(), 16)),
					5F, 0);
			playerCustom.slow = true;
		}
		if (playerCustom.slow) {
			playerCustom.slowTime++;
		}
		if (playerCustom.slowTime == 25 && playerCustom.slow) {
			ObfuscationReflectionHelper.setPrivateValue(Timer.class,
					((Timer) ObfuscationReflectionHelper.getPrivateValue(
							Minecraft.class, Minecraft.getMinecraft(), 16)),
					20F, 0);
			playerCustom.slowTime = 0;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = defaultSensitivity;
			keyPressedSlow = false;
			playerCustom.slow = false;
		}
	}

	private KeyBinding ralenti = new KeyBinding("Slow", Keyboard.KEY_R,
			"Cube's Edge");
	private boolean keyPressedSlow = false;
	private float defaultSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;

	@SubscribeEvent
	public void key(KeyInputEvent event) {
		keyPressedSlow = ralenti.isPressed();
		if (keyPressedSlow) {
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = 0.1F;
		}
	}

	@Override
	public void renderTick(EntityPlayerCustom playerCustom) {

	}
}
