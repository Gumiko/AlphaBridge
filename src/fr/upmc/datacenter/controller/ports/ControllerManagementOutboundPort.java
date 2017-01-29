package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
/**
* The class <code>ControllerManagementOutboundPort</code> implements the
 * inbound port through which the component management methods are called.
* 
* <p>Created on : 2016-2017</p>
* 
* @author	Cédric Ribeiro et Mokrane Kadri
*/
public class ControllerManagementOutboundPort extends AbstractOutboundPort
implements	ControllerManagementI{

    /***
     * 
     * @param owner       owner component
     * @throws Exception
     */
	public		ControllerManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerManagementI.class, owner) ;

		assert	owner != null ;
	}

	/***
	 *  
	 * @param uri             uri of the component
	 * @param owner           owner component
	 * @throws Exception
	 */
	public				ControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null;
	}

	
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#stopSending()
	 */
	@Override
	public void stopSending() throws Exception {
		((ControllerManagementI)this.connector).stopSending();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#startSending()
	 */
	@Override
	public void startSending() throws Exception {
		((ControllerManagementI)this.connector).startSending();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getNextControllerUri()
	 */
	@Override
	public String getNextControllerUri() throws Exception {
		return ((ControllerManagementI)this.connector).getNextControllerUri();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getPreviousControllerUri()
	 */
	@Override
	public String getPreviousControllerUri() throws Exception {
		return ((ControllerManagementI)this.connector).getPreviousControllerUri();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setNextControllerUri(String)
	 */
	@Override
	public void setNextControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.connector).setNextControllerUri(controllerManagementUri);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setPreviousControllerUri(String)
	 */
	@Override
	public void setPreviousControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.connector).setPreviousControllerUri(controllerManagementUri);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#bindSendingDataUri(String)
	 */
	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		((ControllerManagementI)this.connector).bindSendingDataUri(DataInboundPortUri);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getControllerRingDataInboundPortUri()
	 */
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		return ((ControllerManagementI)this.connector).getControllerRingDataInboundPortUri();
	}
}
