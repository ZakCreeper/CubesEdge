package fr.zak.cubesedge.ticks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.IMovement;
import fr.zak.cubesedge.Util;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public class ClientTickHandler {

	private EntityPlayerCustom playerCustom;

	@SubscribeEvent
	public void playerUpdate(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (playerCustom == null) {
				playerCustom = (EntityPlayerCustom) event.player
						.getExtendedProperties("Cube's Edge Player");
			}
			for (Object o : Util.getMovements()) {
				if (!((IMovement) o).isMovementDisabled()) {
					((IMovement) o).control(playerCustom, event.player, event.side);
				}
			}
		}
	}

	@SubscribeEvent
	public void tick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START
				&& Minecraft.getMinecraft().theWorld != null) {
			playerCustom = (EntityPlayerCustom) Minecraft.getMinecraft().thePlayer
					.getExtendedProperties("Cube's Edge Player");
			for (Object o : Util.getMovements()) {
				if (!((IMovement) o).isMovementDisabled()) {
					((IMovement) o).renderTick(playerCustom);
				}
			}
		}
	}
}
