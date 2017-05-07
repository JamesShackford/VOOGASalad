package authoring.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import authoring.Workspace;
import authoring.command.AddInfo;
import authoring.components.Direction;
import authoring.panel.chat.Chat;
import authoring.panel.display.EntityDisplay;
import authoring.panel.info.InfoPanel;
import engine.entities.Entity;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Accordion;
import javafx.scene.image.Image;
import polyglot.Case;
import utils.views.CollapsibleView;
import utils.views.View;

/**
 * A class that encapsulates a panel architecture. New subpanels can be added
 * using a single line of code, placed in the createSubviews() method. This
 * system seeks to facilitate the addition of new subviews and to reduce
 * duplicated boilerplate code.
 * 
 * @author Elliott Bolzan
 *
 */
public class Panel extends CollapsibleView {

	private Workspace workspace;
	private List<View> subviews;
	private EntityDisplay entityDisplay;
	private Chat chat;
	private LayerPanel layerPanel;
	private InfoPanel info;

	/**
	 * Returns a Panel.
	 * 
	 * @param workspace
	 *            the Workspace that owns the Panel.
	 * @param index
	 *            the index of the divider that the Panel collapses to, in the
	 *            SplitPane that owns it.
	 */
	public Panel(Workspace workspace, int index) {
		super(workspace, workspace.getPane(), workspace.getPolyglot().get("PanelTitle", Case.TITLE), index,
				Direction.LEFT, false);
		this.workspace = workspace;
		entityDisplay = new EntityDisplay(workspace);
		chat = new Chat(workspace);
		layerPanel = new LayerPanel(workspace);
		info = new InfoPanel(workspace);
		createSubviews();
		setup();
	}

	/**
	 * Populate the subviews List, which dictates which subviews appear in the
	 * Accordion.
	 */
	private void createSubviews() {
		subviews = new ArrayList<View>();
		subviews.add(entityDisplay);
		subviews.add(chat);
		subviews.add(layerPanel);
		subviews.add(info);
	}

	/**
	 * Create the Accordion and add it to the view.
	 */
	private void setup() {
		List<StringBinding> info = new ArrayList<StringBinding>(
				Arrays.asList(workspace.getPolyglot().get("EntityInfo"), workspace.getPolyglot().get("ChatInfo"),
						workspace.getPolyglot().get("LayerPanelInfo"), workspace.getPolyglot().get("GameInfo")));
		Accordion accordion = workspace.getMaker().makeAccordion(subviews, info);
		accordion.getStyleClass().add("gae-tile");
		setCenter(accordion);
	}

	/**
	 * @return the EntityDisplay.
	 */
	public EntityDisplay getEntityDisplay() {
		return entityDisplay;
	}

	/**
	 * @return the Chat.
	 */
	public Chat getChat() {
		return chat;
	}

	/**
	 * Initialize the dragging system. Allows for Entities to be dragged from
	 * the Panel's EntityDisplay to the Workspace's Canvas.
	 */
	public void setupDragToAddEntity() {
		entityDisplay.getList().setOnDragDetected(e -> {
			Entity addedEntity = entityDisplay.getList().getSelectionModel().getSelectedItem();
			addedEntity.setId(addedEntity.generateId());
			Image image = new Image(addedEntity.getImagePath());
			setCursor(new ImageCursor(image, 0, 0));
			entityDisplay.getList().setOnMouseReleased(e2 -> {
				Point2D canvasPoint = workspace.getLevelEditor().getCurrentLevel().getCanvas().getExpandablePane()
						.screenToLocal(new Point2D(e2.getScreenX(), e2.getScreenY()));
				Point2D workspacePoint = workspace.screenToLocal(new Point2D(e2.getScreenX(), e2.getScreenY()));
				Bounds canvasBounds = workspace.getLevelEditor().getCurrentLevel().getCanvas()
						.localToScene(workspace.getLevelEditor().getCurrentLevel().getCanvas().getBoundsInLocal());
				// only add entity to canvas if the mouse intersects the canvas.
				if (canvasBounds.intersects(workspacePoint.getX(), workspacePoint.getY(), 0, 0)) {
					AddInfo addInfo = new AddInfo(addedEntity.getName(), canvasPoint.getX(), canvasPoint.getY(),
							workspace.getLevelEditor().getCurrentLevel().getCurrentLayer());
					deselectAllEntities();
					workspace.getNetworking().sendIfConnected(addInfo);
				}
				setCursor(Cursor.DEFAULT);
			});
		});
	}

	private void deselectAllEntities() {
		workspace.getLevelEditor().getCurrentLevel().getLayers().forEach(layer -> {
			layer.getSelectedEntities().forEach(selectedEntity -> {
				selectedEntity.setSelected(false);
			});
		});
	}

	/**
	 * Select an existing level, taking into account the nature of the old level
	 * and the new one.
	 * 
	 * @param oldLevel
	 *            a String representing the oldLevel.
	 * @param newLevel
	 *            a String representing the newLevel.
	 */
	public void selectExistingLevel(String oldLevel, String newLevel) {
		layerPanel.selectLevel(oldLevel, newLevel);
	}

	/**
	 * Select a Level based on its number.
	 * 
	 * @param number
	 *            the number of the Level to be switched to.
	 */
	public void selectLoadedLevel(int number) {
		layerPanel.selectLevel(number);
	}

}
