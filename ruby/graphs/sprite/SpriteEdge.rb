class SpriteEdge
	def initialize(edg)
		@graphic = line
		
		@edg = edg
		@edg.vert1_property.add_change_listener { |obs, oval, nval| 
			puts 'aaa'
			bindStart(prop) 
		}
		@edg.vert2_property.add_change_listener {|obs, oval, nval| 
			puts 'bbb'
			bindEnd(prop) 
		}
		
		bindStart(@edg.vert1)
		bindEnd(@edg.vert2)
	end
	def bindStart(vert)
		SpriteUtils.sceneBinding(@graphic.startXProperty, vert.x_property, sceneWidth, sceneRatio)
		SpriteUtils.sceneBinding(@graphic.startYProperty, vert.y_property, sceneHeight, sceneRatio)
	end
	def bindEnd(vert)
		SpriteUtils.sceneBinding(@graphic.endXProperty, vert.x_property, sceneWidth, sceneRatio)
		SpriteUtils.sceneBinding(@graphic.endYProperty, vert.y_property, sceneHeight, sceneRatio)
	end
	def getGraphic
		@graphic
	end
	
end