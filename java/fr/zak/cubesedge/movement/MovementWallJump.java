package fr.zak.cubesedge.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement(Side.CLIENT)
public class MovementWallJump {

	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, int heading){
		if(!player.capabilities.isFlying && !playerCustom.isSneaking){
			if(!player.onGround && player.motionY <= 0){
				if((player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube()) && (( heading == 0) || (heading == 2))){
					playerCustom.isOnWall = true;
					if(player.moveForward > 0){
						if(player instanceof EntityPlayerSP){
							if(((EntityPlayerSP)player).movementInput.jump && !playerCustom.wallJump){
								playerCustom.animRight = false;
								playerCustom.animLeft = false;
								if(heading == 0){
									player.motionZ = 0.7D;
									player.motionX = -0.2D;
								}
								if(heading == 2){
									player.motionZ = -0.7D;
									player.motionX = -0.2D;
								}
								player.motionY = 0.41999998688697815D;
								playerCustom.wallJump = true;
							}
							if(!((EntityPlayerSP)player).movementInput.jump){
								if(heading == 2){
									playerCustom.animRight = true;
								}
								if(heading == 0){
									playerCustom.animLeft = true;

								}
							}
						}
					}
				}
				else if((player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube()) && ((heading == 0) || (heading == 2))){
					playerCustom.isOnWall = true;
					if(player.moveForward > 0){
						if(player instanceof EntityPlayerSP){
							if(((EntityPlayerSP)player).movementInput.jump && !playerCustom.wallJump){
								playerCustom.animRight = false;
								playerCustom.animLeft = false;
								if(heading == 0){
									player.motionZ = 0.7D;
									player.motionX = 0.2D;
								}
								if(heading == 2){
									player.motionZ = -0.7D;
									player.motionX = 0.2D;
								}
								player.motionY = 0.41999998688697815D;
								playerCustom.wallJump = true;
							}
							if(!((EntityPlayerSP)player).movementInput.jump){
								if(heading == 2){
									playerCustom.animLeft = true;
								}
								if(heading == 0){
									playerCustom.animRight = true;
								}
							}
						}
					}
				}
				else if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) + 1).isNormalCube()) && ((heading == 3) || (heading == 1))){
					playerCustom.isOnWall = true;
					if(player.moveForward > 0){
						if(player instanceof EntityPlayerSP){
							if(((EntityPlayerSP)player).movementInput.jump && !playerCustom.wallJump){
								playerCustom.animRight = false;
								playerCustom.animLeft = false;
								if(heading == 3){
									player.motionX = 0.7D;
									player.motionZ = -0.2D;
								}
								if(heading == 1){
									player.motionX = -0.7D;
									player.motionZ = -0.2D;
								}
								player.motionY = 0.41999998688697815D;
								playerCustom.wallJump = true;
							}
							if(!((EntityPlayerSP)player).movementInput.jump){
								if(heading == 3){
									playerCustom.animRight = true;
								}
								if(heading == 1){
									playerCustom.animLeft = true;
								}
							}
						}
					}
				}
				else if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() || player.worldObj.getBlock(MathHelper.floor_double(player.posX) , MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) - 1).isNormalCube()) && ((heading == 3) || (heading == 1))){
					playerCustom.isOnWall = true;
					if(player.moveForward > 0){
						if(player instanceof EntityPlayerSP){
							if(((EntityPlayerSP)player).movementInput.jump && !playerCustom.wallJump){
								playerCustom.animRight = false;
								playerCustom.animLeft = false;
								if(heading == 3){
									player.motionX = 0.7D;
									player.motionZ = 0.2D;
								}
								if(heading == 1){
									player.motionX = -0.7D;
									player.motionZ = 0.2D;
								}
								player.motionY = 0.41999998688697815D;
								playerCustom.wallJump = true;
							}
							if(!((EntityPlayerSP)player).movementInput.jump){
								if(heading == 3){
									playerCustom.animLeft = true;
								}
								if(heading == 1){
									playerCustom.animRight = true;
								}
							}
						}
					}
				}
				else {
					playerCustom.isOnWall = false;
				}
				if(playerCustom.isOnWall && player.moveForward > 0){
					player.motionZ *= 0.95D;
					player.motionX *= 0.95D;
					player.motionY *= 0.75D;
				}
			}
			else {
				playerCustom.isOnWall = false;
			}
		}
		if(player.onGround){
			playerCustom.wallJump = false;
			playerCustom.animRight = false;
			playerCustom.animLeft = false;
			playerCustom.isJumpingOnWall = false;
		}
		if(player.capabilities.isFlying){
			playerCustom.animRight = false;
			playerCustom.animLeft = false;
		}
	}
}
