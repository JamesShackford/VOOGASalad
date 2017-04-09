package engine.entities;

import engine.Entity;
import engine.Parameter;

public class CharacterEntity extends Entity {

	public CharacterEntity(String name, String imagePath) {
		super(name, imagePath);
		addParam(new Parameter("Lives", Integer.class, 5));
	}
	
	public int getLives(){
		 return (Integer)getParam("Lives");
	}
	
	public void setLives(int lives){
		updateParam("Lives",lives);
	}
}
