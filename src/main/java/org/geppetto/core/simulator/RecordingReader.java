
package org.geppetto.core.simulator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

import org.apache.commons.lang.ArrayUtils;
import org.geppetto.core.common.GeppettoExecutionException;
import org.geppetto.core.data.model.ResultsFormat;
import org.geppetto.core.model.Recording;
import org.geppetto.core.utilities.StringSplitter;
import org.geppetto.model.ExperimentState;
import org.geppetto.model.VariableValue;
import org.geppetto.model.types.TypesPackage;
import org.geppetto.model.util.PointerUtility;
import org.geppetto.model.values.SkeletonAnimation;
import org.geppetto.model.values.SkeletonTransformation;
import org.geppetto.model.values.TimeSeries;
import org.geppetto.model.values.Unit;
import org.geppetto.model.values.Value;
import org.geppetto.model.values.ValuesFactory;

/**
 * @author matteocantarelli
 * @author giovanniidili
 */
public class RecordingReader
{
	private int currentRecordingIndex = 0;

	private Recording recording;

	private ResultsFormat recordingFormat;

	private boolean recordingOpened = false;

	public RecordingReader(Recording recording) throws GeppettoExecutionException
	{
		this(recording, ResultsFormat.GEPPETTO_RECORDING);
	}

	public RecordingReader(Recording recording, ResultsFormat format) throws GeppettoExecutionException
	{
		super();
		this.recording = recording;
		this.setRecordingFormat(format);
		openRecording();
	}

	/**
	 * @param variables
	 * @param tree
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readRecording(String recordedVariable, ExperimentState modelState, boolean readAll) throws GeppettoExecutionException
	{
		

		String recordingVariablePath = "/" + recordedVariable;

		recordingVariablePath = recordingVariablePath.replace(".", "/");

		this.readVariable(recordingVariablePath, recording.getHDF5(), modelState, readAll);

		if(!readAll)
		{
			currentRecordingIndex++;
		}
		
		
	}

	/**
	 * @return
	 */
	public int getAndIncrementCurrentIndex()
	{
		return currentRecordingIndex++;
	}

