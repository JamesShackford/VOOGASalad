package authoring.networking;

public class MoveEntityUpdate extends EntityUpdate {

	private static final long serialVersionUID = 2171824380064010358L;
	double newX;
	double newY;

	public MoveEntityUpdate(double newX, double newY, long entityId) {
		super(entityId);
	}

	public double getX() {
		return newX;
	}

	public double getY() {
		return newY;
	}
}
