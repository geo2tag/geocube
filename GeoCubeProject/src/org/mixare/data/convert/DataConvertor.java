/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare.data.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.mixare.data.DataSource;
import org.mixare.lib.marker.Marker;
import org.mixare.lib.reality.PhysicalPlace;

/**
 * This class is responsible for converting raw data to marker data
 * The class will first check which processor is needed before it handles the data
 * After that it will convert the data to the format the processor wants. I.E. JSON / XML
 * @author A. Egal
 */
public class DataConvertor {
	
	private List<DataProcessor> dataProcessors = new ArrayList<DataProcessor>();
	
	private static DataConvertor instance;
	
	public static DataConvertor getInstance(){
		if(instance == null){
			instance = new DataConvertor();
			instance.addDefaultDataProcessors();
		}
		return instance;
	}
	
	public void clearDataProcessors() {
		dataProcessors.clear();
		addDefaultDataProcessors();
	}
	
	public void addDataProcessor(DataProcessor dataProcessor){
		dataProcessors.add(dataProcessor);
	}

	public List<Marker> load(String url, String rawResult, DataSource ds){
//        DataProcessor dataProcessor = new WikiDataProcessor();
          DataProcessor dataProcessor = new Geo2TagDataProcessor();

		try {
			return dataProcessor.load(rawResult, ds.getTaskId(), ds.getColor());
		} catch (JSONException e) {
            e.printStackTrace();
		}
		return null;
	}
	
	private void addDefaultDataProcessors(){
//        dataProcessors.add(new WikiDataProcessor());
        dataProcessors.add(new Geo2TagDataProcessor());
	}
	
	public static String getOSMBoundingBox(double lat, double lon, double radius) {
		String bbox = "[bbox=";
		PhysicalPlace lb = new PhysicalPlace(); // left bottom
		PhysicalPlace rt = new PhysicalPlace(); // right top
		PhysicalPlace.calcDestination(lat, lon, 225, radius*1414, lb); // 1414: sqrt(2)*1000
		PhysicalPlace.calcDestination(lat, lon, 45, radius*1414, rt);
		bbox+=lb.getLongitude()+","+lb.getLatitude()+","+rt.getLongitude()+","+rt.getLatitude()+"]";
		return bbox;

		//return "[bbox=16.365,48.193,16.374,48.199]";
	}
	
}
