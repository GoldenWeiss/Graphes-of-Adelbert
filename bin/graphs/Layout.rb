# graph({a: [0,0,0], b: [50,50,50]}, [[:a, :b]])

class Layout
	attr_accessor :vertices
	attr_accessor :edges
	def initialize(vertices=nil, edges=nil)
		if vertices != nil
			@vertices = Hash[vertices.to_a.map {|k, v| [k, v.nil? ? vertice : v] }]
			@vertices.each { |k, v|  @vertices[k] = ((v.is_a? Array) ? vertice(*v) : v) }
		else
			@vertices = {}
		end
	
		@edges = []
		if edges != nil
			edges.each_with_index { |v, i| @edges[i] = ((v.is_a? Array) ? edge(@vertices[v[0]], @vertices[v[1]]) : v) }
		end
	end
	def [](ind)
		@vertices[ind]
	end
	def to_s
		"G #{self.vertices().size} #{self.edges().size}"
	end
	def ==(other)
		@vertices == other.vertices
		@edges == other.edges
	end
	def layout_to(duration, l)
		@vertices.each {|k,v| v.move_to(duration, l[k].x(), l[k].y())}
	end
	def shrink(duration=500.ms)
		xmap = @vertices.values.map{|n|n.x}
		xmax = xmap.max
		xmin = xmap.min
		
		ymap = @vertices.values.map{|n|n.y}
		ymax = ymap.max
		ymin = ymap.min
		
		@vertices.each { |k, v|  v.move_to( duration,  rand(xmin..xmax), rand(ymin..ymax)) }
			
	
	end
end

def graph(*args)
	RGL::DirectedAdjacencyGraph.new(*args)
end

def cycle(n)
	  
	pc = 2 * Math::PI
	scal = n * 20
	ve = {}
	ed = Array.new(n)
	n.times do |i| 
		val = pc * i / n
		ve[i] = vertice(scal * Math.cos(val), scal * Math.sin(val))
	end
	n.times do |i|
		ed[i] = edge(ve[i],ve[(i+1)%n])
	end
	layout(ve, ed)
end
