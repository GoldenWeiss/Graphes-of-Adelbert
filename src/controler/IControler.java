package controler;

import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import model.principal.DataModel;
import model.ruby.GraphNodeData;

public interface IControler {

	Scene getScene();

	void eventSetCode(String pCode);

	void registerVarNode(DataModel pDataModel);

	void removeVarNode(String pKey);

	void clearVarNode();

	void eventSetSceneProperties(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty,
			DoubleProperty ratio);

	

}
