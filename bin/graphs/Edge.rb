class Edge
	property_accessor :vert1
	property_accessor :vert2
	
	
	def initialize(v1=nil, v2=nil)
		@vert1 = simple_object_property((v1 == nil ? vertice : v1))
		@vert2 = simple_object_property((v2 == nil ? vertice(50, 50, 0) : v2))
	end
end
def edge(*args)
	Edge.new(*args)
end