/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011 - 2015 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.geppetto.core.simulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5L_iterate_t;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_cb;
import ncsa.hdf.hdf5lib.callbacks.H5O_iterate_t;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5L_info_t;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.features.IWatchableVariableListFeature;
import org.geppetto.core.model.ModelInterpreterException;
import org.geppetto.core.model.ModelWrapper;
import org.geppetto.core.model.RecordingModel;
import org.geppetto.core.model.runtime.ACompositeNode;
import org.geppetto.core.model.runtime.ANode;
import org.geppetto.core.model.runtime.AspectNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode;
import org.geppetto.core.model.runtime.CompositeNode;
import org.geppetto.core.model.runtime.EntityNode;
import org.geppetto.core.model.runtime.RuntimeTreeRoot;
import org.geppetto.core.model.runtime.VariableNode;
import org.geppetto.core.model.runtime.AspectSubTreeNode.AspectTreeType;
import org.geppetto.core.services.GeppettoFeature;

/**
 * Abstract feature class for listing variable watch
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 *
 */
public class AWatchableVariableListFeature implements IWatchableVariableListFeature{

	private GeppettoFeature type = GeppettoFeature.WATCHABLE_VARIABLE_LIST_FEATURE;
	private RuntimeTreeRoot root;
	private static Log _logger = LogFactory.getLog(AWatchableVariableListFeature.class);

	@Override
	public GeppettoFeature getType()
	{
		return type;
	}

	@Override
	public boolean listWatchableVariables(AspectNode aspectNode) throws ModelInterpreterException
	{
		long start = System.currentTimeMillis();

		boolean modified = true;

		ModelWrapper model = (ModelWrapper) aspectNode.getModel();
		Collection<Object> models = model.getModels();
		Iterator i = models.iterator();
		while(i.hasNext()){
			Object m = i.next();
			if(m instanceof RecordingModel)
			{
				//Get scene root
				ANode n = aspectNode.getParent();
				while(n.getParent()!=null){
					n  = n.getParent();
				}
				
				this.root = (RuntimeTreeRoot) n;
				//traverse scene root to get all simulation trees for all aspects
				FindSimulationTree mappingVisitor = new FindSimulationTree();
				this.root.apply(mappingVisitor);
			
				try {
					//we send the recording hdf5 location, plus map of aspects to populate
					//them with recordings extracts
					this.readRecording(((RecordingModel) m).getHDF5().getAbsolutePath(), mappingVisitor.getAspects(), false);
				} catch (GeppettoExecutionException e) {
					throw new ModelInterpreterException(e);
				}
			}
		}
		return modified;
	}
	
	public void readRecording(String file,HashMap<String, AspectNode> hashMap, boolean readAll) throws GeppettoExecutionException
	{
		int file_id = -1;

        try {
            //Open file
            file_id = H5.H5Fopen(file, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);

            //Begin iteration using H5Ovisit
            H5O_iterate_t iter_data = new H5O_iter_data();
            H5O_iterate_cb iter_cb = new H5O_iter_callback(hashMap);
            H5.H5Ovisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb, iter_data);
            //Repeat the same process using H5Lvisit
            H5L_iterate_t iter_data2 = new H5L_iter_data();
            H5L_iterate_cb iter_cb2 = new H5L_iter_callback(hashMap);
            H5.H5Lvisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb2, iter_data2);

        }
        catch (Exception e) {
           throw new GeppettoExecutionException(e);
        }
        finally {
            //Close and release resources.
            if(file_id >= 0)
				try {
					H5.H5Fclose (file_id);
				} catch (HDF5LibraryException e) {
					throw new GeppettoExecutionException(e);
				}
        }
	}
}

/************************************************************
 * Operator function for H5Lvisit.  This function simply
 * retrieves the info for the object the current link points
 * to, and calls the operator function for H5Ovisit.
 ************************************************************/

class idata {
    public String link_name = null;
    public int link_type = -1;
    idata(String name, int type) {
        this.link_name = name;
        this.link_type = type;
    }
}

class H5L_iter_data implements H5L_iterate_t {
    public ArrayList<idata> iterdata = new ArrayList<idata>();
}

class H5L_iter_callback implements H5L_iterate_cb {
    private HashMap<String, AspectNode> mapping;

