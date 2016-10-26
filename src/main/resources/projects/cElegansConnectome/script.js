celegans.setColor('0x00fffc');
G.setCameraPosition(-64.313,-345.746,54.455)
G.setCameraRotation(1.285,-0.827,-0.233,379.635)

G.addWidget(Widgets.POPUP);
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setName("Description");
Popup1.setPosition(600,18);
Popup1.setSize(160,465);
window.connectionsLoaded=false;
window.cLoaded=function(){$("#buttonOne").remove();window.connectionsLoaded=true;}
window.showConnectivity=function(){if(window.connectionsLoaded){G.addWidget(6).setData(celegans).setName("c. elegans Connectome").configViaGUI();}else{Model.neuroml.resolveAllImportTypes(function(){G.addWidget(6).setData(celegans).configViaGUI(); window.cLoaded();});};}

$(".simulation-controls").children().attr('disabled', 'disabled')
$("#genericHelpBtn").removeAttr('disabled')

G.addWidget(Widgets.BUTTONBAR).renderBar("c. elegans",JSON.parse('{"Sample ButtonBar": {"buttonOne": {"actions": ["Model.neuroml.resolveAllImportTypes(function(){celegans.RIPL[0].select(); window.cLoaded();})"],"icon": "gpt-worm","label": "Load connectome","tooltip": "Load connectome"},"buttonTwo": {"actions": ["window.showConnectivity();"],"icon": "gpt-make-group","label": "Connectivity analysis","tooltip": "Connectivity analysis"}}}')).setPosition(128,18);
	      