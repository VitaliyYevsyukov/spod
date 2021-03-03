package presenters.detector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoadDetectorModel {
	
	private List<Detector> detectorsList;
	private Map<RoadDetectorHBoxCell, List<Detector>> mapOfMultiZonesDetector;

	public RoadDetectorModel() {
		mapOfMultiZonesDetector = new LinkedHashMap<>();
		detectorsList = new ArrayList<>();
	}

	public List<Detector> getDetectorsList() {
		return detectorsList;
	}

	public void setDetectorsList(List<Detector> detectorsList) {
		this.detectorsList = detectorsList;
	}

	public Map<RoadDetectorHBoxCell, List<Detector>> getMapOfMultiZonesDetector() {
		return mapOfMultiZonesDetector;
	}

	public void setMapOfMultiZonesDetector(Map<RoadDetectorHBoxCell, List<Detector>> mapOfMultiZonesDetector) {
		this.mapOfMultiZonesDetector = mapOfMultiZonesDetector;
	}
	
}
