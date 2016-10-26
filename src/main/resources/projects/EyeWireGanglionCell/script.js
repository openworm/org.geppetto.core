G.addWidget(Widgets.POPUP);
Popup1.setName("EyeWire Sample");
Popup1.setMessage(Project.getActiveExperiment().getDescription());
Popup1.setSize(172.80000019073486,394.80000019073486)
Popup1.setPosition(129,77)
G.incrementCameraZoom(-0.16)
G.incrementCameraRotate(0, 0, 0.07)
$(".simulation-controls").children().attr('disabled', 'disabled')
$("#genericHelpBtn").removeAttr('disabled')
G.setCameraPosition(-7197.325,144949.009,101269.908)
G.setCameraRotation(2.961,-1.114,1.791,75645.966)
GEPPETTO.SceneController.setWireframe(true);
G.autoRotate();