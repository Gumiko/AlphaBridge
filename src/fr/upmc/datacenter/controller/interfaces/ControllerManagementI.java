package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.ring.ports.RingDynamicStateDataInboundPort;
/**
 * The interface <code>ControllerManagementI</code> defines the methods
 * to manage the Controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface ControllerManagementI extends OfferedI,RequiredI{
	/**
	 * Tell the Controller to stop sending Data
	 * @throws Exception e
	 */
	public void stopSending() throws Exception;
	/**
	 * Tell the Controller to start sending Data
	 * @throws Exception e
	 */
	public void startSending() throws Exception;

	/**
	 * Retrieve the URI of the next Controller in the Data Ring
	 * @return String
	 * @throws Exception e
	 */
	public String getNextControllerUri() throws Exception;
	/**
	 * Retrieve the URI of the previous Controller in the Data Ring
	 * @return String
	 * @throws Exception e
	 */
	public String getPreviousControllerUri() throws Exception;

	/**
	 * Connect the RingDynamicStateDataOutboundPort of the Controller to the RingDynamicStateDataInboundPort
	 * @param RingDynamicStateDataInboundPort  URI of the RingDynamicStateDataInboundPort
	 * @throws Exception e
	 */
	public void bindSendingDataUri(String RingDynamicStateDataInboundPort) throws Exception;


	/**
	 * Set the next Controller Uri to something else 
	 * Used when adding/removing a Controller to the Ring
	 * @param controllerManagementInboundPortUri  URI of the RingDynamicStateDataInboundPort
	 * @throws Exception e
	 */
	void setNextControllerUri(String controllerManagementInboundPortUri) throws Exception;


	/**
	 * Set the previous Controller Uri to something else 
	 * Used when adding/removing a Controller to the Ring
	 * @param controllerManagementInboundPortUri  URI of the controllerManagementInboundPortUri
	 * @throws Exception e
	 */
	void setPreviousControllerUri(String controllerManagementInboundPortUri) throws Exception;

	/**
	 * Get the RingDynamicStateDataInboundPort URI of the Controller
	 * @return String
	 * @throws Exception e
	 */
	String getControllerRingDataInboundPortUri() throws Exception;

}
