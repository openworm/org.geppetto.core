options = {yaxis:{min:-0.08,max:0.01},xaxis:{min:0,max:400,show:false}};
G.addWidget(GEPPETTO.Widgets.PLOT);
Plot1.setName("Voltage response for neuron and muscle (mV)");
Plot1.setSize(256,453);
Plot1.setPosition(184,83);
Plot1.plotData(net1.muscle[0].v,options);
Plot1.plotData(net1.neuron[0].v,options);

options = {yaxis:{min:-0.000005,max:0.000055},xaxis:{min:0,max:400,show:false}};
G.addWidget(GEPPETTO.Widgets.PLOT);
Plot2.setName("Ca concentration muscle (mM)");
Plot2.setSize(246,450);
Plot2.setPosition(187,348);
Plot2.plotData(net1.muscle[0].caConc,options);

G.addWidget(Widgets.POPUP);
Popup1.setName("Description");
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setSize(160.80000019073486,446.80000019073486);
Popup1.setPosition(643,83);

Project.getActiveExperiment().play({playAll:true});

net1.electrical.getModelTree();

G.addWidget(GEPPETTO.Widgets.TREEVISUALISERDAT);
TreeVisualiserDAT1.setName("Neuron and muscle cell network");
TreeVisualiserDAT1.setData(net1);
TreeVisualiserDAT1.setPosition(643,250);
TreeVisualiserDAT1.setSize(343,447);
