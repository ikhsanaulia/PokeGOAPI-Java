package com.pokegoapi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.hs.gpxparser.GPXWriter;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Waypoint;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.pokegoapi.util.GPSUtils.Position.Latitude;
import com.pokegoapi.util.GPSUtils.Position.Longitude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public final class GPSUtils {
	
	public static void main (String[] args) throws FileNotFoundException, ParserConfigurationException, TransformerException {

		Position start = Position.create(106.832120, -6.178551);
		Position end = Position.create(106.827174, -6.175372);
		List<Position> waypoints = generateWaypoints(start, end, 20, 25);
		
		GPX gpx = new GPX();
		waypoints.stream().forEach(e -> {
			gpx.addWaypoint(new Waypoint(e.getLongitude().getValue(), e.getLatitude().getValue()));
			System.out.println(e.longitude.getValue() + "," + e.latitude.getValue());
		});
		
		GPXWriter wr = new GPXWriter();
		wr.writeGPX(gpx, new FileOutputStream(new File("waypoints.gpx")));
	}
	
	public static List<Position> generateWaypoints(Position start, Position end, double incr, double radius) {
		List<Position> waypoints = new LinkedList<GPSUtils.Position>();
		Position current = start;
		while (getDistance(current, end) > radius) {
			current = travel(current, incr, getAngle(current, end));
			waypoints.add(generateRandomPosition(current, 10));
		}
		return waypoints;
	}
	
	public static Position generateRandomPosition(Position position, double radius) {
		
	    Random random = new Random();
	    
	    double x0 = position.getLongitude().getValue();
	    double y0 = position.getLatitude().getValue();
	    
	    // Convert radius from meters to degrees
	    double radiusInDegrees = radius / 111000f;

	    double u = random.nextDouble();
	    double v = random.nextDouble();
	    double w = radiusInDegrees * Math.sqrt(u);
	    double t = 2 * Math.PI * v;
	    double x = w * Math.cos(t);
	    double y = w * Math.sin(t);

	    double new_x = x / Math.cos(y0);

	    return new Position(new Latitude((double) (y + y0)), new Longitude((double) (new_x + x0)));
	    
	}
	
	public static Position travel(Position position, double distance, double angle) {
		return toPosition(LatLngTool.travel(toLatLng(position), angle, distance, LengthUnit.METER));
	}
	
	public static double getAngle(Position position1, Position position2) {
		return LatLngTool.initialBearing(toLatLng(position1), toLatLng(position2));
	}
	
	public static double getDistance(Position position1, Position position2) {
		return LatLngTool.distance(toLatLng(position1), toLatLng(position2), LengthUnit.METER);
	}
	
	private static Position toPosition(LatLng latLng) {
		return new Position(new Latitude(latLng.getLatitude() + 90), new Longitude(latLng.getLongitude() + 90));
	}
	
	private static LatLng toLatLng(Position position) {
		return new LatLng(position.getLatitude().getValue() - 90, position.getLongitude().getValue() - 90);
	}
	
	@AllArgsConstructor
	@ToString(includeFieldNames = true)
	public static class Position {
		
		@Getter
		private final Latitude latitude;
		
		@Getter
		private final Longitude longitude;
		
		@Getter
		private final long elevation;
		
		public static Position create(double latitude, double longitude) {
			return new Position(new Latitude(latitude), new Longitude(longitude));
		}
		
		public Position(Latitude latitude, Longitude longitude) {
			this(latitude, longitude, 0);
		}
		
		@AllArgsConstructor
		@ToString(includeFieldNames = true)
		public static class Longitude {
			
			@Getter
			private final double value;
		}
		
		@AllArgsConstructor
		@ToString(includeFieldNames = true)
		public static class Latitude {
			
			@Getter
			private final double value;
		}
		
	}
	
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@ToString(includeFieldNames = true)
	public static enum Direction {
		NORTH(90), SOUTH(-90), EAST(180), WEST(0), NORTH_EAST(45), NORTH_WEST(135), SOUTH_EAST(-135), SOUTH_WEST(-45);
		
		@Getter
		private double angle;
	}
}
