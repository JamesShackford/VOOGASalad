package authoring.canvas;

/**
 * A utility class for creating a grid tile system.
 * 
 * @author jimmy
 *
 */
public class GridUtil
{
	/**
	 * Gets the tiled coordinate for the given x and y position. For example,
	 * for a grid tile_size of 25, position (19, 3) will map to (25, 0).
	 * 
	 * @param coordiante
	 *            coordinate
	 * @param tileSize
	 *            size of one grid tile
	 * @return tiled coordinate of the given input.
	 */
	public static double getTiledCoordinate(double coordinate, int tileSize)
	{
		double gridCoordinate = ((int) coordinate / tileSize) * tileSize;
		if (coordinate % tileSize > tileSize / 2) {
			return gridCoordinate + tileSize;
		}
		return gridCoordinate;
	}
}
