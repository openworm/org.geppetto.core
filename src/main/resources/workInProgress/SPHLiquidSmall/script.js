G.addWidget(Widgets.PLOT);
 
var options = {yaxis:{min:0,max:15},xaxis:{min:0,max:300,show:false}};

Plot1.setOptions(options)
Plot1.setPosition(194,140);
Plot1.setName("Particle 1 coordinates")

Plot1.plotData(sample.fluid.SimulationTree.particle[1].position.x);
Plot1.plotData(sample.fluid.SimulationTree.particle[1].position.y);
Plot1.plotData(sample.fluid.SimulationTree.particle[1].position.z);
