package fr.zak.cubesedge.movement;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fr.zak.cubesedge.Movement;
import fr.zak.cubesedge.MovementVar;
import fr.zak.cubesedge.entity.EntityPlayerCustom;

@Movement("Jump")
public class MovementJump extends MovementVar{

	public void control(EntityPlayerCustom playerCustom, EntityPlayer player, int heading){
		if(!player.capabilities.isFlying && !playerCustom.isSneaking){
			if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) - 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)){
				if((!player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (!player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (!player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (!player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)){
					if(ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41)){
						player.motionY = 0.41999998688697815D;
					}
				}
				if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)){
					playerCustom.isJumping = 3;
					if((player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ) - 1).isNormalCube() && heading == 2) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ) + 1).isNormalCube() && heading == 0) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) - 1, MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 1) || (player.worldObj.getBlock(MathHelper.floor_double(player.posX) + 1, MathHelper.floor_double(player.posY) + 1, MathHelper.floor_double(player.posZ)).isNormalCube() && heading == 3)){
						playerCustom.isJumping = 4;
					}
				}
			}

			if(player.fallDistance == 0 && !player.onGround){
				if(playerCustom.isJumping == 3){
					player.motionY *= 0.75D;
				}
				if(playerCustom.isJumping == 4){
					if(playerCustom.jumpTime < 1){
						player.motionY = 0.41999998688697815D;
						playerCustom.jumpTime++;
					}
				}
			}
			if(player.onGround || playerCustom.isTurning || playerCustom.isGrabbing){
				if(!(Boolean)ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, (EntityLivingBase)player, 41)){
					playerCustom.isJumping = 0;
				}
				playerCustom.jumpTime = 0;
			}
		}
	}
	
}
