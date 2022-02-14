package model.layer;

import java.util.Map;

public class Layer {
	String name;
	Map oldVarMap;

	public Layer(String pName) {
		name = pName;
		oldVarMap = null;
	}

	public void setOldVarMap(Map pOldVarMap) {
		oldVarMap = pOldVarMap;
	}
	public Map getOldVarMap() {
		return oldVarMap;
	}
}
