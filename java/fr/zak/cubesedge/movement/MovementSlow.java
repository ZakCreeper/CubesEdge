package fr.zak.cubesedge.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.entity.EntityPlayerCustom;
import fr.zak.cubesedge.event.KeyHandler;

@Movement(name = "Slow")
public class MovementSlow {

	public void control(EntityPlayerCustom playerCustom, EntityPlayer player){
		if(KeyHandler.keyPressedSlow && !playerCustom.slow){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 5F, 0);
			playerCustom.slow = true;
		}
		if(playerCustom.slow){
			playerCustom.slowTime++;
		}
		if(playerCustom.slowTime == 25 && playerCustom.slow){
			ObfuscationReflectionHelper.setPrivateValue(Timer.class, ((Timer)ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 15)), 20F, 0);
			playerCustom.slowTime = 0;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = KeyHandler.defaultSensitivity;
			KeyHandler.keyPressedSlow = false;
			playerCustom.slow = false;
		}
	}
}
