package fr.zak.cubesedge;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public abstract class IMovement {

	private boolean disabled;

	public boolean isMovementDisabled() {
		return disabled;
	}

	public void enable() {
		if (disabled) {
			disabled = false;
		}
	}

	public void disable() {
		if (!disabled) {
			disabled = true;
		}
	}

	public abstract void control(EntityPlayerCustom playerCustom,
			EntityPlayer player, Side side);

	public abstract void renderTick(EntityPlayerCustom playerCustom);
	
	public abstract String getName();
}