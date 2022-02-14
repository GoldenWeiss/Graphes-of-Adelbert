package main;

import controler.Controler;
import controler.IControler;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class App extends Application {
	final KeyCombination FullScreenKeyCombo = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
	private static final double SPLASH_WIDTH = 1000;
	private static final double SPLASH_HEIGHT = 427;
	private Stage ppStage;

	/**
	 * Démarre l'application principale JavaFx
	 */
	@Override
	public void start(Stage pStage) throws Exception {
		this.ppStage = pStage;
		ImageView splash = new ImageView(new Image("splash/fanart.png"));
		splash.setFitHeight(SPLASH_HEIGHT);
		splash.setFitWidth(SPLASH_WIDTH);

		Pane splashLayout = new Pane(splash);
		splashLayout.setPrefSize(SPLASH_WIDTH, SPLASH_HEIGHT);
		splashLayout.setBackground(null);
		Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);

		pStage.initStyle(StageStyle.UNDECORATED);

		final Rectangle2D bounds = Screen.getPrimary().getBounds();

		pStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		pStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);

		pStage.setOnShown(e -> Platform.runLater(() -> load()));
		pStage.setAlwaysOnTop(true);
		pStage.setScene(splashScene);

		pStage.show();
		pStage.toFront();
	}

	private void load() {
		Stage pStage = new Stage(StageStyle.UNDECORATED);
		IControler controleur = new Controler(pStage);

		Duration d = Duration.millis(1000);
		Timeline t1 = new Timeline(new KeyFrame(d, new KeyValue(ppStage.opacityProperty(), 0, Interpolator.EASE_BOTH)));
		t1.setOnFinished(e -> ppStage.close());
		t1.play();
		

		pStage.setTitle("Adelbert");
		pStage.setScene(controleur.getScene());
		pStage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		
		pStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (FullScreenKeyCombo.match(event)) {
				pStage.setFullScreen(!pStage.isFullScreen());
			}
		});
	
		pStage.initModality(Modality.APPLICATION_MODAL);
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

		pStage.setX(bounds.getMinX());
		pStage.setY(bounds.getMinY());
		pStage.setWidth(bounds.getWidth());
		pStage.setHeight(bounds.getHeight());
		pStage.show();
	}

	/**
	 * Point d'entrée du programme
	 * 
	 * @param args les entrées de la console
	 */
	public static void main(String[] args) {

		launch(args);
	}
}
