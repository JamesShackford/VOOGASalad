package authoring.panel.chat;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import authoring.Workspace;
import engine.entities.Entity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebView;
import utils.views.View;

/**
 * 
 * The class that displays received messages. The messages are displayed using
 * HTML, for modularity's sake. On the reception of each new message, HTML is
 * created. This HTML is styled by a CSS file.
 * 
 * Certain patterns in the HTML (like <mark>) are recognized by the class, which
 * receives notifications when the user clicks on them. This allows for the
 * creation of hyperlink references to Entities.
 * 
 * Click detection in JavaFX WebView adapted from:
 * http://blogs.kiyut.com/tonny/2013/07/30/javafx-webview-addhyperlinklistener/#.WP97vFKZNmA.
 * 
 * @author Elliott Bolzan
 *
 */
public class Log extends View {

	private static final String LINK_PATTERN = "#{1}\\w+";
	private static final String EVENT_TYPE_CLICK = "click";
	private static final String SELF_CLASS = "self";
	private static final String OTHER_CLASS = "other";
	private static final String MESSAGE_CLASS = "message";

	private Workspace workspace;
	private String username;
	private WebView browser;
	private String chatLog;
	private ChangeListener<State> listener;

	/**
	 * Creates a Log.
	 * 
	 * @param workspace
	 *            the Workspace that owns the Log.
	 * @param username
	 *            the machine's current username.
	 */
	public Log(Workspace workspace, String username) {
		this.workspace = workspace;
		this.username = username;
		chatLog = "";
		browser = new WebView();
		setCSS();
		refresh();
		setCenter(browser);
	}

	private void setCSS() {
		File file = new File(workspace.getIOResources().getString("ChatCSSPath"));
		browser.getEngine().setUserStyleSheetLocation(file.toURI().toString());
	}

	/**
	 * Append a Message to the log. The message is appended as a new
	 * <li>object in the HTML. The username and actual message are appended as
	 * different HTML elements, to allow for them to be styled differently.
	 * 
	 * @param message
	 *            the Message to be appended.
	 */
	public void append(Message message) {
		String outer = message.getUsername().equals(username) ? SELF_CLASS : OTHER_CLASS;
		String original = "<li class=\"" + outer + "\"><div class=\"" + MESSAGE_CLASS + "\">" + message.getMessage()
				+ "</div><username>" + message.getUsername() + "</username></li>";
		chatLog += mark(original);
		refresh();
	}

	private String mark(String input) {
		try {
			return input.replaceAll(LINK_PATTERN, "<mark>$0</mark>");
		} catch (Exception e) {
			return input;
		}
	}

	private void refresh() {
		browser.getEngine().loadContent(createHTML());
		if (listener != null) {
			browser.getEngine().getLoadWorker().stateProperty().removeListener(listener);
		}
		createListener();
		browser.getEngine().getLoadWorker().stateProperty().addListener(listener);
	}

	private void createListener() {
		listener = new ChangeListener<State>() {
			@Override
			public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {
				if (newState == Worker.State.SUCCEEDED) {
					EventListener listener = new EventListener() {
						@Override
						public void handleEvent(Event ev) {
							String domEventType = ev.getType();
							if (domEventType.equals(EVENT_TYPE_CLICK)) {
								String entity = ((Element) ev.getTarget()).getTextContent();
								editEntity(entity);
							}
						}
					};
					Document doc = browser.getEngine().getDocument();
					NodeList nodeList = doc.getElementsByTagName("mark");
					for (int i = 0; i < nodeList.getLength(); i++) {
						EventTarget target = (EventTarget) nodeList.item(i);
						target.addEventListener(EVENT_TYPE_CLICK, listener, false);
					}
				}
			}
		};
	}

	private String createHTML() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head>");
		html.append("   <script language=\"javascript\" type=\"text/javascript\">");
		html.append("       function toBottom(){");
		html.append("           window.scrollTo(0, document.body.scrollHeight);");
		html.append("       }");
		html.append("   </script>");
		html.append("</head>");
		html.append("<body onload='toBottom()'>");
		html.append("<center><div class=\"intro\">" + workspace.getPolyglot().get("ChatInstructions").get()
				+ "</div></center>");
		html.append("<ol>");
		html.append(chatLog);
		html.append("</ol>");
		html.append("</body>");
		html.append("</html>");
		return html.toString();
	}

	private void editEntity(String name) {
		Entity entity = workspace.getDefaults().getEntity(name.replace("#", ""));
		if (entity == null) {
			Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
					workspace.getPolyglot().get("EntityNotExist"));
			alert.show();
			return;
		}
		workspace.getPanel().getEntityDisplay().editHelper(entity);
	}

}
