G.addWidget(3).setData(purkinje.electrical.VisualizationTree, {expandNodes: true});
TreeVisualiserDAT1.setPosition(147,90);
TreeVisualiserDAT1.setSize(448,452);
TreeVisualiserDAT1.setName("Purkinje cell");
purkinje.electrical.VisualizationTree.ChannelDensities.Density.Leak.show(true);
G.addWidget(1).setMessage(Project.getActiveExperiment().getDescription());
Popup1.setPosition(1004,98);
Popup1.setSize(138,407);
Popup1.setName("Description");