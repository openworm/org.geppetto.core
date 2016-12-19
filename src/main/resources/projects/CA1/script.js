G.addWidget(3).setData(ca1.getType(),{expandNodes:true});
TreeVisualiserDAT1.setPosition(147,90);
TreeVisualiserDAT1.setSize(448,452);
TreeVisualiserDAT1.setName("Hippocampus CA1 pyramidal cell model");

Model.neuroml.morphology_CA1.kad_dendrite_group.show(true)
var newPopup = G.addWidget(1);
newPopup.setMessage(Project.getActiveExperiment().getDescription());
newPopup.setPosition(950,98);
newPopup.setSize(138,500);
newPopup.setName("Description");