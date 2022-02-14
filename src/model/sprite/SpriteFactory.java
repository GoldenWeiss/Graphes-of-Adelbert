package model.sprite;

import java.util.HashMap;
import java.util.Map;

import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import model.principal.Model;

public class SpriteFactory {
	private ScriptingContainer container;
	
	private Map<String, RubyClass> savedConsts;
	
	
	public SpriteFactory() {
		
	}
	
	public Node generate(String pRubyClasseName, String pSpriteClassName, IRubyObject pRubyObject) {
		RubyClass klass = savedConsts.get(pSpriteClassName);
		Object instance = container.callMethod(klass, "new", pRubyObject);
		Node n = container.callMethod(instance, "getGraphic", Node.class);
		
		return n;
	}

	public void setContainer(ScriptingContainer pContainer) {
		this.container = pContainer;
		savedConsts = new HashMap<>();
		for (String name : Model.spriteClassesNames)
			savedConsts.put(name, (RubyClass) container.runScriptlet(name));
	}
}
