package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;

public class ControllerManagementInboundPort extends AbstractInboundPort implements ControllerManagementI{
	private static final long serialVersionUID = 1L;

	public		ControllerManagementInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerManagementI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				ControllerManagementInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void stopSending() throws Exception {
		final Controller c = (Controller) this.owner;
		c.stopSending();
	}

	@Override
	public void startSending() throws Exception {
		final Controller c = (Controller) this.owner;
		c.startSending();
	}

	@Override
	public String getNextControllerUri() {
		final Controller c = (Controller) this.owner;
		return c.getNextControllerUri();
	}

	@Override
	public String getPreviousControllerUri() {
		final Controller c = (Controller) this.owner;
		return c.getPreviousControllerUri();
	}
	
	@Override
	public void setNextControllerUri(String controllerManagementUri) {
		final Controller c = (Controller) this.owner;
		c.setNextControllerUri(controllerManagementUri);
	}

	@Override
	public void setPreviousControllerUri(String controllerManagementUri) {
		final Controller c = (Controller) this.owner;
		c.setPreviousControllerUri(controllerManagementUri);
	}

	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		final Controller c = (Controller) this.owner;
		c.bindSendingDataUri(DataInboundPortUri);

	}
	
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		final Controller c = (Controller)this.owner;
		return c.getControllerRingDataInboundPortUri();
	}

}
