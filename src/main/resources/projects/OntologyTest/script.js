GEPPETTO.Init.flipCameraY();
GEPPETTO.ControlPanel.setColumnMeta([ { "columnName": "path", "order": 1, "locked": false, "visible": true, "displayName": "Path", "source": "$entity$.getPath()" }, { "columnName": "name", "order": 2, "locked": false, "visible": true, "displayName": "Name", "source": "$entity$.getName()" }, { "columnName": "type", "order": 3, "locked": false, "visible": true, "customComponent": GEPPETTO.ArrayComponent, "displayName": "Type(s)", "source": "$entity$.getTypes().map(function (t) {return t.getPath()})" }, { "columnName": "controls", "order": 4, "locked": false, "visible": true, "customComponent": GEPPETTO.ControlsComponent, "displayName": "Controls", "source": "" }, { "columnName": "image", "order": 5, "locked": false, "visible": true, "customComponent": GEPPETTO.ImageComponent, "displayName": "Image", "cssClassName": "img-column", "source": "GEPPETTO.ModelFactory.getAllVariablesOfMetaType($entity$.$entity$_meta.getType(), 'ImageType')[0].getInitialValues()[0].value.data" } ]);
GEPPETTO.ControlPanel.setColumns(['name', 'type', 'controls', 'image']);
GEPPETTO.ControlPanel.setDataFilter(function(entities){var visualInstances = GEPPETTO.ModelFactory.getAllInstancesWithCapability(GEPPETTO.Resources.VISUAL_CAPABILITY, entities);var compositeInstances = [];for(var i=0; i<visualInstances.length; i++){if(visualInstances[i].getType().getMetaType() == GEPPETTO.Resources.COMPOSITE_TYPE_NODE){compositeInstances.push(visualInstances[i]);}}return compositeInstances;});

Model.getDatasources()[0].fetchVariable('VFB_00000001')
Model.getDatasources()[0].fetchVariable('FBbt_00100219')

Instances.getInstance("VFB_00000001.VFB_00000001_meta")
Instances.getInstance("FBbt_00100219.FBbt_00100219_meta")

var resolve3D = function(path){ try{ var i = Instances.getInstance(path+"."+path+"_obj"); i = Instances.getInstance(path+"."+path+"_swc"); }catch(ignore){} i.getType().resolve( (path)); }
var customHandler=function(node, path, widget){ var n;try {n = eval(path);} catch (ex) {node = undefined;}var meta=path+"."+path+"_meta";var target=widget; if(GEPPETTO.isKeyPressed("meta")){target=G.addWidget(1).addCustomNodeHandler(customHandler,'click');}if(n!=undefined){var metanode= Instances.getInstance(meta);target.setData(metanode).setName(n.getName());}else{Model.getDatasources()[0].fetchVariable(path,function(){Instances.getInstance(meta);target.setData(eval(meta)).setName(eval(path).getName()); resolve3D(path);});}};

G.addWidget(1).setPosition(687,80).setSize(551.8,681.8).setData(FBbt_00100219.FBbt_00100219_meta).setName(FBbt_00100219.getName()).addCustomNodeHandler(customHandler,'click');

window.Model.getLibraries()[1].getTypes()[0].resolve()
