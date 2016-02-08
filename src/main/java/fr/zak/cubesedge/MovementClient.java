package fr.zak.cubesedge;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

public abstract class MovementClient {

	public abstract void controlClient(EntityPlayerCustom playerCustom, EntityPlayer player);
	
	public abstract void renderTick(EntityPlayerCustom playerCustom);

	public abstract String getName();

	public Block getBlock(World obj, int x, int y, int z){
		return obj.getBlockState(new BlockPos(x, y, z)).getBlock();
	}
}
