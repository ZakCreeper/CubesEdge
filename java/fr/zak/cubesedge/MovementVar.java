package fr.zak.cubesedge;

public abstract class MovementVar {
	
	private boolean disabled;
	
	public boolean isMovementDisabled(){
		return disabled;
	}
	
	public void enable(){
		if(disabled){
			disabled = false;
		}
	}
	
	public void disable(){
		if(!disabled){
			disabled = true;
		}
	}
}