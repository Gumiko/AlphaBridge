package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerActuatorI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;

public class ControllerActuatorOutboundPort extends AbstractOutboundPort implements ControllerActuatorI{
	public		ControllerActuatorOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerActuatorI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				ControllerActuatorOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerActuatorI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void addCores() throws Exception {
		((ControllerActuatorI)this.connector).addCores();
	}

	@Override
	public void removeCores() throws Exception {
		((ControllerActuatorI)this.connector).removeCores();
	}

	@Override
	public void addVM() throws Exception {
		((ControllerActuatorI)this.connector).addVM();
	}
	
	@Override
	public void removeVM() throws Exception {
		((ControllerActuatorI)this.connector).removeVM();
	}
	
}