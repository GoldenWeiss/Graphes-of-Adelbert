class SpriteUtils
	java_import javafx.beans.binding.Bindings
	
	def self.sceneBinding(bindedProperty, positionProperty, dimensionProperty, ratioProperty) 
		bindedProperty.set(positionProperty.value() * ratioProperty.value() + dimensionProperty.value()/2)
		
		positionProperty.add_change_listener {|n| bindedProperty.set(n * ratioProperty.value() + dimensionProperty.value()/2) }
		dimensionProperty.add_change_listener {|n|bindedProperty.set(positionProperty.value() * ratioProperty.value() + n/2) }
		ratioProperty.add_change_listener {|n|bindedProperty.set(positionProperty.value() * n  + dimensionProperty.value()/2)}
		
		bindedProperty.add_change_listener {|n| positionProperty.set((n-dimensionProperty.value()/2) / ratioProperty.value())}
		
	end
end