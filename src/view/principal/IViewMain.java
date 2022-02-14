package view.principal;

import java.util.Map;

import controler.IControler;
import javafx.scene.Node;
import javafx.scene.Scene;
import model.principal.DataModel;
import model.ruby.GraphNodeData;

public interface IViewMain {

	public IControler getControler();

	public Scene getScene();

	public void registerVarNode(DataModel pDataModel);

	public void removeVarNode(String pKey);

	public void clearVarNode();

	

	
}
