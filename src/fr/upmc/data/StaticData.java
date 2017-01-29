package fr.upmc.data;


/**
 * The class <code>StaticData</code> 
 *
 *
 * <p><strong>Description</strong></p>
 * 
 * Contains static fields used by
 * 
 *  the controllers , the requests dispatchers while performing 
 *   performance  management actions
 * 

 * @author	Cédric Ribeiro et Mokrane Kadri
 *
 */
public class StaticData {
	/*Targeting the request per second for a request dispatcher in ms*/
	public static long AVERAGE_TARGET=4000;
	/*Percent that is acceptable, near the Average Target */
	public static double LOWER_PERCENT=0.6;
	public static double HIGHER_PERCENT=0.4;
	/* data push interval in the ring systeme */
	public static int RING_PUSH_INTERVAL=2000;
	/* data push interval to the controller */
	public static int DISPATCHER_PUSH_INTERVAL=5000;
	/* max allocation in one time */
	public static int MAX_ALLOCATION=25;
	/* max deallocation in one time */
	public static int MAX_DEALLOCATION=25;
}
