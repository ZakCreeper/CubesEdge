package fr.zak.cubesedge.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement(name = "Turn")
public class MovementTurn {

	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, int heading){
		if(!player.capabilities.isFlying && !playerCustom.isSneaking){
			if(playerCustom.isTurning){
				if(!playerCustom.isOnWall){
					float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw);
					player.rotationYaw = yaw - 180;
					playerCustom.isTurning = false;
					if(playerCustom.isJumping != 0 && playerCustom.turningTime == 0 && !playerCustom.isTurningOnWall){
						playerCustom.isTurningOnWall = true;
					}
				}
				else{
					if(playerCustom.turningTime == 0 && !playerCustom.isTurningOnWall){
						float yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw);
						if((player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 3)){
							player.rotationYaw = yaw - 90;
						}
						else if((player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 3)){
							player.rotationYaw = yaw + 90;
						}
						playerCustom.isTurningOnWall = true;
					}
				}
			}
			if(playerCustom.isTurningOnWall){
				if(playerCustom.turningTime < 10){
					playerCustom.turningTime++;
				}
				if(playerCustom.turningTime == 10){
					playerCustom.isTurning = false;
					playerCustom.turningTime = 0;
					playerCustom.isTurningOnWall = false;
				}
				if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ )).isNormalCube() && heading == 3)) {
					player.motionZ *= 0.95D;
					player.motionX *= 0.95D;
					player.motionY *= 0.75D;
					if(player instanceof EntityPlayerSP) {
						if(((EntityPlayerSP)player).movementInput.jump && !playerCustom.isJumpingOnWall){
							if(heading == 0){
								player.motionZ = 0.7F;
							}
							if(heading == 1){
								player.motionX = -0.7F;
							}
							if(heading == 2){
								player.motionZ = -0.7F;
							}
							if(heading == 3){
								player.motionX = 0.7F;
							}
							player.motionY = 0.7D;
							playerCustom.isJumpingOnWall = true;
						}
					}
				}
			}
		}
	}

}
