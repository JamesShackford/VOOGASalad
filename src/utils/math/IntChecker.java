package utils.math;


/**
 * Check whether a string is an int.
 * 
 * @author nikita
 */
public class IntChecker {
	
	public boolean check(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
