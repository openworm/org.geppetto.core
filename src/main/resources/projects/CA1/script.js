G.addWidget(3).setData(ca1.getType(),{expandNodes:true});
TreeVisualiserDAT1.setPosition(147,90);
TreeVisualiserDAT1.setSize(448,452);
TreeVisualiserDAT1.setName("Hippocampus CA1 pyramidal cell model");

Model.neuroml.morphology_CA1.kad_dendrite_group.show(true)
G.addWidget(1).setMessage(Project.getActiveExperiment().getDescription());
Popup1.setPosition(950,98);
Popup1.setSize(138,500);
Popup1.setName("Description");