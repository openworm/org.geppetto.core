Model.neuroml.resolveAllImportTypes();

Project.getActiveExperiment().playAll();

G.addWidget(6);
Connectivity1.setName("Connectivity matrix");
Connectivity1.setData(acnet2,{linkType:function(c){return GEPPETTO.ModelFactory.getAllVariablesOfType(c.getParent(),GEPPETTO.ModelFactory.geppettoModel.neuroml.synapse)[0].getId();}});

var newPopup = G.addWidget(1);
newPopup.setMessage(Project.getActiveExperiment().getDescription());
newPopup.setName("Description");
Connectivity1.setPosition(780,182)
G.incrementCameraPan(-0.1, 0)
Connectivity1.setPosition(645,210)
newPopup.setPosition(641,78)
newPopup.setSize(110.80000019073486,665.8000001907349)
acnet2.baskets_12[0].select()

G.addWidget(0);
Plot1.setName("Primary Auditory Cortext Network - Some membrane potentials");
Plot1.setPosition(113, 90);
Plot1.setSize(230,445);
Plot1.plotData(acnet2.pyramidals_48[0].soma_0.v);
Plot1.plotData(acnet2.pyramidals_48[1].soma_0.v);
Plot1.plotData(acnet2.baskets_12[2].soma_0.v);


G.addBrightnessFunction(acnet2.pyramidals_48[0].soma_0, acnet2.pyramidals_48[0].soma_0.v, function(x){return (x+0.07)/0.1;});
G.addBrightnessFunction(acnet2.pyramidals_48[1].soma_0, acnet2.pyramidals_48[1].soma_0.v, function(x){return (x+0.07)/0.1;});
G.addBrightnessFunction(acnet2.baskets_12[2].soma_0, acnet2.baskets_12[2].soma_0.v, function(x){return (x+0.07)/0.1;});
