/*******************************************************************************
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 * <p/>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 * <p/>
 * Contributors:
 * OpenWorm - http://openworm.org/people.html
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.geppetto.core;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import ncsa.hdf.object.h5.H5File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.ecore.EClass;
import org.geppetto.core.common.GeppettoInitializationException;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.manager.SharedLibraryManager;
import org.geppetto.core.model.GeppettoModelAccess;
import org.geppetto.core.recordings.ConvertDATToRecording;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.GeppettoFactory;
import org.geppetto.model.GeppettoLibrary;
import org.geppetto.model.GeppettoModel;
import org.geppetto.model.VariableValue;
import org.geppetto.model.types.ArrayType;
import org.geppetto.model.types.CompositeType;
import org.geppetto.model.types.StateVariableType;
import org.geppetto.model.types.Type;
import org.geppetto.model.types.TypesFactory;
import org.geppetto.model.types.TypesPackage;
import org.geppetto.model.util.GeppettoVisitingException;
import org.geppetto.model.values.Pointer;
import org.geppetto.model.values.PointerElement;
import org.geppetto.model.values.ValuesFactory;
import org.geppetto.model.variables.Variable;
import org.geppetto.model.variables.VariablesFactory;
import org.junit.AfterClass;
import org.junit.Test;

public class TestConvertMuscleDATToRecordingClass {

    private static Log _logger = LogFactory.getLog(TestConvertMuscleDATToRecordingClass.class);

    @Test
    public void datToHDF5() throws Exception {
        long start = System.currentTimeMillis();
        ExperimentState experimentState = GeppettoFactory.eINSTANCE.createExperimentState();
        GeppettoModel gm = GeppettoFactory.eINSTANCE.createGeppettoModel();
        
        gm.getLibraries().add(SharedLibraryManager.getSharedCommonLibrary());
        createModel(experimentState, gm);

        _logger.info("Model created, took:" + (System.currentTimeMillis() - start));
        start=System.currentTimeMillis();
        GeppettoModelAccess geppettoModelAccess = new GeppettoModelAccess(gm);

        // Uncomment to save to XMI
        // GeppettoPackage.eINSTANCE.eClass();
        // Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        // Map<String, Object> m = reg.getExtensionToFactoryMap();
        // m.put("json", new JsonResourceFactory()); // sets the factory for the JSON type
        // m.put("xmi", new XMIResourceFactoryImpl()); // sets the factory for the XMI typ
        // ResourceSet resSet = new ResourceSetImpl();
        // Resource resource = resSet.createResource(URI.createURI("src/test/resources/bigSample/test.xmi"));
        // resource.getContents().add(gm);
        // resource.save(null);

        ConvertDATToRecording datConverter = new ConvertDATToRecording("src/test/resources/muscleSample/muscleSample.h5", geppettoModelAccess);

        Map<File, ResultsFormat> results = new HashMap<File, ResultsFormat>();
        List<String> variableNames = new ArrayList<String>();

        File mappingResultsFile = new File("src/test/resources/muscleSample/outputMapping.dat");
        results.put(mappingResultsFile, ResultsFormat.RAW);

        BufferedReader input;

        input = new BufferedReader(new FileReader(mappingResultsFile));

        // read rest of DAT file and extract values
        String filePath = "";
        String line = "";
        while ((line = input.readLine()) != null) {
            if (filePath.equals("")) {
                filePath = line;
            } else {
                String[] variables = line.split("\\s+");
                for (String s : variables) {
                    variableNames.add(s);
                }
                String fileName = mappingResultsFile.getParent() + "/" + filePath;
                datConverter.addDATFile(fileName, variables);
                results.put(new File(fileName), ResultsFormat.RAW);
                filePath = "";
            }
        }
        input.close();
        _logger.info("DAT processed, ready to convert, took" + (System.currentTimeMillis() - start));
        start=System.currentTimeMillis();
        datConverter.convert(experimentState);
        _logger.info("Conversion done, took" + (System.currentTimeMillis() - start));
        assertNotNull(datConverter.getRecordingsFile());

        H5File file = datConverter.getRecordingsFile();
        file.open();
        file.close();

    }

	private void createModel(ExperimentState experimentState, GeppettoModel gm) throws GeppettoVisitingException, GeppettoInitializationException
	{
		addVariableValue(gm, experimentState, "time");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].biophys(biophys).membraneProperties(membraneProperties).k_slow_all(k_slow_all).iDensity(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].biophys(biophys).membraneProperties(membraneProperties).k_fast_all(k_fast_all).iDensity(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].biophys(biophys).membraneProperties(membraneProperties).Leak_all(Leak_all).iDensity(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].biophys(biophys).membraneProperties(membraneProperties).ca_boyle_all(ca_boyle_all).iDensity(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).neuron(generic_iaf_cell)[0].v(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].v(StateVariable)");
		addVariableValue(gm, experimentState, "net1(net1).muscle(SingleCompMuscleCell)[0].caConc(StateVariable)");
		
	}

    private void addVariableValue(GeppettoModel gm, ExperimentState experimentState, String variable) throws GeppettoVisitingException, GeppettoInitializationException {
        VariableValue vv = GeppettoFactory.eINSTANCE.createVariableValue();
        Pointer p = ValuesFactory.eINSTANCE.createPointer();
        String varName, typeName;

        StringTokenizer st = new StringTokenizer(variable, ".");
        CompositeType currentParent = null;
        while (st.hasMoreTokens()) {
            String nextVarType = st.nextToken();
            if (variable.contains("(")) {
                varName = nextVarType.substring(0, nextVarType.indexOf("("));
                typeName = nextVarType.substring(nextVarType.indexOf("(") + 1, nextVarType.indexOf(")"));
            } else {
                varName = variable;
                typeName = "StateVariable";
            }

            Type type = getType(gm, typeName, varName, nextVarType);
            Variable v = getVariable(gm, varName, type, currentParent);

            if (type instanceof CompositeType) {
                currentParent = (CompositeType) type;
            } else if (type instanceof ArrayType) {
                currentParent = (CompositeType) ((ArrayType) type).getArrayType();

            } else {
                currentParent = null;
            }

            PointerElement pelem = ValuesFactory.eINSTANCE.createPointerElement();
            pelem.setVariable(v);
            pelem.setType(type);
            p.getElements().add(pelem);
        }

        vv.setPointer(p);
        experimentState.getRecordedVariables().add(vv);
    }

    private Variable getVariable(GeppettoModel gm, String varName, Type type, CompositeType container) {
        if (container != null) {
            for (Variable v : container.getVariables()) {
                if (v.getId().equals(varName)) {
                    return v;
                }
            }
        } else {
            for (Variable v : gm.getVariables()) {
                if (v.getId().equals(varName)) {
                    return v;
                }
            }
        }

        Variable v = VariablesFactory.eINSTANCE.createVariable();
        v.setId(varName);
        v.setName(varName);
        v.getTypes().add(type);
        if (container != null) {
            container.getVariables().add(v);
        } else {
            gm.getVariables().add(v);
        }
        return v;
    }

    private GeppettoLibrary neuroml = null;

    private Type getType(GeppettoModel gm, String typeName, String varName, String variable) throws GeppettoVisitingException, GeppettoInitializationException {
        if (typeName.equals("StateVariable")) {
            return (StateVariableType) getType(SharedLibraryManager.getSharedCommonLibrary(), TypesPackage.Literals.STATE_VARIABLE_TYPE);
        }
        if (neuroml == null) {
            neuroml = GeppettoFactory.eINSTANCE.createGeppettoLibrary();
            neuroml.setId("neuroml");
            gm.getLibraries().add(neuroml);
        }
        for (Type t : neuroml.getTypes()) {
            if (t.getId().equals(typeName)) {
                return t;
            }
        }
        Type newType = null;
        if (variable.contains("[")) {
            newType = TypesFactory.eINSTANCE.createArrayType();
            ((ArrayType) newType).setSize(1);

            CompositeType arrayType = TypesFactory.eINSTANCE.createCompositeType();
            arrayType.setId("arrayType");
            ((ArrayType) newType).setSize(1);
            ((ArrayType) newType).setArrayType(arrayType);
            neuroml.getTypes().add(arrayType);
        } else {
            newType = TypesFactory.eINSTANCE.createCompositeType();
        }
        newType.setId(typeName);
        neuroml.getTypes().add(newType);
        return newType;
    }

    /**
     * Usage commonLibraryAccess.getType(TypesPackage.Literals.PARAMETER_TYPE);
     *
     * @return
     * @throws GeppettoVisitingException
     */
    public Type getType(GeppettoLibrary library, EClass eclass) throws GeppettoVisitingException {
        for (Type type : library.getTypes()) {
            if (type.eClass().equals(eclass)) {
                return type;
            }
        }
        throw new GeppettoVisitingException("Type for eClass " + eclass + " not found in common library.");
    }

    @AfterClass
    public static void teardown() throws Exception {
        File sampleFile = new File("src/test/resources/muscleSample/muscleSample.h5");
        if (sampleFile.exists()) {
            sampleFile.delete();
            _logger.info("Deleting sample h5");
        }
    }

}
