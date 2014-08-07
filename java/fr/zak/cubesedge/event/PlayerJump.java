package fr.zak.cubesedge.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Util;

public class PlayerJump {

	@SubscribeEvent
	public void jump(LivingJumpEvent event){
		if(event.entityLiving instanceof EntityPlayer && Util.isRolling){
			event.entityLiving.motionY = 0;
		}
	}
	
}
