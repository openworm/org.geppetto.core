GEPPETTO.Init.flipCameraY();
GEPPETTO.Init.flipCameraZ();
GEPPETTO.SceneController.setWireframe(true);

Model.getDatasources()[0].fetchVariable('VFB_00000001');
Model.getDatasources()[0].fetchVariable('FBbt_00100219');

Instances.getInstance("VFB_00000001.VFB_00000001_meta");
Instances.getInstance("FBbt_00100219.FBbt_00100219_meta");

//set separable colours:
colours = ["0x000000","0x00ff00","0xff00ff","0x0000ff","0xffd300","0x0084f6","0x008d46","0xa7613e","0x4f006a","0x00fff6","0x3e7b8d","0xeda7ff","0xd3ff95","0xb94fff","0xe51a58","0x848400","0x00ff95","0x61002c","0xf68412","0xcaff00","0x2c3e00","0x0035c1","0xffca84","0x002c61","0x9e728d","0x4fb912","0x9ec1ff","0x959e7b","0xff7bb0","0x9e0900","0xffb9b9","0x8461ca","0x9e0072","0x84dca7","0xff00f6","0x00d3ff","0xff7258","0x583e35","0x003e35","0xdc61dc","0x6172b0","0xb9ca2c","0x12b0a7","0x611200","0x2c002c","0x5800ca","0x95c1ca","0xd39e23","0x84b058","0xe5edb9","0xf6d3ff","0xb94f61","0x8d09a7","0x6a4f00","0x003e9e","0x7b3e7b","0x3e7b61","0xa7ff61","0x0095d3","0x3e7200","0xb05800","0xdc007b","0x9e9eff","0x4f4661","0xa7fff6","0xe5002c","0x72dc72","0xffed7b","0xb08d46","0x6172ff","0xdc4600","0x000072","0x090046","0x35ed4f","0x2c0000","0xa700ff","0x00f6c1","0x9e002c","0x003eff","0xf69e7b","0x6a7235","0xffff46","0xc1b0b0","0x727272","0xc16aa7","0x005823","0xff848d","0xb08472","0x004661","0x8dff12","0xb08dca","0x724ff6","0x729e00","0xd309c1","0x9e004f","0xc17bff","0x8d95b9","0xf6a7d3","0x232309","0xff6aca","0x008d12","0xffa758","0xe5c19e","0x00122c","0xc1b958","0x00c17b","0x462c00","0x7b3e58","0x9e46a7","0x4f583e","0x6a35b9","0x72b095","0xffb000","0x4f3584","0xb94635","0x61a7ff","0xd38495","0x7b613e","0x6a004f","0xed58ff","0x95d300","0x35a7c1","0x00009e","0x7b3535","0xdcff6a","0x95d34f","0x84ffb0","0x843500","0x4fdce5","0x462335","0x002c09","0xb9dcc1","0x588d4f","0x9e7200","0xca4684","0x00c146","0xca09ed","0xcadcff","0x0058a7","0x2ca77b","0x8ddcff","0x232c35","0xc1ffb9","0x006a9e","0x0058ff","0xf65884","0xdc7b46","0xca35a7","0xa7ca8d","0x4fdcc1","0x6172d3","0x6a23ff","0x8d09ca","0xdcc12c","0xc1b97b","0x3e2358","0x7b6195","0xb97bdc","0xffdcd3","0xed5861","0xcab9ff","0x3e5858","0x729595","0x7bff7b","0x95356a","0xca9eb9","0x723e1a","0x95098d","0xf68ddc","0x61b03e","0xffca61","0xd37b72","0xffed9e","0xcaf6ff","0x58c1ff","0x8d61ed","0x61b972","0x8d6161","0x46467b","0x0058d3","0x58dc09","0x001a72","0xd33e2c","0x959546","0xca7b00","0x4f6a8d","0x9584ff","0x46238d","0x008484","0xf67235","0x9edc84","0xcadc6a","0xb04fdc","0x4f0912","0xff1a7b","0x7bb0d3","0x1a001a","0x8d35f6","0x5800a7","0xed8dff","0x969696"];
coli = 10;
var setSepCol = function(entityPath) { var c = coli; coli++; if (coli > 199) { coli = 0; }; Instances.getInstance(entityPath).setColor(colours[c], true).setOpacity(0.8, true); if (c = 0) { Instances.getInstance(entityPath).setOpacity(0.2, true); }; };

var resolve3D = function(path, callback) { var instance = undefined; try { instance = Instances.getInstance(path + "." + path + "_swc"); } catch (ignore) { } if(instance == undefined){ try { instance = Instances.getInstance(path + "." + path + "_obj"); } catch (ignore) { } } if(instance!=undefined){ instance.getType().resolve(function() { setSepCol(path); if (callback != undefined) { callback(); } }); } };

var customHandler=function(node, path, widget){ var n;try {n = eval(path);} catch (ex) {node = undefined;}var meta=path+"."+path+"_meta";var target=widget; if(GEPPETTO.isKeyPressed("meta")){target=G.addWidget(1).addCustomNodeHandler(customHandler,'click');}if(n!=undefined){var metanode= Instances.getInstance(meta);target.setData(metanode).setName(n.getName());}else{Model.getDatasources()[0].fetchVariable(path,function(){Instances.getInstance(meta);target.setData(eval(meta)).setName(eval(path).getName()); resolve3D(path);});}};

G.addWidget(1).setPosition((window.innerWidth-(Math.ceil(window.innerWidth/5)+10)),10).setSize((window.innerHeight-20),Math.ceil(window.innerWidth/5)).setData(FBbt_00100219.FBbt_00100219_meta).setName(FBbt_00100219.getName()).addCustomNodeHandler(customHandler,'click');

window.Model.getLibraries()[1].getTypes()[0].resolve();
