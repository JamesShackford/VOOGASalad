package data;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import engine.entities.Entity;
import engine.entities.entities.AchievementEntity;
import engine.entities.entities.BackgroundEntity;
import engine.entities.entities.CameraEntity;
import engine.game.Level;
import exceptions.NotAGameFolderException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class provides the main functionality for loading games. It unzips the
 * game file to the user's temporary folder so they do not have to see the game
 * files. It builds a Game object from the XML file, setting all of its instance
 * variables so the Player/Editor will have everything needed to load up the
 * game.
 * 
 * @author Jay Doherty
 * @author Michael Li
 */
public class GameLoader {

	private ResourceManager resourceManager;

	/**
	 * Loads game given the folder path and returns the entities and songpath
	 * necessary
	 * 
	 * @param gameFolderPath
	 *            : path to load game from
	 * @return
	 * @throws NotAGameFolderException
	 *             : incorrect folder path exception
	 */
	public Game loadGame(String gameFolderPath, String saveName) throws Exception {
		resourceManager = new ResourceManager();
		String tempFolderPath = System.getProperty(resourceManager.getTempDir()) + File.separator
				+ resourceManager.getVoogaName();
		File voogaDirectory = new File(tempFolderPath);
		if (!voogaDirectory.exists()) {
			voogaDirectory.mkdirs();
		}
		(new Unpackager()).unzip(gameFolderPath, tempFolderPath);
		File dataFile = new File(tempFolderPath + File.separator + saveName);
		if (!dataFile.exists()) {
			throw new NotAGameFolderException();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.parse(tempFolderPath + File.separator + saveName);
		Game game = new Game();
		addGameSettings(game, doc, tempFolderPath);
		return game;
	}

	/**
	 * Adds necessary Information to the Game objet
	 * 
	 * @param game
	 *            : game where settings are added
	 * @param doc
	 *            : doc where information is derived from
	 * @param tempFolderPath
	 *            : path string to game folder in the temp folder
	 */
	private void addGameSettings(Game game, Document doc, String tempFolderPath) {
		addName(game, doc);
		addInfo(game, doc);
		addDefaults(game, doc, tempFolderPath);
		addLevels(game, doc, tempFolderPath);
		addSong(game, doc, tempFolderPath);
		addSaves(game, tempFolderPath);
		addAchievements(game);
		addCurrentTime(game, doc);
		addIsCountingDown(game, doc);
		addNumberOfLives(game, doc);
		addUnlockedLevels(game, doc);
	}

	/**
	 * Adds the number of current lives to game
	 * 
	 * @param game
	 *            : game where lives is set
	 * @param doc
	 *            : doc where number of livess param is derived from
	 */
	private void addNumberOfLives(Game game, Document doc) {
		NodeList timeNodes = doc.getElementsByTagName(resourceManager.getNumLives());
		game.setNumberOfLives(Integer.parseInt(timeNodes.item(0).getAttributes().item(0).getNodeValue()));
	}

	/**
	 * Adds the unlocked levels to game
	 * 
	 * @param game
	 *            : game where unlocked levels is set
	 * @param doc
	 *            : doc where unlocked levels param is derived from
	 */

	private void addUnlockedLevels(Game game, Document doc) {
		NodeList unlockedLevelsNode = doc.getElementsByTagName(resourceManager.getUnlockedLvls());
		NodeList levelsList = unlockedLevelsNode.item(0).getChildNodes();
		Set<Integer> gameLevelsUnlocked = new HashSet<Integer>();
		for (int i = 0; i < levelsList.getLength(); i++) {
			Element levelElement = (Element) levelsList.item(i);
			int instantiatedLevel = Integer.parseInt(levelElement.getAttributes().item(0).getNodeValue());
			gameLevelsUnlocked.add(instantiatedLevel);
		}
		game.setUnlockedLevels(gameLevelsUnlocked);
	}

	/**
	 * Adds the current time of the timer
	 * 
	 * @param game
	 *            : game where timer time is set
	 * @param doc
	 *            : doc where current time param is derived from
	 */
	private void addCurrentTime(Game game, Document doc) {
		NodeList timeNodes = doc.getElementsByTagName(resourceManager.getCurrTime());
		game.setCurrentTime(Double.parseDouble(timeNodes.item(0).getAttributes().item(0).getNodeValue()));
	}

	/**
	 * Adds whether timer is counting down
	 * 
	 * @param game
	 *            : game where countdown is set
	 * @param doc
	 *            : doc where countdown param is derived from
	 */
	private void addIsCountingDown(Game game, Document doc) {
		NodeList countdownNodes = doc.getElementsByTagName(resourceManager.getTimeDown());
		game.setClockGoingDown(Boolean.parseBoolean(countdownNodes.item(0).getAttributes().item(0).getNodeValue()));
	}

	/**
	 * Adds saved games to game object
	 * 
	 * @param game
	 *            : game where saves are added
	 * @param folderpath
	 *            : path of the game folder
	 */
	private void addSaves(Game game, String folderPath) {
		ObservableList<String> saves = FXCollections.observableArrayList();
		File folder = new File(folderPath);
		File[] allFiles = folder.listFiles();
		for (File file : allFiles) {
			if (isSave(game, file)) {
				saves.add(file.getName());
			}
		}
		game.setSaves(saves);
	}

	/**
	 * Returns if game is a saved game (within player)
	 * 
	 * @param game
	 *            : current game
	 * @param File
	 *            : file of the settings being read
	 */

	private boolean isSave(Game game, File file) {
		return (file.getName().contains(game.getName()) && file.getName().contains(resourceManager.getSave())
				&& file.getName().contains(resourceManager.getXML()));
	}

	/**
	 * Adds game description to game
	 * 
	 * @param game
	 *            : game where info is added
	 * @param doc
	 *            : doc info is derived from
	 */
	private void addInfo(Game game, Document doc) {
		NodeList infoNode = doc.getElementsByTagName(resourceManager.getGameInfo());
		game.setInfo(infoNode.item(0).getAttributes().item(0).getNodeValue());
	}

	/**
	 * Adds game name from document to game
	 * 
	 * @param game
	 *            : game where name is to be added
	 * @param doc
	 *            : Document that contains game name, extracted by XML
	 */
	private void addName(Game game, Document doc) {
		NodeList nameNodes = doc.getElementsByTagName(resourceManager.getNameTitle());
		game.setName(nameNodes.item(0).getAttributes().item(0).getNodeValue());
	}

	/**
	 * Adds game song from document to game
	 * 
	 * @param game
	 *            : game where song is to be added
	 * @param doc
	 *            : Document that contains song, extracted by XML
	 * @param gameFolderPath
	 *            : top-level directory of the game
	 */
	private void addSong(Game game, Document doc, String gameFolderPath) {
		try {
			NodeList songNodes = doc.getElementsByTagName(resourceManager.getResourceTitle());
			game.setSongPath(gameFolderPath + File.separator
					+ convertPathForSystem(songNodes.item(0).getAttributes().item(0).getNodeValue()));
		} catch (Exception e) {
			game.setSongPath("");
		}
	}

	/**
	 * Method to add default entities to game after they are extracted from
	 * Document
	 * 
	 * @param game
	 *            : game object containing entities needed to be added
	 * @param doc
	 *            : Document containing information from extracted folder
	 * @param gameFolderPath
	 *            : top-level directory of the game
	 */
	private void addDefaults(Game game, Document doc, String gameFolderPath) {
		NodeList defaultsNode = doc.getElementsByTagName(resourceManager.getDefaultsTitle());
		Element entitiesNode = (Element) defaultsNode.item(0).getChildNodes().item(0);
		game.setDefaults(getEntities(entitiesNode, gameFolderPath));

	}

	/**
	 * Method to set the achievements extracted from the defaults Must be called
	 * after addDefaults
	 * 
	 * @param game
	 */
	private void addAchievements(Game game) {
		game.setAchievements(
				game.getDefaults().stream().filter(s -> s instanceof AchievementEntity).collect(Collectors.toList()));
	}

	/**
	 * Method to add levels to the game, as they are extracted from XML
	 * 
	 * @param game
	 *            : game where levels are to be added
	 * @param doc
	 *            : Document that contains level information
	 * @param gameFolderPath
	 *            : top-level directory of the game
	 */
	private void addLevels(Game game, Document doc, String gameFolderPath) {
		NodeList levelsNode = doc.getElementsByTagName(resourceManager.getLevelsTitle());
		NodeList levelsList = levelsNode.item(0).getChildNodes();
		List<Level> gameLevels = new ArrayList<Level>();

		for (int i = 0; i < levelsList.getLength(); i++) {
			Element levelElement = (Element) levelsList.item(i);
			Level instantiatedLevel = convertElementToLevel(levelElement, gameFolderPath);
			gameLevels.add(instantiatedLevel);
		}
		game.setLevels(gameLevels);
	}

	/**
	 * Converts an element from the XML into a level by filling it with entities
	 * 
	 * @param levelElement
	 *            : extracted level element from XML
	 * @param gameFolderPath
	 *            : top-level directory of the game
	 * @return
	 */
	private Level convertElementToLevel(Element levelElement, String gameFolderPath) {
		Element entitiesNode = (Element) levelElement.getChildNodes().item(0);
		Level returnedLevel = new Level();
		for (Entity entity : getEntities(entitiesNode, gameFolderPath)) {
			returnedLevel.addEntity(entity);
		}

		Element cameraNode = (Element) levelElement.getChildNodes().item(1);
		Entity camera = getEntityFromElement(cameraNode, gameFolderPath);
		returnedLevel.setCamera((CameraEntity) camera);

		Element backgroundNode = (Element) levelElement.getChildNodes().item(2);
		Entity background = getEntityFromElement(backgroundNode, gameFolderPath);
		returnedLevel.setBackground((BackgroundEntity) background);

		return returnedLevel;
	}

	/**
	 * Method to extract entities from nodes and return so game can be populated
	 * 
	 * @param entitiesNode
	 *            : node of entities that will be extracted
	 * @param folderPath
	 *            : top-level directory of the game
	 * @return
	 */
	private List<Entity> getEntities(Element entitiesNode, String gameFolderPath) {
		NodeList entitiesList = entitiesNode.getChildNodes();
		List<Entity> entityList = new ArrayList<Entity>();
		for (int i = 0; i < entitiesList.getLength(); i++) {
			if (entitiesList.item(i).getNodeName().equals(resourceManager.getEntityState())) {
				Entity instantiatedEntity = getEntityFromElement((Element) entitiesList.item(i), gameFolderPath);
				entityList.add(instantiatedEntity);
			}
		}
		return entityList;
	}

	/**
	 * Converts an element node from XML into an entity
	 * 
	 * @param entityElement
	 *            : element to be converted into an entity
	 * @return
	 */
	private Entity getEntityFromElement(Element entityElement, String gameFolderPath) {
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new EntityConverter());

		Entity entity = (Entity) xStream.fromXML(getXMLStringFromElement(entityElement));
		entity.setImagePath(resourceManager.getFileStart() + gameFolderPath + File.separator
				+ convertPathForSystem(entity.getImagePath()));

		return entity;
	}

