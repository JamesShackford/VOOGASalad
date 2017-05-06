package authoring.panel.chat;

import authoring.Workspace;
import authoring.networking.Packet;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import polyglot.Case;
import utils.views.View;

/**
 * 
 * A class representing a chat system. Allows for a username, the viewing of
 * sent messages, and the sending of messages. Plays a chat sound when messages
 * are received.
 * 
 * Interacts with the Log class, which is actually responsible for the
 * displaying of messages to the user.
 * 
 * @author Elliott Bolzan
 *
 */
public class Chat extends View {

	private Workspace workspace;
	private Log log;
	private TextField sendField;
	private String username;

	/**
	 * Creates a Chat.
	 * 
	 * @param workspace
	 *            the workspace that owns the Chat.
	 */
	public Chat(Workspace workspace) {
		super(workspace.getPolyglot().get("ChatTitle", Case.TITLE));
		this.workspace = workspace;
		setup();
	}

	private void setup() {
		getStyleClass().add("background");
		username = System.getProperty("user.name");
		log = new Log(workspace, username);
		log.setPrefHeight(Double.MAX_VALUE);
		setCenter(log);
		setBottom(createSendBox());
	}

	private Node createSendBox() {
		HBox sendBox = new HBox();
		sendField = new TextField();
		sendField.promptTextProperty().bind(workspace.getPolyglot().get("ChatEntryPlaceholder"));
		sendField.setOnKeyPressed(e -> sendKeyPressed(e));
		Button sendButton = new Button();
		sendButton.textProperty().bind(workspace.getPolyglot().get("SendButton"));
		sendButton.setOnAction(e -> send());
		sendBox.getChildren().addAll(sendField, sendButton);
		sendBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(sendField, Priority.ALWAYS);
		return sendBox;
	}

	private void sendKeyPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			event.consume();
			send();
		}
	}

	private void send() {
		if (!sendField.getText().replace("\\s+", "").equals("")) {
			try {
				workspace.getNetworking().send(new Message(username, sendField.getText()));
				sendField.setText("");
			} catch (Exception e) {
				Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
						workspace.getPolyglot().get("MessageFailed"));
				alert.show();
			}
		}
	}

	/**
	 * Called when a Packet is received by the application. A sound is played
	 * and the log is updated.
	 * 
	 * @param packet
	 *            the Packet that was received.
	 */
	public void received(Packet packet) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Message message = (Message) packet;
				if (!message.getUsername().equals(username)) {
					play();
				}
				log.append(message);
			}
		});
	}

	private void play() {
		String path = getClass().getResource(workspace.getIOResources().getString("ChatSong")).toExternalForm();
		MediaPlayer player = new MediaPlayer(new Media(path));
		player.play();
	}

}
