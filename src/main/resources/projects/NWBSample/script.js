Instances.getInstance("nwbSample.stimulus");
Instances.getInstance("nwbSample.response");
Instances.getInstance("nwbSample.metadata");
Instances.getInstance("nwbSample.time");
G.addWidget(0).plotXYData(nwbSample.response,nwbSample.time).plotXYData(nwbSample.stimulus,nwbSample.time).setSize(273.8,556.8).setPosition(136,69).setName("Stimulus and response");
G.addWidget(0).plotXYData(nwbSample.response,nwbSample.time).setSize(385.8,557.8).setPosition(134,367).setName("Response");
G.addWidget(1).setData(nwbSample.metadata).setName('Metadata').setPosition(713,228).setSize(525.8,580.8);
G.addWidget(1).setMessage(Project.getActiveExperiment().getDescription()).setSize(150,581.8).setPosition(713,68.5).setName("Description");

GEPPETTO.Init.flipCameraY();
GEPPETTO.Init.flipCameraZ();
GEPPETTO.SceneController.setWireframe(true);

GEPPETTO.ControlPanel.setColumnMeta([{  "columnName": "sweep",  "order": 1,  "locked": false,  "displayName": "Sweep No.",  "source": "$entity$.getName()" }, {  "columnName": "name",  "order": 2,  "locked": false,  "displayName": "Name",  "source": "$entity$.getVariables()[1].getType().getVariables()[0].getValue().getWrappedObj()['value']['text']" }, {  "columnName": "amplitude",  "order": 2,  "locked": false,  "displayName": "Amplitude",  "source": "$entity$.getVariables()[1].getType().getVariables()[1].getValue().getWrappedObj()['value']['text']" }]);
GEPPETTO.ControlPanel.setColumns(['sweep', 'name', 'amplitude']);
GEPPETTO.ControlPanel.setDataFilter(function(entities) { var compositeInstances = GEPPETTO.ModelFactory.getAllTypesOfMetaType(GEPPETTO.Resources.COMPOSITE_TYPE_NODE, entities); var sweepInstances = []; for (index in compositeInstances) { allVariables = compositeInstances[index].getVariables(); for(v in allVariables){ if(allVariables[v].getId().startsWith('Sweep_')){ s_r = allVariables[v].getType().getVariables(); sweepInstances.push(s_r[0].getType()); sweepInstances.push(s_r[1].getType()); } } } return sweepInstances; }); 

GEPPETTO.ControlPanel.setData(Instances);
$("#controlpanel").show();