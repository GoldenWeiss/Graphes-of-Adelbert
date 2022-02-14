package model.ruby;

public class GraphNodeData {
	private String key;
	private Object rbObject;
	
	
	public GraphNodeData(String pKey, Object pRbObject) {
		key = pKey;
		rbObject = pRbObject;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getRbObject() {
		return rbObject;
	}
}
