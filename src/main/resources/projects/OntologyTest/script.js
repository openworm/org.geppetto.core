GEPPETTO.ControlPanel.setColumns(['name', 'type', 'controls', 'image'], [ { "columnName": "path", "order": 1, "locked": false, "visible": true, "displayName": "Path", "source": "$entity$.getPath()" }, { "columnName": "name", "order": 2, "locked": false, "visible": true, "displayName": "Name", "source": "$entity$.getName()" }, { "columnName": "type", "order": 3, "locked": false, "visible": true, "customComponent": GEPPETTO.ArrayComponent, "displayName": "Type(s)", "source": "$entity$.getTypes().map(function (t) {return t.getPath()})" }, { "columnName": "controls", "order": 4, "locked": false, "visible": true, "customComponent": GEPPETTO.ControlsComponent, "displayName": "Controls", "source": "" }, { "columnName": "image", "order": 5, "locked": false, "visible": true, "customComponent": GEPPETTO.ImageComponent, "displayName": "Image", "cssClassName": "img-column", "source": "GEPPETTO.ModelFactory.getAllVariablesOfMetaType($entity$.$entity$_meta.getType(), 'ImageType')[0].getInitialValues()[0].value.data" } ]);

Model.getDatasources()[0].fetchVariable('VFB_00000001')
Model.getDatasources()[0].fetchVariable('FBbt_00100219')

Instances.getInstance("VFB_00000001.VFB_00000001_meta")
Instances.getInstance("FBbt_00100219.FBbt_00100219_meta")

var resolve3D = function(path){ var i = Instances.getInstance(path+"."+path+"_obj"); i.getType().resolve();}
var customHandler=function(node, path){ var n;try {n = eval(path);} catch (ex) {node = undefined;}var meta=path+"."+path+"_meta"; if(n!=undefined){var metanode= Instances.getInstance(meta);G.addWidget(1).setData(metanode).setName(n.getName()).addCustomNodeHandler(customHandler,'click');}else{Model.getDatasources()[0].fetchVariable(path,function(){Instances.getInstance(meta);G.addWidget(1).setData(eval(meta)).setName(eval(path).getName()).addCustomNodeHandler(customHandler,'click'); resolve3D(path);});}};

G.addWidget(1).setData(FBbt_00100219.FBbt_00100219_meta).setName(FBbt_00100219.getName()).setPosition(92,110).addCustomNodeHandler(customHandler,'click');
G.addWidget(1).setData(VFB_00000001.VFB_00000001_meta).setName(VFB_00000001.getName()).setPosition(587,110).addCustomNodeHandler(customHandler,'click');

window.Model.getLibraries()[1].getTypes()[0].resolve()




