package controler;

import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.principal.DataModel;
import model.principal.Model;
import model.ruby.GraphNodeData;
import view.principal.ViewMain;
import view.principal.IViewMain;

public class Controler implements IControler {
	private IViewMain view;
	private Model m;
	
	public Controler(Stage pStage) {
		
		m = new Model(this);
		view = new ViewMain(this, pStage);
	}
	
	@Override
	public Scene getScene() {
		return view.getScene();
	}

	@Override
	public void eventSetCode(String pCode) {
		m.eventSetCode(pCode);
	}

	@Override
	public void registerVarNode(DataModel pDataModel) {
		view.registerVarNode(pDataModel);
	}

	@Override
	public void removeVarNode(String pKey) {
		m.removeVarNode(pKey);
		view.removeVarNode(pKey);
	}

	@Override
	public void clearVarNode() {
		m.clearVarNode();
		view.clearVarNode();
		
	}

	@Override
	public void eventSetSceneProperties(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty,
			DoubleProperty ratio) {
		m.eventSetSceneProperties(widthProperty, heightProperty, ratio);
		
	}

}
