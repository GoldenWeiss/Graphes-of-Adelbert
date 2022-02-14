package model.principal;

import org.jruby.RubyModule;
import org.jruby.embed.ScriptingContainer;

import model.ruby.GraphNodeData;

public class DataModel {
	private ScriptingContainer container;
	private String key;
	private GraphNodeData rbObject;
	private RubyModule[] savedConsts;
	private int nbSavedConsts;
	
	
	public DataModel() {
		
	}
	public ScriptingContainer getContainer() {
		return container;
	}
	public void setContainer(ScriptingContainer container) {
		this.container = container;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public GraphNodeData getRbObject() {
		return rbObject;
	}
	public void setRbObject(GraphNodeData rbObject) {
		this.rbObject = rbObject;
	}
	public RubyModule[] getSavedConsts() {
		return savedConsts;
	}
	public void setSavedConsts(RubyModule[] savedConsts) {
		this.savedConsts = savedConsts;
	}
	public int getNbSavedConsts() {
		return nbSavedConsts;
	}
	public void setNbSavedConsts(int nbSavedConsts) {
		this.nbSavedConsts = nbSavedConsts;
	}
}
