package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerActuatorI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;

public class ControllerActuatorInboundPort extends	AbstractInboundPort implements ControllerActuatorI
{
	
	private static final long serialVersionUID = 1L;

	public		ControllerActuatorInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerActuatorI.class, owner) ;

		assert	owner != null && owner instanceof AdmissionController ;
	}

	public				ControllerActuatorInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerActuatorI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void addCores() throws Exception {
		final Controller c = (Controller) this.owner;
		 c.addCores();
	}

	@Override
	public void removeCores() throws Exception {
		final Controller c = (Controller) this.owner;
		 c.removeCores();
	}

	@Override
	public void addVM() throws Exception {
		final Controller c = (Controller) this.owner;
		 c.addVM();
	}
	
	@Override
	public void removeVM() throws Exception {
		final Controller c = (Controller) this.owner;
		 c.removeVM();
	}

}
