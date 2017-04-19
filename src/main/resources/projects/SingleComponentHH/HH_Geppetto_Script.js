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

var widthScreen=this.innerWidth,heightScreen=this.innerHeight,marginLeft=100,marginTop=70,marginRight=10,marginBottom=50,defaultWidgetWidth=450,defaultWidgetHeight=500,initialiseTreeWidget=function(e,t,o,n,i){n="undefined"!=typeof n?n:defaultWidgetWidth,i="undefined"!=typeof i?i:defaultWidgetHeight;var a=G.addWidget(3);return a.setSize(i,n),a.setName(e),a.setPosition(t,o),a},initialiseControlPanel=function(){var e=88,t=5,o={"OSB Control Panel":{"Cell Info":{"Model Description":{actions:["showModelDescription(getMainType())"],icon:"gpt-pyramidal-cell",label:"Model Description",tooltip:"Information on the model"},Cell:{actions:["executeOnSelection(showSelection)"],icon:"gpt-pyramidal-cell",label:"Cell Info",tooltip:"Information on the cell"},Channels:{actions:["executeOnSelection(showChannelTreeView)"],icon:"gpt-ion-channel",label:"Channels",tooltip:"Ion channels present on the cell"},Inputs:{actions:["executeOnSelection(showInputTreeView)"],icon:"gpt-inputs",label:"Inputs",tooltip:"Inputs present on the cell"},CellVisual:{actions:["executeOnSelection(showVisualTreeView)"],icon:"gpt-pyramidal-cell",label:"Cell Visual",tooltip:"Visual information on the cell"}}}};G.addWidget(7).renderBar("OSB Control Panel",o["OSB Control Panel"]),ButtonBar1.setPosition(e,t)},showModelDescription=function(e){var t=G.addWidget(1).setName("Model Description");t.setHTML(GEPPETTO.ModelFactory.getHTMLVariable(e,GEPPETTO.Resources.HTML_TYPE,"modelDescription"))},getMainType=function(){return"undefined"==typeof hhcell?GEPPETTO.ModelFactory.geppettoModel.neuroml.hhcell:hhcell.getType()},addSuggestionsToSpotlight=function(){var e={VFB:{url:"http://vfbdev.inf.ed.ac.uk/search/select?fl=short_form,label,synonym,id,type,has_narrow_synonym_annotation,has_broad_synonym_annotation&start=0&fq=ontology_name:(fbbt)&fq=is_obsolete:false&fq=shortform_autosuggest:VFB_*%20OR%20shortform_autosuggest:FBbt_*&rows=250&bq=is_defining_ontology:true%5E100.0%20label_s:%22%22%5E2%20synonym_s:%22%22%20in_subset_annotation:BRAINNAME%5E3%20short_form:FBbt_00003982%5E2&q=**&defType=edismax&qf=label%20synonym%20label_autosuggest_ws%20label_autosuggest_e%20label_autosuggest%20synonym_autosuggest_ws%20synonym_autosuggest_e%20synonym_autosuggest%20shortform_autosuggest%20has_narrow_synonym_annotation%20has_broad_synonym_annotation&wt=json&indent=true",crossDomain:!0,label:"label",type:{"class":{actions:["alert('VFB True Cross Domain Class');"],icon:"fa-dot-circle-o"},individual:{actions:["alert('VFB True Cross Domain Individual');"],icon:"fa-square-o"}}},VFB2:{url:"https://raw.githubusercontent.com/openworm/org.geppetto.samples/d9a5e22244eedf67f8518df228103cb78078a956/DataSources/demo.json",crossDomain:!1,label:"name",type:{"class":{actions:["alert('Demo True Cross Domain Class');"],icon:"fa-dot-circle-o"},individual:{actions:["alert('Demo True Cross Domain Individual');"],icon:"fa-square-o"}}}};GEPPETTO.Spotlight.addDataSource(e)},executeOnSelection=function(e){if(GEPPETTO.ModelFactory.geppettoModel.neuroml.cell){var t=G.getSelection()[0];"undefined"!=typeof t?e(t):G.addWidget(1).setMessage("No cell selected! Please select one of the cells and click here for information on its properties.").setName("Warning Message")}},showConnectivityMatrix=function(e){void 0==GEPPETTO.ModelFactory.geppettoModel.neuroml.projection?G.addWidget(1).setMessage("No connection found in this network").setName("Warning Message"):G.addWidget(6).setData(e,{linkType:function(e){if(void 0!=GEPPETTO.ModelFactory.geppettoModel.neuroml.synapse){var t=GEPPETTO.ModelFactory.getAllVariablesOfType(e.getParent(),GEPPETTO.ModelFactory.geppettoModel.neuroml.synapse)[0];if(void 0!=t)return t.getId()}return e.getName().split("-")[0]}}).setName("Connectivity Widget on network "+e.getId())},showChannelTreeView=function(e){if(GEPPETTO.ModelFactory.geppettoModel.neuroml.ionChannel){var node=eval(e.getPath()),n=G.addWidget(1).setName("Ion Channels on cell "+e.getName());n.setPosition(206,294),n.setHTML(GEPPETTO.ModelFactory.getHTMLVariable(node.getType(),GEPPETTO.Resources.HTML_TYPE,"ionChannel"))}n.setPosition(606,394)},showInputTreeView=function(e){if(GEPPETTO.ModelFactory.geppettoModel.neuroml.pulseGenerator){var node=eval(e.getPath()),n=G.addWidget(1).setName("Inputs on "+e.getId());n.setPosition(widthScreen-marginLeft-650,heightScreen-marginBottom-400),n.setHTML(GEPPETTO.ModelFactory.getHTMLVariable(node.getType(),GEPPETTO.Resources.HTML_TYPE,"pulseGenerator"))}n.setPosition(206,294)},showVisualTreeView=function(e){var t=350,o=400,n=initialiseTreeWidget("Visual information on cell "+e.getName(),widthScreen-marginLeft-t,heightScreen-marginBottom-o,t,o);n.setData(e.getType().getVisualType(),{expandNodes:!0})},showSelection=function(e){var node=eval(e.getPath()),n=G.addWidget(1).setName("Cell Information for "+e.getId());n.setPosition(widthScreen-marginLeft-650,heightScreen-marginBottom-650),n.setHTML(GEPPETTO.ModelFactory.getAllVariablesOfMetaType(node.getType(),GEPPETTO.Resources.HTML_TYPE))};initialiseControlPanel(),hhcell.hhpop[0].select();


