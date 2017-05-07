package authoring.panel.creation.pickers;

import authoring.Workspace;
import authoring.components.CustomTooltip;
import authoring.components.EditableContainer;
import authoring.panel.creation.EntityMaker;
import engine.GameObject;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * 
 * A superclass for pickers in the EntityMaker. The pickers have common
 * functionality: they all need to edit, delete, and select elements
 * (functionality inherited from EditableContainer), and they all need
 * functionality specific to their nature as a picker (updating, selecting a
 * GameObject, etc). For this reason, it made sense to create a hierarchy and
 * reduce duplicated code.
 * 
 * @author Elliott Bolzan
 *
 */
public abstract class Picker extends EditableContainer {

	private EntityMaker maker;
	private String titleProperty;
	private Label infoLabel;

	/**
	 * Creates a Picker.
	 * 
	 * @param workspace
	 *            the workspace that owns the Picker.
	 * @param titleProperty
	 *            the property corresponding to the Picker's title.
	 * @param maker
	 *            the EntityMaker which created the Picker.
	 */
	public Picker(Workspace workspace, String titleProperty, EntityMaker maker) {
		super(workspace, "");
		this.titleProperty = titleProperty;
		this.maker = maker;
		setup();
	}

	/**
	 * @return the EntityMaker that created this Picker.
	 */
	public EntityMaker getEntityMaker() {
		return maker;
	}

	private void setup() {
		setPadding(new Insets(15));
		createTypeBox();
		createContainer();
	}

	private void createTypeBox() {
		Label label = new Label();
		label.textProperty().bind(getWorkspace().getPolyglot().get(titleProperty));
		HBox spacing = new HBox();
		spacing.maxWidth(Double.MAX_VALUE);
		HBox.setHgrow(spacing, Priority.ALWAYS);
		infoLabel = new Label("?");
		ToolBar toolBar = new ToolBar(label, spacing, infoLabel);
		toolBar.getStyleClass().add("picker-header");
		setTop(toolBar);
	}

	/**
	 * Attach an tooltip to the information label.
	 * 
	 * @param string
	 *            the StringBinding to be attached to the information label.
	 */
	public void attachInfoTooltip(StringBinding string) {
		new CustomTooltip(string, infoLabel);
	}

	/**
	 * A method called when the user selects a GameObject from the Picker's
	 * view. Each implementation can respond differently to this event.
	 * 
	 * @param object
	 *            the selected GameObject.
	 */
	public abstract void select(GameObject object);

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.components.EditableContainer#createContainer()
	 */
	public abstract void createContainer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.components.EditableContainer#createNew()
	 */
	public abstract void createNew();

	/**
	 * Add an element to the Picker's container.
	 * 
	 * @param element
	 *            the element to be added to the container.
	 */
	public abstract <E> void add(E element);

	/**
	 * Remove an element from the Picker's container.
	 * 
	 * @param element
	 *            the element to be removed.
	 */
	public abstract <E> void remove(E element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.components.EditableContainer#delete()
	 */
	public abstract void delete();

	/**
	 * Update the Picker's display. Because the Event and Action lists provided
	 * by the back-end are not observable, the update process is manual. As
	 * such, it makes sense to have a required method to implement this update
	 * process.
	 */
	public abstract void update();

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.components.EditableContainer#edit()
	 */
	public abstract void edit();

	/**
	 * Called when the Editor must be shown to the user. Two situations
	 * typically require this: the creation of a new GameObject and the editing
	 * of an old one.
	 */
	public abstract void showEditor();

}
