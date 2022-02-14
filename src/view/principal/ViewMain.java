package view.principal;

import java.io.IOException;
import java.util.Map;

import org.apache.pivot.wtk.Keyboard.KeyCode;

import com.jfoenix.controls.JFXButton;

import ai.cogmission.mosaic.refimpl.javafx.MosaicPane;
import controler.IControler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.principal.DataModel;
import model.ruby.GraphNodeData;
import view.codearea.ViewCodeArea;
import view.scene.ViewScene;
import view.varmap.ViewVarMap;

public class ViewMain implements IViewMain {

	private IControler controler;
	private StackPane root;
	private Scene scene;
	private ViewVarMap _wndptr_varMap;
	private ViewScene _wndptr_scene;

	/*
	 * FXML
	 */
	@FXML
	private BorderPane borderPane1;

	@FXML
	private StackPane stackPane1;
	@FXML
	private StackPane stackPane2;

	@FXML
	private Button jfxClose;
	public boolean dragAllowed;

	@FXML
	private void close() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	private void hide() {
		((Stage) scene.getWindow()).setIconified(true);
	}

	/**
	 * Constructor
	 *
	 * @param pControler
	 */
	public ViewMain(IControler pControler, Stage pStage) {
		controler = pControler;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ViewMain.fxml"));
			loader.setController(this);
			root = loader.load();

			scene = new Scene(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
		pStage.setOnShown(e -> _wndptr_varMap = new ViewVarMap(this));

		root.addEventHandler(KeyEvent.ANY, e -> {
			if (e.getEventType() == KeyEvent.KEY_PRESSED)
				if (e.getCode().getCode() == KeyCode.SPACE)
					dragAllowed = true;

			if (e.getEventType() == KeyEvent.KEY_RELEASED)
				if (e.getCode().getCode() == KeyCode.SPACE)
					dragAllowed = false;
		});
		root.focusedProperty().addListener((obs, o, n) -> dragAllowed = dragAllowed && n);
	}

	public Label getLabel(String color, String id) {
		Label label = new Label();
		label.textProperty().set(id);
		label.textAlignmentProperty().set(TextAlignment.CENTER);
		label.alignmentProperty().set(Pos.CENTER);
		label.setOpacity(1.0);
		label.setTextFill(Color.WHITE);
		label.setFont(Font.font("Arial", FontWeight.BOLD, 16d));
		label.setStyle(
				"-fx-background-color: " + color.toString() + ";-fx-alignment:center;-fx-text-alignment:center;");
		label.setManaged(false);

		return label;
	}

	/*
	 * getters
	 */
	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public IControler getControler() {
		return controler;
	}

	/*
	 * Intialize
	 */
	@FXML
	private void initialize() {

		ViewCodeArea v = new ViewCodeArea(this, controler);
		_wndptr_scene = new ViewScene(this, controler);

		MosaicPane<Node> mosaicPane = new MosaicPane<Node>();

		mosaicPane.add(_wndptr_scene.getNode(), "1", 0.5, 0, 0.5, 0.7);
		mosaicPane.add(v.getNode(), "0", 0, 0, 0.5, 0.7);
		mosaicPane.add(getLabel("rgb(65,65,65)", "javadoc"), "2", 0, 0.7, 1, 0.3);

		mosaicPane.getEngine().addSurface(mosaicPane.getSurface());

		stackPane2.getChildren().add(mosaicPane);
		jfxClose.toFront();

		// stackPane1.toFront();
	}

	/*
	 * Windows Stuff
	 */
	public void showVarMap() {
		_wndptr_varMap.requestFocus();
	}

	private boolean isClosed(ViewVarMap wndptr) {
		return wndptr == null || wndptr.closed();
	}

	/*
	 * Nodes manipulation
	 */
	@Override
	public void registerVarNode(DataModel pDataModel) {
		_wndptr_varMap.registerVarNode(pDataModel);
		_wndptr_scene.registerVarNode(pDataModel);

	}

	@Override
	public void removeVarNode(String pKey) {
		_wndptr_varMap.removeVarNode(pKey);
	}

	@Override
	public void clearVarNode() {
		_wndptr_varMap.clearVarNode();

	}

}
