var w=G.addWidget(Widgets.BUTTONBAR);
w.renderBar("Menu", {"buttonGroupOne": {"buttonOne": {"actions": ["G.addWidget(8).setPosition(97,87)"],"icon": "gpt-inputs","label": "Draw vector","tooltip": "Opens a widget to draw a vector"}}});
w.setResizable(false);
w.showCloseButton(false);
w.setDraggable(false);
w.setPosition(97,5);
G.addWidget(1).setMessage("The draw vector widget has been ported from NEURON. </br></br><b>Instructions</b></br>Click on the 'Draw vector' button to open a new widget.</br>Click on the canvas to move the closest control point.</br>Click anywhere on a line to add a control point.</br>Drag and drop control points to move them.</br>Alt + click on a control point to remove it.</br>Click on the save icon to download an SVG of the graph.</br>Click on the vector icon to get the coordinates of the vector.</br></br><b>Prototype limitations</b></br>It is not possible to change the scale of the axis or zoom. </br>It is not possible to add control points given a set of coordinates.</br>It is not possible to rescale the widget.");
Popup1.setPosition(723,87);
Popup1.setSize(350,460);
Popup1.setName("Draw vector widget prototype.");
G.addWidget(8).setPosition(97,87)