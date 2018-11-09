package hw9;

import hw7.CampusNode;
import hw7.IDEdge;
import hw7.RouteData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class RouteWrapper extends RouteData{
	private RouteData r;
	static CampusNode b1, b2;
	static ArrayList<Integer> x_coords, y_coords;

	RouteWrapper() {
		r = new RouteData();
		b1 = null;
		b2 = null;
		x_coords = new ArrayList<>();
		y_coords = new ArrayList<>();
	}

	Vector<String> getBuildingOptions() {
		Vector<String> building_names = new Vector<>();
		building_names.add("Select a building");
		Iterator<CampusNode> itr = r.getBuildingList();
		while (itr.hasNext()) {
			building_names.add(itr.next().name());
		}
		return building_names;
	}

	void updatePoint(Integer order, String bname) {
		if (bname == "Select a building") {
			if (order == 1) b1 = null;
			else b2 = null;
		}
		else {
			Iterator<CampusNode> itr = r.getBuildingList();
			while (itr.hasNext()) {
				CampusNode current_building = itr.next();
				if (current_building.name().equals(bname)) {
					if (order == 1) b1 = current_building;
					else b2 = current_building;
					break;
				}
			}
		}
		
		if (b1 == null || b2 == null) {
			x_coords = null;
			y_coords = null;
			return;
		}
		x_coords = new ArrayList<>();
		y_coords = new ArrayList<>();
		x_coords.add(b1.x());
		y_coords.add(b1.y());
		Iterator<IDEdge> path = r.getRoute(b1.id(), b2.id());
		if (path == null) return;
		while (path.hasNext()) {
			CampusNode next_node = r.getCampusNode(path.next().id2());
			x_coords.add(next_node.x());
			y_coords.add(next_node.y());
		}
	}	
}