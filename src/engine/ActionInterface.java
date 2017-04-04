package engine;

import java.util.Map;

public interface ActionInterface {
	//external
	Map<String, Object> getParams(); // will use reflection
	void setParams(Map<String, Object> params); // will use reflection
	String getDisplayName();
	String getDisplayDescription();
	
	//internal
	void act(); // carry out the action triggered by the event. Will need a reference to game
}
