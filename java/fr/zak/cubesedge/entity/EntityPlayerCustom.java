package fr.zak.cubesedge.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityPlayerCustom implements IExtendedEntityProperties {

	public int isJumping = 0;
	public int jumpTime = 0;
	public boolean isJumpingOnWall = false;
	
	public boolean prevRolling = false;
	public int rollingTime = 0;
	public boolean isRolling = false;

	public boolean isGrabbing = false;
	public boolean[] grabbingDirections = {false, false, false, false};
	public byte[] grabbingDirectionsByte = {0, 0, 0, 0}; 

	public boolean beginingRunning = false;
	public float tickRunningLeft = 0;
	public float tickRunningRight = 0;
	public boolean animRunnig = false;
	public boolean backLeft = false;
	public boolean backRight = false;

	public int temps = 0;
	public boolean ralenti = false;

	public boolean isOnWall = false;
	public boolean wallJump = false;
	public boolean animRight = false;
	public boolean animLeft = false;
	
	public boolean isSneaking = false;
	public int sneakTime = 0; 
	public boolean wasSprinting = false;
	
	public boolean isTurning = false;
	public boolean isTurningOnWall = false;
	public int turningTime = 0;
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("isJumping", this.isJumping);
		compound.setInteger("jumpTime", this.jumpTime);
		compound.setBoolean("isJumpingOnWall", this.isJumpingOnWall);
		compound.setBoolean("prevRolling", this.prevRolling);
		compound.setInteger("rollingTime", this.rollingTime);
		compound.setBoolean("isRolling", this.isRolling);
		compound.setBoolean("isGrabbing", this.isGrabbing);
		for(int i = 0; i < this.grabbingDirections.length; i++){
			if(this.grabbingDirections[i]){
				this.grabbingDirectionsByte[i] = 1;
			}
			if(!this.grabbingDirections[i]){
				this.grabbingDirectionsByte[i] = 0;
			}
		}
		compound.setByteArray("grabbingDirections", this.grabbingDirectionsByte);
		compound.setBoolean("beginingRunning", this.beginingRunning);
		compound.setFloat("tickRunningLeft", this.tickRunningLeft);
		compound.setFloat("tickRunningRight", this.tickRunningRight);
		compound.setBoolean("animRunning", this.animRunnig);
		compound.setBoolean("backLeft", this.backLeft);
		compound.setBoolean("backRight", this.backRight);
		compound.setInteger("temps", this.temps);
		compound.setBoolean("ralenti", this.ralenti);
		compound.setBoolean("isOnWall", this.isOnWall);
		compound.setBoolean("wallJump", this.wallJump);
		compound.setBoolean("animLeft", this.animLeft);
		compound.setBoolean("animRight", this.animRight);
		compound.setBoolean("isSneaking", this.isSneaking);
		compound.setInteger("sneakTime", this.sneakTime);
		compound.setBoolean("wasSprinting", this.wasSprinting);
		compound.setBoolean("isTurning", this.isTurning);
		compound.setBoolean("isTurningOnWall", this.isTurningOnWall);
		compound.setInteger("turningTime", this.turningTime);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.isJumping = compound.getInteger("isJumping");
		this.jumpTime = compound.getInteger("jumpTime");
		this.isJumpingOnWall = compound.getBoolean("isJumpingOnWall");
		this.prevRolling = compound.getBoolean("prevRolling");
		this.rollingTime = compound.getInteger("rollingTime");
		this.isRolling = compound.getBoolean("isRolling");
		this.isGrabbing = compound.getBoolean("isGrabbing");
		for(int i = 0; i < compound.getByteArray("grabbingDirections").length; i++){
			if(compound.getByteArray("grabbingDirections")[i] == 0){
				this.grabbingDirections[i] = false;
			}
			if(compound.getByteArray("grabbingDirections")[i] == 1){
				this.grabbingDirections[i] = true;
			}
		}
		this.beginingRunning = compound.getBoolean("beginingRunning");
		this.tickRunningLeft = compound.getFloat("tickRunningLeft");
		this.tickRunningRight = compound.getFloat("tickRunningRight");
		this.animRunnig = compound.getBoolean("animRunning");
		this.backLeft = compound.getBoolean("backLeft");
		this.backRight = compound.getBoolean("backRight");
		this.temps = compound.getInteger("temps");
		this.ralenti = compound.getBoolean("ralenti");
		this.isOnWall = compound.getBoolean("isOnWall");
		this.wallJump = compound.getBoolean("wallJump");
		this.animLeft = compound.getBoolean("animLeft");
		this.animRight = compound.getBoolean("animRight");
		this.isSneaking = compound.getBoolean("isSneaking");
		this.sneakTime = compound.getInteger("sneakTime");
		this.wasSprinting = compound.getBoolean("wasSprinting");
		this.isTurning = compound.getBoolean("isTurning");
		this.isTurningOnWall = compound.getBoolean("isTurningOnWall");
		this.turningTime = compound.getInteger("turningTime");
	}

	@Override
	public void init(Entity entity, World world) {
		
	}

}
