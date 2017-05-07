package authoring.networking;

public class AuthoringUpdate extends Packet {
	private static final long serialVersionUID = 2842958873499181950L;

	private String entityName;

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}
}
