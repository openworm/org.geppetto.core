window.initVFB();

Model.getDatasources()[0].fetchVariable('VFB_00000001');
Model.getDatasources()[0].fetchVariable('FBbt_00100219');

Instances.getInstance("VFB_00000001.VFB_00000001_meta");
Instances.getInstance("FBbt_00100219.FBbt_00100219_meta");

window.Model.getLibraries()[1].getTypes()[0].resolve();