	public H5L_iter_callback(HashMap<String, AspectNode> hashMap) {
		this.mapping = hashMap;
	}

	public int callback(int group, String name, H5L_info_t info, H5L_iterate_t op_data) {

        idata id = new idata(name, info.type);
        ((H5L_iter_data)op_data).iterdata.add(id);

        H5O_info_t infobuf;
        int ret = 0;
        try {
            //Get type of the object and display its name and type. The name of the object is passed to this function by the Library.
            infobuf = H5.H5Oget_info_by_name (group, name, HDF5Constants.H5P_DEFAULT);
            H5O_iterate_cb iter_cbO = new H5O_iter_callback(this.mapping);
            H5O_iterate_t iter_dataO = new H5O_iter_data();
            ret=iter_cbO.callback(group, name, infobuf, iter_dataO);
        }
        catch (Exception e) {
            
        }

        return ret;
    }
}

class H5O_iter_data implements H5O_iterate_t {
    public ArrayList<idata> iterdata = new ArrayList<idata>();
}


class H5O_iter_callback implements H5O_iterate_cb {
    private HashMap<String, AspectNode> mapping;
	private String VISUALIZATION_TREE = "VISUALIZATION_TREE";
	private String SIMULATION_TREE = "SIMULATION_TREE";
	

	public H5O_iter_callback(HashMap<String, AspectNode> hashMap) {
		this.mapping = hashMap;
	}

	public int callback(int group, String name, H5O_info_t info, H5O_iterate_t op_data) {
        idata id = new idata(name, info.type);
        ((H5O_iter_data)op_data).iterdata.add(id);
        
        if(info.type == HDF5Constants.H5O_TYPE_DATASET)
            createNodes(name);

        return 0;
    }
    
    public void createNodes(String path){
    	String aspectPath = "";
		String type=null;
		//if path contains visualization tree, return, doesn't belong 
		//here in simulation tree populating
    	if(path.contains(VISUALIZATION_TREE)){
    		aspectPath = path.split("/"+VISUALIZATION_TREE)[0];
    		type = VISUALIZATION_TREE;
    		return;
    	}else if(path.contains(SIMULATION_TREE)){
    		aspectPath = path.split("/"+SIMULATION_TREE)[0];
    		type = SIMULATION_TREE;
    	}

    	aspectPath = aspectPath.replace("/",".");
    	AspectNode aspect = this.mapping.get(aspectPath);
    	if(aspect!=null){
    		ACompositeNode node = null;
 
    		//Check for Visualization tree if it wants to be added
    		if(type.equals(SIMULATION_TREE)){
    			aspect.getSubTree(AspectTreeType.SIMULATION_TREE).setModified(true);
    			node = aspect.getSubTree(AspectTreeType.SIMULATION_TREE);
    		}

    		path = path.replace("/",".");
    		String pr = aspect.getInstancePath();
    		path = "/" + path.replace(aspect.getInstancePath() + ".", "");
    		path = path.replace(".", "/");
    		path = path.replace("/"+type+"/", "");

    		StringTokenizer tokenizer = new StringTokenizer(path, "/");

    		VariableNode newVariableNode = null;
    		while(tokenizer.hasMoreElements())
    		{
    			String current = tokenizer.nextToken();
    			boolean found = false;
    			for(ANode child : node.getChildren())
    			{
    				if(child.getId().equals(current))
    				{
    					if(child instanceof ACompositeNode)
    					{
    						node = (ACompositeNode) child;
    					}
    					if(child instanceof VariableNode)
    					{
    						newVariableNode = (VariableNode) child;
    					}
    					found = true;
    					break;
    				}
    			}
    			if(found)
    			{
    				continue;
    			}
    			else
    			{
    				if(tokenizer.hasMoreElements())
    				{
    					// not a leaf, create a composite state node
    					ACompositeNode newNode = new CompositeNode(current);
    					node.addChild(newNode);
    					node = newNode;
    				}
    				else
    				{
    					// it's a leaf node
    					VariableNode newNode = new VariableNode(current);
    					newVariableNode = newNode;
    					node.addChild(newNode);
    				}
    			}
    		}
    	}
    }
}
