G.addWidget(GEPPETTO.Widgets.PLOT);
Plot1.setOptions({yaxis:{min:-.08,max:-.04},xaxis:{min:0,max:400,show:false}});

Plot1.setPosition(146,72);
Plot1.setSize(331.80000019073486,620.8000001907349);
Plot1.setName("Membrane potentials");
Plot1.plotData(c302.ADAL_0.electrical.SimulationTree.generic_iaf_cell.v);
Plot1.plotData(c302.ADAR_0.electrical.SimulationTree.generic_iaf_cell.v);
Plot1.plotData(c302.BDUR_0.electrical.SimulationTree.generic_iaf_cell.v);
Plot1.plotData(c302.I1R_0.electrical.SimulationTree.generic_iaf_cell.v);
Plot1.plotData(c302.I2L_0.electrical.SimulationTree.generic_iaf_cell.v);
Plot1.plotData(c302.PVDR_0.electrical.SimulationTree.generic_iaf_cell.v);

G.addBrightnessFunction(c302.ADAL_0.electrical, c302.ADAL_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.ADAR_0.electrical, c302.ADAR_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.BDUR_0.electrical, c302.BDUR_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.I1R_0.electrical, c302.I1R_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.I2L_0.electrical, c302.I2L_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.PVDR_0.electrical, c302.PVDR_0.electrical.SimulationTree.generic_iaf_cell.v, function(x){return (x+0.06)/0.01;});

Project.getActiveExperiment().play({step:1});