G.addWidget(GEPPETTO.Widgets.PLOT);
Plot1.setName("Voltage response for neuron and muscle");
Plot1.setSize(256,453);
Plot1.setPosition(184,83);
Plot1.plotData(net1.muscle[0].v);
Plot1.plotData(net1.neuron[0].v);

G.addWidget(Widgets.POPUP);
Popup1.setName("Description");
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setSize(160.80000019073486,446.80000019073486);
Popup1.setPosition(643,83);

Project.getActiveExperiment().playAll();

G.addWidget(GEPPETTO.Widgets.TREEVISUALISERDAT);
TreeVisualiserDAT1.setName("Neuron and muscle cell network");
TreeVisualiserDAT1.setData(net1.getType(),{expandNodes:true});
TreeVisualiserDAT1.setPosition(643,250);
TreeVisualiserDAT1.setSize(343,447);
