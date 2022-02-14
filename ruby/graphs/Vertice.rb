class Vertice
	#include JRubyFX
	#include JRubyFX::DSL
	
	property_accessor :x
	property_accessor :y
	property_accessor :z
	attr_reader :movement
	
	def initialize(px=0, py=0, pz=0)
    	@x = double_property(self, "x", px)
    	@y = double_property(self, "y", py)
    	@z = double_property(self, "z", pz)
    	@movement = nil
  	end
  
	def move_to(duration, px=x, py=y, pz=z)
		@movement = timeline(
		key_frame(duration, key_value(x_property, px, Java::javafx::animation::Interpolator::EASE_BOTH)),
		key_frame(duration, key_value(y_property, py, Java::javafx::animation::Interpolator::EASE_BOTH)),
		key_frame(duration, key_value(z_property, pz, Java::javafx::animation::Interpolator::EASE_BOTH))
		)
		@movement.play
	end

	def to_s
		"V #{self.x()} #{self.y()} #{self.z()}"
	end
end

def vertice(*args)
	Vertice.new(*args)
end
