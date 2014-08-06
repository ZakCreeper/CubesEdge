package fr.zak.cubesedge.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.Util;

public class PlayerFall {

	@SubscribeEvent
	public void onFall(LivingFallEvent event){
		if(Util.prevRolling && event.entity instanceof EntityPlayer){
			event.distance = 0;
		}
	}
}