	/**
	 * Converts file separators to match the system
	 * 
	 * @param path
	 * @return
	 */
	private String convertPathForSystem(String path) {
		String newPath = path;
		if (File.separator.equals(resourceManager.getForwardSlash())) {
			if (path.contains(resourceManager.getBackwardSlash())) {
				newPath = path.replace(resourceManager.getBackwardSlash(), File.separator);
			}
		} else {
			if (path.contains(resourceManager.getForwardSlash())) {
				newPath = path.replace(resourceManager.getForwardSlash(), File.separator);
			}
		}
		return newPath;
	}

	/**
	 * Method to convert an element node from XML into a string
	 * 
	 * @param entityElement
	 *            element to be converted into a string
	 * @return
	 */
	private String getXMLStringFromElement(Element entityElement) {
		StreamResult result = null;
		NodeList entityChildren = entityElement.getChildNodes();
		Element entityXMLElement = null;
		for (int i = 0; i < entityChildren.getLength(); i++) {
			if (entityChildren.item(i).getNodeType() == Node.ELEMENT_NODE) {
				entityXMLElement = (Element) entityChildren.item(i);
			}
		}
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(entityXMLElement);
			result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
		} catch (TransformerException e) {
		}
		String strObject = result.getWriter().toString();
		return strObject;
	}
}