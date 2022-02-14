package model.principal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.internal.BiVariableMap;
import org.jruby.runtime.builtin.IRubyObject;

import controler.Controler;
import controler.IControler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import model.layer.Layer;
import model.ruby.EasyRuby;
import model.ruby.GraphNodeData;

import model.sprite.SpriteFactory;

public class Model {

	private IControler controler;
	private ScriptingContainer container;
	private List<Layer> layers;
	private int currentLayerIndex;
	private static final String MAIN_FILENAME = "main.rb";
	
	public static final String[] rubyClassesNames = { "Vertice" , "Edge", "RGL::DirectedAdjacencyGraph" };
	public static final String[] spriteClassesNames = {"SpriteVertice", "SpriteEdge", "SpriteGraph"};
	private RubyModule[] savedConsts;
	private int nbSavedConsts;
	
	
	
	public Model(IControler pControler) {
		controler = pControler;

		container = new ScriptingContainer(LocalVariableBehavior.PERSISTENT);
		container.runScriptlet(PathType.CLASSPATH, MAIN_FILENAME);

		
		nbSavedConsts = rubyClassesNames.length;
		savedConsts = new RubyModule[nbSavedConsts];
		for (int i = 0; i < nbSavedConsts; i++)
			savedConsts[i] = (RubyModule) container.runScriptlet(rubyClassesNames[i]);

		layers = new ArrayList<>();
		currentLayerIndex = 0;
		addLayer("Scene 0");
	}

	/*
	 * Layers manipulation
	 */
	public void addLayer(String pName) {
		layers.add(new Layer(pName));
	}

	public Layer getCurrentLayer() {
		return layers.get(currentLayerIndex);
	}

	/*
	 * Nodes manipulations
	 */
	public void eventSetCode(String pCode) {
		try {
			Object a = container.runScriptlet(pCode);
		} catch (Exception e) {

		}

		Map m = new HashMap<>(container.getVarMap());
		Map old = getCurrentLayer().getOldVarMap();

		if (old == null || old.isEmpty()) {
			for (String key : (Set<String>) m.keySet()) {
				if (m.get(key) != null)
					registerVarNode(key, new GraphNodeData(key, m.get(key)));
			}
		} else {
			for (String key : (Set<String>) m.keySet()) {
				Object p = old.get(key);
				if (m.get(key) != null && (p == null || EasyRuby.edited(p, m.get(key)))) {
					registerVarNode(key, new GraphNodeData(key, m.get(key)));
				}
			}

			for (String key : (Set<String>) old.keySet()) {
				if (old.get(key) != null && (!m.containsKey(key) || m.get(key) == null)) {
					controler.removeVarNode(key);
				}
			}
		}

		getCurrentLayer().setOldVarMap(m);
	}

	private void registerVarNode(String pKey, GraphNodeData pGraphNodeData) {
		DataModel dataModel = new DataModel();
		dataModel.setKey(pKey);
		dataModel.setRbObject(pGraphNodeData);
		dataModel.setContainer(container);
		dataModel.setSavedConsts(savedConsts);
		dataModel.setNbSavedConsts(nbSavedConsts);
		controler.registerVarNode(dataModel);
	}

	public void removeVarNode(String pKey) {
		if (container.getVarMap().containsKey(pKey))
			container.getVarMap().remove(pKey);
		if (getCurrentLayer().getOldVarMap().containsKey(pKey))
			getCurrentLayer().getOldVarMap().remove(pKey);
	}

	public void clearVarNode() {
		container.getVarMap().clear();
		getCurrentLayer().getOldVarMap().clear();
	}

	public void eventSetSceneProperties(ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty,
			DoubleProperty ratio) {
		
			Object o = container.runScriptlet("def define_getter(name, val);Kernel.define_method(name.to_sym){return val};end");//");
			container.callMethod(o, "define_getter", "sceneWidth", widthProperty);
			container.callMethod(o, "define_getter", "sceneHeight", heightProperty);
			container.callMethod(o, "define_getter", "sceneRatio", ratio);
			
		
	}

}
