package view.varmap;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.ruby.GraphNodeData;
import view.principal.IViewMain;

public class VarNode {
	
	/*
	 * Attributes
	 */
	private String name;
	private GraphNodeData rbObject;
	private Node sprite;
	private double dragX, dragY;
	private double layoutWidth, layoutHeight;
	private IViewMain viewMother;
	
	/*
	 * FXML variables
	 */
	@FXML
	Label lblTitle;
	@FXML
	Label lblType;
	@FXML
	Button btnEdit;
	@FXML
	Button btnClose;
	
	/*
	 * FXML
	 */

	@FXML
	public void close() {
		viewMother.getControler().removeVarNode(name);
	}
	@FXML
	public void edit() {

	}
	@FXML
	public void onMousePressed(MouseEvent event) {
		dragX = sprite.getScene().getWindow().getX() - event.getScreenX() + sprite.getLayoutX();
		dragY = sprite.getScene().getWindow().getY() - event.getScreenY() + sprite.getLayoutY();
	}

	@FXML
	public void onMouseDragged(MouseEvent event) {
		sprite.setLayoutX(event.getScreenX() - sprite.getScene().getWindow().getX() + dragX);
		sprite.setLayoutY(event.getScreenY() - sprite.getScene().getWindow().getY() + dragY);
	}
	
	/**
	 * Constructor
	 * @param pName
	 * @param pRbObject
	 * @param pViewMother
	 */
	public VarNode(String pName, GraphNodeData pRbObject, IViewMain pViewMother) {
		viewMother = pViewMother;
		setName(pName);
		setRbObject(pRbObject);
		dragX = 0;
		dragY = 0;
		generateSprite();
	}
	/*
	 * Getters and setters
	 */
	public double getLayoutWidth() {
		return layoutWidth;
	}

	public double getLayoutHeight() {
		return layoutHeight;
	}

	public Node getSprite() {
		return sprite;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getRbObject() {
		return rbObject;
	}

	public void setRbObject(GraphNodeData pRbObject) {
		this.rbObject = pRbObject;
	}

	/*
	 * Gen sprite
	 */
	public void generateSprite() {
		boolean regen = sprite != null;

		Node oldSprite = sprite;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/VarNode.fxml"));
			loader.setController(this);
			sprite = loader.load();
			layoutWidth = 130;
			layoutHeight = 44;
		} catch (IOException e) {
			e.printStackTrace();
		}

		lblTitle.setText(getName());
		lblType.setText(rbObject.getRbObject().toString());

		if (regen) {
			sprite.setLayoutX(oldSprite.getLayoutX());
			sprite.setLayoutY(oldSprite.getLayoutY());
		}
		sprite.applyCss();
	}
}
