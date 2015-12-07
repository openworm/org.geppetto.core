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

package org.geppetto.core.utilities;


/**
 * Utility class to read recording files
 * 
 * @author Jesus R Martinez (jesus@metacell.us)
 * @author Giovanni Idili (giovanni@openworm.org)
 *
 *         Note: most code for traversing is taken from HDF examples.
 */
public class RecordingReader
{
//IT FIXME: Still useful? This seems to do some iteration as opposed to the other RecordingReader...why are two classes with the same name?
//	/**
//	 * Method to traverse HDF5 recording file and populate aspects with all visualization variables
//	 * 
//	 * @param file
//	 * @param aspectMap
//	 */
//	public void populateVisualizationVariables(String file, HashMap<String, AspectNode> aspectMap) throws GeppettoExecutionException
//	{
//		this.populateVariablesByTree(file, aspectMap, AspectTreeType.VISUALIZATION_TREE);
//	}
//
//	/**
//	 * Method to traverse HDF5 recording file and populate aspects with all simulation variables
//	 * 
//	 * @param file
//	 * @param aspectMap
//	 * @throws GeppettoExecutionException
//	 */
//	public void populateSimulationVariables(String file, HashMap<String, AspectNode> aspectMap) throws GeppettoExecutionException
//	{
//		this.populateVariablesByTree(file, aspectMap, AspectTreeType.SIMULATION_TREE);
//	}
//
//	private void populateVariablesByTree(String file, HashMap<String, AspectNode> aspectMap, AspectTreeType treeType) throws GeppettoExecutionException
//	{
//		int file_id = -1;
//
//		try
//		{
//			// Open file
//			file_id = H5.H5Fopen(file, HDF5Constants.H5F_ACC_RDONLY, HDF5Constants.H5P_DEFAULT);
//
//			// Begin iteration using H5Ovisit
//			H5O_iterate_t iter_data = this.new H5O_iter_data();
//			H5O_iterate_cb iter_cb = this.new H5O_iter_callback(aspectMap, treeType);
//			H5.H5Ovisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb, iter_data);
//
//			// Repeat the same process using H5Lvisit
//			H5L_iterate_t iter_data2 = this.new H5L_iter_data();
//			H5L_iterate_cb iter_cb2 = this.new H5L_iter_callback(aspectMap, treeType);
//			H5.H5Lvisit(file_id, HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_NATIVE, iter_cb2, iter_data2);
//		}
//		catch(Exception e)
//		{
//			throw new GeppettoExecutionException(e);
//		}
//		finally
//		{
//			// Close and release resources.
//			if(file_id >= 0) try
//			{
//				H5.H5Fclose(file_id);
//			}
//			catch(HDF5LibraryException e)
//			{
//				throw new GeppettoExecutionException(e);
//			}
//		}
//	}
//
//	/************************************************************
//	 * Operator function for H5Lvisit. This function simply retrieves the info for the object the current link points to, and calls the operator function for H5Ovisit.
//	 ************************************************************/
//
//	private class idata
//	{
//		public String link_name = null;
//		public int link_type = -1;
//
//		idata(String name, int type)
//		{
//			this.link_name = name;
//			this.link_type = type;
//		}
//	}
//
//	private class H5L_iter_data implements H5L_iterate_t
//	{
//		public ArrayList<idata> iterdata = new ArrayList<idata>();
//	}
//
//	private class H5L_iter_callback implements H5L_iterate_cb
//	{
//		private HashMap<String, AspectNode> mapping;
//		private AspectTreeType treeType;
//
//		public H5L_iter_callback(HashMap<String, AspectNode> hashMap, AspectTreeType treeType)
//		{
//			this.mapping = hashMap;
//			this.treeType = treeType;
//		}
//
//		public int callback(int group, String name, H5L_info_t info, H5L_iterate_t op_data)
//		{
//
//			idata id = new idata(name, info.type);
//			((H5L_iter_data) op_data).iterdata.add(id);
//
//			H5O_info_t infobuf;
//			int ret = 0;
//			try
//			{
//				// Get type of the object and display its name and type. The name of
//				// the object is passed to this function by the Library.
//				infobuf = H5.H5Oget_info_by_name(group, name, HDF5Constants.H5P_DEFAULT);
//				H5O_iterate_cb iter_cbO = new H5O_iter_callback(this.mapping, this.treeType);
//				H5O_iterate_t iter_dataO = new H5O_iter_data();
//				ret = iter_cbO.callback(group, name, infobuf, iter_dataO);
//			}
//			catch(Exception e)
//			{
//
//			}
//
//			return ret;
//		}
//	}
//
//	private class H5O_iter_data implements H5O_iterate_t
//	{
//		public ArrayList<idata> iterdata = new ArrayList<idata>();
//
//		H5O_iter_data()
//		{
//		};
//	}
//
//	private class H5O_iter_callback implements H5O_iterate_cb
//	{
//		private HashMap<String, AspectNode> mapping;
//		private AspectTreeType treeType;
//
//		public H5O_iter_callback(HashMap<String, AspectNode> hashMap, AspectTreeType treeType)
//		{
//			this.mapping = hashMap;
//			this.treeType = treeType;
//		}
//
//		public int callback(int group, String name, H5O_info_t info, H5O_iterate_t op_data)
//		{
//			idata id = new idata(name, info.type);
//			((H5O_iter_data) op_data).iterdata.add(id);
//
//			if(info.type == HDF5Constants.H5O_TYPE_DATASET) createNodes(name);
//
//			return 0;
//		}
//
//		public void createNodes(String path)
//		{
//			String aspectPath = "";
//			String type = null;
//
//			// if path does not contain the tree we are looking for return
//			boolean contained = path.contains(treeType.toString());
//			if(contained)
//			{
//				aspectPath = path.split("/" + treeType.toString())[0];
//				type = treeType.toString();
//			}
//			else
//			{
//				return;
//			}
//
//			aspectPath = aspectPath.replace("/", ".");
//			AspectNode aspect = this.mapping.get(aspectPath);
//			if(aspect != null)
//			{
//				ACompositeValue node = null;
//
//				if(type.equals(treeType.toString()))
//				{
//					aspect.getSubTree(treeType).setModified(true);
//					node = aspect.getSubTree(treeType);
//				}
//
//				path = path.replace("/", ".");
//				path = "/" + path.replace(aspect.getInstancePath() + ".", "");
//				path = path.replace(".", "/");
//				path = path.replace("/" + type + "/", "");
//
//				StringTokenizer tokenizer = new StringTokenizer(path, "/");
//
//				while(tokenizer.hasMoreElements())
//				{
//					String current = tokenizer.nextToken();
//					boolean found = false;
//					for(INode child : node.getChildren())
//					{
//						if(child.getId().equals(current))
//						{
//							if(child instanceof ACompositeValue)
//							{
//								node = (ACompositeValue) child;
//							}
//							found = true;
//							break;
//						}
//					}
//					if(found)
//					{
//						continue;
//					}
//					else
//					{
//						if(tokenizer.hasMoreElements())
//						{
//							// not a leaf, create a composite state node
//							ACompositeValue newCompNode = new CompositeValue(current);
//							node.addChild(newCompNode);
//							node = newCompNode;
//						}
//						else if(treeType == AspectTreeType.SIMULATION_TREE)
//						{
//							// sim tree leaf node - it's a variable node
//							// TODO: should check MetaType.STATE_VARIABLE from recording
//							VariableValue newVarNode = new VariableValue(current);
//							node.addChild(newVarNode);
//						}
//						else if(treeType == AspectTreeType.VISUALIZATION_TREE)
//						{
//							// vis tree leaf node - it's a skeleton animation node
//							// TODO: should check MetaType.VISUAL_TRANSFORMATION from recording
//							SkeletonAnimationValue newSkeletonNode = new SkeletonAnimationValue(current);
//							node.addChild(newSkeletonNode);
//						}
//					}
//				}
//			}
//		}
//	}

}