package fr.upmc.data;

public class StaticData {
	/*Targeting the request per second for a request dispatcher in ms*/
	public static long AVERAGE_TARGET=2000;
	/*Percent that is acceptable, near the Average Target */
	public static double PERCENT=0.4;
	
	public static int RING_PUSH_INTERVAL=3000;
}
