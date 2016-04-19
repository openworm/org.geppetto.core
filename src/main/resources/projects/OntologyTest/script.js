Model.getDatasources()[0].fetchVariable('VFB_00000001')
Model.getDatasources()[0].fetchVariable('FBbt_00100219')

Instances.getInstance("VFB_00000001.VFB_00000001_meta")
Instances.getInstance("FBbt_00100219.FBbt_00100219_meta")

G.addWidget(1).setData(FBbt_00100219.FBbt_00100219_meta).setName(FBbt_00100219.getName())
G.addWidget(1).setData(VFB_00000001.VFB_00000001_meta).setName(VFB_00000001.getName())

window.Model.getLibraries()[1].getTypes()[0].resolve()
Popup2.setPosition(87,110)
Popup1.setPosition(587,110)