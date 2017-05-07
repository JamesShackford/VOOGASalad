package authoring.command;

import authoring.canvas.EntityView;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import utils.animation.ResizeHeightTranslation;
import utils.animation.ResizeWidthTranslation;

/**
 * 
 * @author jimmy (animations by Elliott Bolzan)
 *
 */
public class ResizeCommand extends EntityCommand {

	double oldHeight;
	double oldWidth;
	double newHeight;
	double newWidth;
	double oldX;
	double oldY;
	double newX;
	double newY;

	public ResizeCommand(EntityView entityView, ResizeInfo resizeInfo) {
		super(entityView);
		this.oldHeight = resizeInfo.getOldHeight();
		this.oldWidth = resizeInfo.getOldWidth();
		this.newHeight = resizeInfo.getNewHeight();
		this.newWidth = resizeInfo.getNewWidth();
		this.oldX = resizeInfo.getOldX();
		this.oldY = resizeInfo.getOldY();
		this.newX = resizeInfo.getNewX();
		this.newY = resizeInfo.getNewY();
	}

	@Override
	public void execute() {
		super.getEntityView().setTranslateX(newX);
		super.getEntityView().setTranslateY(newY);
		animateHeight(super.getEntityView(), newHeight);
		animateWidth(super.getEntityView(), newWidth);
	}

	@Override
	public void unexecute() {
		super.getEntityView().setTranslateX(oldX);
		super.getEntityView().setTranslateY(oldY);
		animateHeight(super.getEntityView(), oldHeight);
		animateWidth(super.getEntityView(), oldWidth);
	}

	private void animateWidth(Region region, double width) {
		ResizeWidthTranslation resize = new ResizeWidthTranslation(Duration.millis(300), region, width);
		resize.play();
	}

	private void animateHeight(Region region, double height) {
		ResizeHeightTranslation resize = new ResizeHeightTranslation(Duration.millis(300), region, height);
		resize.play();
	}

}
