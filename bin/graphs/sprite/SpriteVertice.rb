class SpriteVertice
	
	java_import javafx.scene.paint.Color
	java_import javafx.beans.binding.Bindings
	def initialize(vert)
		@graphic = circle
		
		@graphic.setFill(Color::RED)
		rad = 10
		@graphic.radius = rad * sceneRatio.value()
		@graphic.radiusProperty.bind(sceneRatio.multiply(rad))
		@graphic.on_mouse_dragged {|e| @graphic.centerX = e.getX; @graphic.centerY = e.getY}
		SpriteUtils.sceneBinding(@graphic.centerXProperty, vert.x_property, sceneWidth, sceneRatio)
		SpriteUtils.sceneBinding(@graphic.centerYProperty, vert.y_property, sceneHeight, sceneRatio)
	end
	def getGraphic
		@graphic
	end
end