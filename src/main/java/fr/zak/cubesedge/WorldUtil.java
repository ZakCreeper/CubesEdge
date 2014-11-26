package fr.zak.cubesedge;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil {
	
	public static IBlockState getBlockState(World w, int x, int y, int z)
	{
		return w.getBlockState(new BlockPos(x, y, z));
	}
	
	public static Block getBlock(World w, int x, int y, int z)
	{
		return getBlockState(w, x, y, z).getBlock();
	}
	

}
