G.setCameraPosition(-64.313,-345.746,54.455)
G.setCameraRotation(1.285,-0.827,-0.233,379.635)

G.addWidget(Widgets.POPUP);
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setName("Description");
Popup1.setPosition(600,18);
Popup1.setSize(160,465)

G.addWidget(Widgets.BUTTONBAR).renderBar("c. elegans",JSON.parse('{"Sample ButtonBar": {"buttonOne": {"actions": ["Model.neuroml.resolveAllImportTypes()"],"icon": "gpt-worm","label": "Load connectome","tooltip": "Load connectome"},"buttonTwo": {"actions": ["Model.neuroml.resolveAllImportTypes()","G.addWidget(6).setData(celegans).configViaGUI()"],"icon": "gpt-make-group","label": "Connectivity analysis","tooltip": "Connectivity analysis"}}}')).setPosition(128,18);
	      