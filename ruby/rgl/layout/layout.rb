module RGL
  
  module Graph
 
  	alias old_eql_graph? eql_graph?
  	def eql_graph?(other)
  		return false if layout_type != other.layout_type
  		return false if layout != other.layout
  		old_eql_graph?(other)
  	end
  	def layout_type=(v)
  		
  		@layout_type=v
  	end
  	def layout_type
  		return @layout_type
  	end
  	def layout
  		@layout || (@layout = build_layout)
  	end
  	def layout_to(duration, ptype)
  		
  		l = layout()
  		@layout_type = ptype
  		l.layout_to(duration, build_layout)
  	end
  	def build_layout
  		
  		case @layout_type
  		when :circle
  			l = layout_circle
  		when :random
  			l = layout_random
  		else
  			l = layout_circle
  		end
  		return l
  	end
  
  	def layout_circle
  		ve = {}
  		s = num_vertices
  		scal = 20 * s
  		i = 0
  		each_vertex do |v|
  			val = 2 * Math::PI * i / s
  			ve[v] = vertice(scal * Math.cos(val), scal * Math.sin(val))
  			i += 1
  		end
  		ed = []
  		i = 0
  		each_edge do |v, k|
  			ed[i] = edge(ve[v], ve[k])
  			i+=1
  		end
  		return Layout.new(ve, ed)
  	end
  
  	def layout_random
  		w = sceneWidth.value()/2
  		h = sceneHeight.value()/2
  		ve = {}
  		s = num_vertices
  		
  		each_vertex {|v| ve[v] = vertice(rand(-w..w), rand(-h..h))}
  		ed = []
  		i = 0
  		each_edge do |v, k|
  			ed[i] = edge(ve[v], ve[k])
  			i+=1
  		end
  	
  		return Layout.new(ve, ed)
  	end
  	
  end
end
