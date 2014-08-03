package fr.zak.cubesedge.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SpeedEvent {
	public static float speed = 1;

	@SubscribeEvent
	public void walk(LivingUpdateEvent event){
		if(event.entityLiving instanceof EntityPlayer && event.entityLiving != null){
			if(((EntityPlayer)event.entityLiving).isSprinting()){
				if(speed < 1.15){
					speed += 0.005F;
				}
				if(speed < 1.20 && speed >= 1.15){
					speed += 0.002F;
				}
				if(speed < 1.30 && speed >= 1.20){
					speed += 0.001F;
				}
				if(speed < 1.32 && speed >= 1.30){
					speed += 0.0002F;
				}
				if(((EntityPlayer)event.entityLiving).onGround){
					((EntityPlayer)event.entityLiving).motionX *= speed;
					((EntityPlayer)event.entityLiving).motionZ *= speed;
				}
				if(!((EntityPlayer)event.entityLiving).onGround){
					((EntityPlayer)event.entityLiving).motionX *= ((double)((speed - 1) / 2)) + 0.9;
					((EntityPlayer)event.entityLiving).motionZ *= ((double)((speed - 1) / 2)) + 0.9;
				}
			}
			else{
				speed = 1;
			}
		}
	}

}