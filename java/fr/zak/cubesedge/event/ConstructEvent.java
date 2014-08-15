package fr.zak.cubesedge.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class ConstructEvent {

	@SubscribeEvent
	public void construct(EntityConstructing event){
		if(event.entity != null && event.entity instanceof EntityPlayer){
			((EntityPlayer)event.entity).registerExtendedProperties("Cube's Edge Player", new EntityPlayerCustom());
		}
	}
	
}