	/**
	 * @param path
	 * @param h5File
	 * @param parent
	 * @param readAll
	 * @throws GeppettoExecutionException
	 */
	public void readVariable(String path, H5File h5File, ExperimentState modelState, boolean readAll) throws GeppettoExecutionException
	{
		try
		{
			Dataset v = (Dataset) FileFormat.findObject(h5File, path);
			if(v==null)
			{
				v=(Dataset) FileFormat.findObject(h5File, PointerUtility.getPathWithoutTypes(path));
			}

			VariableValue variableValue = findVariableValue(path, modelState);

			Value value = null;

			Unit unit = ValuesFactory.eINSTANCE.createUnit();

			String metaType = "";
			Map<String, String> custom = null;

			// get metadata from recording node
			List<Attribute> attributes = v.getMetadata();

			for(Attribute a : attributes)
			{
				if(a.getName().equals("unit"))
				{
					unit.setUnit(((String[]) a.getValue())[0]);
				}
				else if(a.getName().equals("metatype"))
				{
					metaType = ((String[]) a.getValue())[0];
				}
				else if(a.getName().equals("custom_metadata"))
				{
					String customStr = ((String[]) a.getValue())[0];
					custom = StringSplitter.keyValueSplit(customStr, ";");
				}

			}

			Object readData = v.read();

			if(metaType.equals(TypesPackage.Literals.STATE_VARIABLE_TYPE.getName()))
			{

				value = ValuesFactory.eINSTANCE.createTimeSeries();
				((TimeSeries) value).setUnit(unit);

				if(readData instanceof double[])
				{
					double[] dr = (double[]) readData;
					if(!readAll)
					{
						((TimeSeries) value).getValue().add(dr[currentRecordingIndex]);
					}
					else
					{
						for(int i = 0; i < dr.length; i++)
						{
							((TimeSeries) value).getValue().add(dr[i]);
						}

					}

				}
				else if(readData instanceof float[])
				{
					throw new GeppettoExecutionException("Only double values supported");
				}
				else if(readData instanceof int[])
				{
					throw new GeppettoExecutionException("Only double values supported");
				}

			}
			else if(metaType.contains("VISUAL_TRANSFORMATION"))
			{
				value = ValuesFactory.eINSTANCE.createSkeletonAnimation();
				double[] dr = (double[]) readData;
				if(!readAll)
				{
					double[] flatMatrices = null;
					if(readData instanceof double[])
					{

						// get items of interest based on matrix dimension and items per step
						int itemsPerStep = Integer.parseInt(custom.get("items_per_step"));
						int startIndex = currentRecordingIndex * itemsPerStep;
						int endIndex = startIndex + (itemsPerStep);

						if(endIndex <= dr.length)
						{
							flatMatrices = Arrays.copyOfRange(dr, startIndex, endIndex);
						}
						else
						{
							throw new ArrayIndexOutOfBoundsException("ArrayIndexOutOfBounds");
						}
					}

					// set matrices on skeleton animation node
					SkeletonTransformation skeletonTransformation = ValuesFactory.eINSTANCE.createSkeletonTransformation();
					skeletonTransformation.getSkeletonTransformation().addAll(Arrays.asList(ArrayUtils.toObject(flatMatrices)));
					((SkeletonAnimation) value).getSkeletonTransformationSeries().add(skeletonTransformation);
				}
				else
				{
					if(readData instanceof double[])
					{
						// get items of interest based on matrix dimension and items per step
						int itemsPerStep = Integer.parseInt(custom.get("items_per_step"));
						int totalSteps = dr.length / itemsPerStep;

						for(int i = 0; i < totalSteps; i++)
						{
							double[] flatMatrices = null;

							int startIndex = i * itemsPerStep;
							int endIndex = startIndex + (itemsPerStep);

							if(endIndex <= dr.length)
							{
								flatMatrices = Arrays.copyOfRange(dr, startIndex, endIndex);
							}
							else
							{
								throw new ArrayIndexOutOfBoundsException("ArrayIndexOutOfBounds");
							}

							// set matrices on skeleton animation node
							SkeletonTransformation skeletonTransformation = ValuesFactory.eINSTANCE.createSkeletonTransformation();
							skeletonTransformation.getSkeletonTransformation().addAll(Arrays.asList(ArrayUtils.toObject(flatMatrices)));
							((SkeletonAnimation) value).getSkeletonTransformationSeries().add(skeletonTransformation);
						}
					}
				}
			}
			variableValue.setValue(value);

		}
		catch(Exception e)
		{
			throw new GeppettoExecutionException("Error reading a variable from the recording", e);
		}

	}

	private VariableValue findVariableValue(String path, ExperimentState modelState)
	{
		String instancePath = path.substring(1).replace("/", ".");
		for(VariableValue variableValue : modelState.getRecordedVariables())
		{
			if(variableValue.getPointer().getInstancePath().equals(instancePath))
			{
				return variableValue;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public int getRecordingIndex()
	{
		return currentRecordingIndex;
	}

	/**
	 * 
	 */
	public void resetRecordingIndex()
	{
		currentRecordingIndex = 0;
	}

	private void openRecording() throws GeppettoExecutionException
	{
		try
		{
			H5File h5file = recording.getHDF5();
			h5file.open();
		}
		catch(Exception e1)
		{
			throw new GeppettoExecutionException(e1);
		}

		this.recordingOpened = true;
	}

	public void closeRecording() throws GeppettoExecutionException
	{
		if(this.recordingOpened)
		{
			try
			{
				H5File h5file = recording.getHDF5();
				h5file.close();
			}
			catch(Exception e1)
			{
				throw new GeppettoExecutionException(e1);
			}

			this.recordingOpened = false;
		}
	}

	public ResultsFormat getRecordingFormat()
	{
		return recordingFormat;
	}

	public void setRecordingFormat(ResultsFormat recordingFormat)
	{
		this.recordingFormat = recordingFormat;
	}
}
