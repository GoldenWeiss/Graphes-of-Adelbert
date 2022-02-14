package view.varmap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.principal.DataModel;
import model.ruby.GraphNodeData;
import view.childInterface.RegisterableChild;
import view.principal.IViewMain;
import view.principal.ViewMain;

public class ViewVarMap extends RegisterableChild {

	private Stage wndptr;
	private Map<String, VarNode> varNodes;
	private Pane root;
	private IViewMain viewMother;
	public static final double WIDTH = 1200;
	public static final double HEIGHT = 180;

	public ViewVarMap(IViewMain pViewMother) {

		viewMother = pViewMother;
		varNodes = new HashMap<>();

		wndptr = new Stage();

		root = new Pane();
		root.setPrefWidth(WIDTH);
		root.setPrefHeight(HEIGHT);
		root.setStyle("-fx-background-color: rgb(65,65,65)");

		root.setOnContextMenuRequested(event -> {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem clear = new MenuItem("Clear var map");
			contextMenu.getItems().addAll(clear);

			clear.setOnAction(e -> viewMother.getControler().clearVarNode());

			contextMenu.show(root, event.getScreenX(), event.getScreenY());
		});
		
		wndptr.setScene(new Scene(root));
		wndptr.initOwner(pViewMother.getScene().getWindow());
		wndptr.setTitle("Var Map");
		wndptr.show();

	}

	@Override
	public boolean closed() {
		return !wndptr.isShowing();
	}

	@Override
	public void setOnCloseRequest(EventHandler<WindowEvent> value) {
		wndptr.setOnCloseRequest(value);
	}

	@Override
	public void requestFocus() {
		wndptr.show();
		wndptr.requestFocus();
	}

	public void registerVarNode(DataModel pDataModel) {
		String pKey = pDataModel.getKey();
		GraphNodeData pRbObject = pDataModel.getRbObject();
		
		VarNode varNode = null;
		ObservableList<Node> childrens = root.getChildren();

		if (!varNodes.containsKey(pKey)) {
			varNode = new VarNode(pKey, pRbObject, viewMother);
			varNodes.put(pKey, varNode);
			Node sprite = varNode.getSprite();

			sprite.setLayoutX(wndptr.getWidth() / 2 - varNode.getLayoutWidth() / 2);
			sprite.setLayoutY(wndptr.getHeight() / 2 - varNode.getLayoutHeight() / 2);

			childrens.add(sprite);
		} else {
			varNode = varNodes.get(pKey);
			int index = childrens.indexOf(varNode.getSprite());

			varNode.setName(pKey);
			varNode.setRbObject(pRbObject);
			varNode.generateSprite();
			childrens.set(index, varNode.getSprite());
		}

	}

	public void removeVarNode(String pKey) {

		root.getChildren().remove(varNodes.get(pKey).getSprite());
		varNodes.remove(pKey);
	}

	public void clearVarNode() {

		for (VarNode node : varNodes.values())
			root.getChildren().remove(node.getSprite());
		varNodes.clear();
	}

	

}
