package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
/**
* The class <code>ControllerManagementInboundPort</code> implements the
 * inbound port through which the component management methods are called.
* 
* <p>Created on : 2016-2017</p>
* 
* @author	Cédric Ribeiro et Mokrane Kadri
*/
public class ControllerManagementInboundPort extends AbstractInboundPort implements ControllerManagementI{
	private static final long serialVersionUID = 1L;

	/***
	 * 
	 * @param owner     owner component
	 * @throws Exception e
	 */
	public		ControllerManagementInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerManagementI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}
	
	/***
	 * @param uri       uri of the component
	 * @param owner     owner component
	 * @throws Exception e
	 */
	public				ControllerManagementInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#stopSending()
	 */
	@Override
	public void stopSending() throws Exception {
		final Controller c = (Controller) this.owner;
		c.stopSending();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#startSending()
	 */
	@Override
	public void startSending() throws Exception {
		final Controller c = (Controller) this.owner;
		c.startSending();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getNextControllerUri()
	 */
	@Override
	public String getNextControllerUri() {
		final Controller c = (Controller) this.owner;
		return c.getNextControllerUri();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getPreviousControllerUri()
	 */
	@Override
	public String getPreviousControllerUri() {
		final Controller c = (Controller) this.owner;
		return c.getPreviousControllerUri();
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setNextControllerUri(String)
	 */
	@Override
	public void setNextControllerUri(String controllerManagementUri) {
		final Controller c = (Controller) this.owner;
		c.setNextControllerUri(controllerManagementUri);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#setPreviousControllerUri(String)
	 */
	@Override
	public void setPreviousControllerUri(String controllerManagementUri) {
		final Controller c = (Controller) this.owner;
		c.setPreviousControllerUri(controllerManagementUri);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#bindSendingDataUri(String)
	 */
	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		final Controller c = (Controller) this.owner;
		c.bindSendingDataUri(DataInboundPortUri);

	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#getControllerRingDataInboundPortUri()
	 */
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		final Controller c = (Controller)this.owner;
		return c.getControllerRingDataInboundPortUri();
	}

}
