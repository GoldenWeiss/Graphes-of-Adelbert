#jrubyfx
require_relative 'jrubyfx.rb'
include JRubyFX::DSL
include JRubyFX




#my graph lib
require_relative 'graphs/Vertice'
require_relative 'graphs/Edge'
require_relative 'graphs/Layout'
require_relative 'graphs/sprite/SpriteUtils'

#should be moved elsewhere to allow extern modifications
require_relative 'graphs/sprite/SpriteVertice'
require_relative 'graphs/sprite/SpriteEdge'
require_relative 'graphs/sprite/SpriteGraph'

require_relative 'rgl/adjacency'
require_relative 'rgl/layout/layout'

puts 'Loaded ruby runtime'