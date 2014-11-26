package fr.zak.cubesedge.ticks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.zak.cubesedge.MovementClient;
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
					((MovementClient) o).renderTick(player);
//				}
			}
		}
	}
}
