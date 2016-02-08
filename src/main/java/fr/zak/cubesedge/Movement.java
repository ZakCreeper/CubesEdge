package fr.zak.cubesedge;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public abstract class Movement {

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
	
	public Block getBlock(World obj, int x, int y, int z){
		return obj.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public abstract void control(EntityPlayerCustom playerCustom,
			EntityPlayer player, Side side);
	
	public abstract String getName();
}