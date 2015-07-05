net.electrical.getModelTree();

options = {yaxis:{min:-0.08,max:0.01},xaxis:{min:0,max:400,show:false}};
G.addWidget(GEPPETTO.Widgets.PLOT);
Plot1.setName("Voltage response muscle (mV)");
Plot1.setSize(256,453);
Plot1.setPosition(184,83);
Plot1.plotData(net.muscle_0.electrical.SimulationTree.SingleCompMuscleCell.v,options);

options = {yaxis:{min:-0.000005,max:0.000055},xaxis:{min:0,max:400,show:false}};
G.addWidget(GEPPETTO.Widgets.PLOT);
Plot2.setName("Ca concentration muscle (mM)");
Plot2.setSize(256,453);
Plot2.setPosition(184,340);
Plot2.plotData(net.muscle_0.electrical.SimulationTree.SingleCompMuscleCell.caConc,options);

options = {yaxis:{min:-.1,max:0.1},xaxis:{min:0,max:400,show:false}};
G.addWidget(GEPPETTO.Widgets.PLOT);
Plot3.setName("Voltage response neuron (mV)");
Plot3.setSize(256,453);
Plot3.setPosition(639,84)
Plot3.plotData(net.neuron_0.electrical.SimulationTree.generic_iaf_cell.v,options);

G.addWidget(GEPPETTO.Widgets.TREEVISUALISERDAT);
TreeVisualiserDAT1.setName("Neuron and muscle cell network");
TreeVisualiserDAT1.setData(net);
TreeVisualiserDAT1.setPosition(397,77);
TreeVisualiserDAT1.setPosition(639,339)
TreeVisualiserDAT1.setSize(256,453);

G.addWidget(Widgets.POPUP);
Popup1.setName("Description");
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setSize(209.8,294.8)
Popup1.setPosition(1099,84)

G.incrementCameraZoom(-0.2)

