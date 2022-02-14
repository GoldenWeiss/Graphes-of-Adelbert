class SpriteGraph
	def initialize(gra)
		@graphic = pane
		
		gra.layout.edges.each do |e| 
			@graphic.getChildren() << SpriteEdge.new(e).getGraphic
		end
		gra.layout.vertices.each do |k, v| 
			@graphic.getChildren() << SpriteVertice.new(v).getGraphic
		end
		
	end
	def getGraphic
		@graphic
	end
	
end