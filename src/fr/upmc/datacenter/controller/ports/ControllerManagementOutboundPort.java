package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;

public class ControllerManagementOutboundPort extends AbstractOutboundPort
implements	ControllerManagementI{


	public		ControllerManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerManagementI.class, owner) ;

		assert	owner != null ;
	}

	public				ControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null;
	}

	@Override
	public void stopSending() throws Exception {
		((ControllerManagementI)this.connector).stopSending();
	}

	@Override
	public void startSending() throws Exception {
		((ControllerManagementI)this.connector).startSending();
	}

	@Override
	public String getNextControllerUri() throws Exception {
		return ((ControllerManagementI)this.connector).getNextControllerUri();
	}

	@Override
	public String getPreviousControllerUri() throws Exception {
		return ((ControllerManagementI)this.connector).getPreviousControllerUri();
	}
	
	@Override
	public void setNextControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.connector).setNextControllerUri(controllerManagementUri);
	}

	@Override
	public void setPreviousControllerUri(String controllerManagementUri) throws Exception {
		 ((ControllerManagementI)this.connector).setPreviousControllerUri(controllerManagementUri);
	}

	@Override
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception {
		((ControllerManagementI)this.connector).bindSendingDataUri(DataInboundPortUri);
	}
	
	@Override
	public String getControllerRingDataInboundPortUri() throws Exception{
		return ((ControllerManagementI)this.connector).getControllerRingDataInboundPortUri();
	}
}
