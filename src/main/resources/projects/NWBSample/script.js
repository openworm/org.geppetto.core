Instances.getInstance("nwbSample.stimulus")
Instances.getInstance("nwbSample.response")
Instances.getInstance("nwbSample.metadata")
Instances.getInstance("nwbSample.time")

G.addWidget(0).plotXYData(nwbSample.response,nwbSample.time).plotXYData(nwbSample.stimulus,nwbSample.time).setSize(273.8,556.8).setPosition(136,69).setName("Stimulus and response")
G.addWidget(0).plotXYData(nwbSample.response,nwbSample.time).setSize(385.8,557.8).setPosition(134,367).setName("Response")

G.addWidget(1).setData(nwbSample.metadata).setName('Metadata').setPosition(713,228).setSize(525.8,580.8);
G.addWidget(1).setMessage(Project.getActiveExperiment().getDescription()).setSize(150,581.8).setPosition(713,68.5).setName("Description");