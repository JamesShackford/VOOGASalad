package authoring.networking;

public class EntityUpdate extends AuthoringUpdate {
	private static final long serialVersionUID = 5062582635003911536L;
	private long entityId;

	public EntityUpdate(long entityId) {
		this.entityId = entityId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
}
