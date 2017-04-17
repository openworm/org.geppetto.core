Project.getActiveExperiment().playAll();

G.addWidget(Widgets.PLOT);
Plot1.setName("Hodgkin-Huxley Spiking Neuron");

Plot1.setPosition(120, 90);
Plot1.setSize(230,465);
Plot1.plotData(hhcell.hhpop[0].v);
Plot1.plotData(hhcell.hhpop[0].spiking);

G.addWidget(Widgets.PLOT);

Plot2.setName("Gating Variables");
Plot2.setPosition(120,350);
Plot2.setSize(285,465)
Plot2.plotData(hhcell.hhpop[0].bioPhys1.membraneProperties.naChans.na.h.q);
Plot2.plotData(hhcell.hhpop[0].bioPhys1.membraneProperties.naChans.na.m.q);
Plot2.plotData(hhcell.hhpop[0].bioPhys1.membraneProperties.kChans.k.n.q);

Plot2.setLegend(hhcell.hhpop[0].bioPhys1.membraneProperties.naChans.na.h.q,"Sodium h.q");
Plot2.setLegend(hhcell.hhpop[0].bioPhys1.membraneProperties.naChans.na.m.q,"Sodium m.q");
Plot2.setLegend(hhcell.hhpop[0].bioPhys1.membraneProperties.kChans.k.n.q,"Potassium n.q");

var newPopup = G.addWidget(Widgets.POPUP);
newPopup.setMessage(Project.getActiveExperiment().getDescription());
newPopup.setName("Description");
newPopup.setPosition(870, 90);
newPopup.setSize(160,465)

G.addBrightnessFunction(hhcell.hhpop[0], hhcell.hhpop[0].v, window.voltage_color);

Project.getActiveExperiment().play({step:1});




