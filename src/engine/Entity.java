package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public abstract class Entity extends GameObject implements EntityInterface, Cloneable {

	public static final Integer ACCELERATION = 1;
	private SimpleDoubleProperty x, y, width, height, zIndex;
	private SimpleStringProperty name, imagePath;
	private double xSpeed, ySpeed, xAcceleration, yAcceleration;
	private List<Event> events;

	public Entity() {
		this("Default", "resources/images/mario.png");
	}
	
	public Entity(String name, String imagePath) {
		super("Entity");
		x = new SimpleDoubleProperty();
		y = new SimpleDoubleProperty();
		width = new SimpleDoubleProperty();
		height = new SimpleDoubleProperty();
		zIndex = new SimpleDoubleProperty();
		events = new ArrayList<Event>();
		events = new ArrayList<Event>();
		this.name = new SimpleStringProperty(name);
		this.imagePath = new SimpleStringProperty(imagePath);
		addParam(new Parameter("Time Step", Double.class, 0.5));
	}

	public Entity(Entity entity) {
		this();
		for (Event event : entity.events) {
			this.addEvent(event);
		}
	}

	/**
	 * TODO: make sure to check state and set new state before acting.
	 */
	@Override
	public void update() {
		List<Event> eventsToTrigger = events.stream().filter(s -> s.act()).collect(Collectors.toList());
		eventsToTrigger.forEach(event -> event.trigger());
	}

	@Override
	public void addEvent(Event event) {
		this.events.add(event);
	}

	public double getZ() {
		return this.zIndex.get();
	}

	@Override
	public double getX() {
		return this.x.get();
	}

	@Override
	public void setX(double x) {
		this.x.set(x);
	}
	
	@Override
	public String getName() {
		return this.name.get();
	}
	
	@Override
	public String getImagePath() {
		return this.imagePath.get();
	}
	
	public void setImagePath(String path) {
		this.imagePath.set(path);
	}
	

	public ReadOnlyDoubleProperty xReadOnlyProperty() {
		return ReadOnlyDoubleProperty.readOnlyDoubleProperty(x);
	}

	public ReadOnlyDoubleProperty yReadOnlyProperty() {
		return ReadOnlyDoubleProperty.readOnlyDoubleProperty(y);
	}

	public SimpleDoubleProperty xProperty() {
		return x;
	}

	public SimpleDoubleProperty yProperty() {
		return y;
	}

	public SimpleDoubleProperty heightProperty() {
		return height;
	}

	public SimpleDoubleProperty widthProperty() {
		return width;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public SimpleStringProperty imagePathProperty() {
		return imagePath;
	}

	@Override
	public double getY() {
		return this.y.get();
	}

	@Override
	public void setY(double y) {
		this.y.set(y);
	}

	@Override
	public double getWidth() {
		return this.width.get();
	}

	@Override
	public void setWidth(double width) {
		this.width.set(width);
	}

	@Override
	public double getHeight() {
		return this.height.get();
	}

	@Override
	public void setHeight(double height) {
		this.height.set(height);
		;
	}

	public double getXSpeed() {
		return xSpeed;
	}

	@Override
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public double getYSpeed() {
		return ySpeed;
	}

	@Override
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	public double getXAcceleration() {
		return xAcceleration;
	}

	@Override
	public void setXAcceleration(double xAcceleration) {
		this.xAcceleration = xAcceleration;
	}

	public double getYAcceleration() {
		return yAcceleration;
	}

	@Override
	public void setYAcceleration(double yAcceleration) {
		this.yAcceleration = yAcceleration;
	}

	@Override
	public List<Event> getEvents() {
		return events;
	}

}