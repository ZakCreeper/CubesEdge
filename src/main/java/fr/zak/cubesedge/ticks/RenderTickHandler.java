package fr.zak.cubesedge.ticks;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fr.zak.cubesedge.IMovementClient;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class RenderTickHandler {

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START
				&& Minecraft.getMinecraft().theWorld != null) {
			for (Object o : Util.getClientsMovements()) {
				EntityPlayerCustom player = ((EntityPlayerCustom)Minecraft.getMinecraft().thePlayer.getExtendedProperties("Cube's Edge Player"));
//				if (!((IMovementClient) o).isMovementDisabled()) {
					((IMovementClient) o).renderTick(player);
//				}
			}
		}
	}
}
