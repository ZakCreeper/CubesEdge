package fr.zak.cubesedge.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class ClickEvent {

	@SubscribeEvent
	public void onClick(MouseEvent event){
		if(((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Player Custom")).isGrabbing){
			event.setCanceled(true);
		}
	}
}