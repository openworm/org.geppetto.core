G.addWidget(GEPPETTO.Widgets.PLOT);

Plot1.setPosition(146,72);
Plot1.setSize(331.80000019073486,620.8000001907349);
Plot1.setName("Membrane potentials");
Plot1.plotData(c302.ADAL[0].v);
Plot1.plotData(c302.ADAR[0].v);
Plot1.plotData(c302.BDUR[0].v);
Plot1.plotData(c302.I1R[0].v);
Plot1.plotData(c302.I2L[0].v);
Plot1.plotData(c302.PVDR[0].v);

G.addBrightnessFunction(c302.ADAL[0], c302.ADAL[0].v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.ADAR[0], c302.ADAR[0].v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.BDUR[0], c302.BDUR[0].v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.I1R[0], c302.I1R[0].v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.I2L[0], c302.I2L[0].v, function(x){return (x+0.06)/0.01;});
G.addBrightnessFunction(c302.PVDR[0], c302.PVDR[0].v, function(x){return (x+0.06)/0.01;});

Project.getActiveExperiment().play({step:1});