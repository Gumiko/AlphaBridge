package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerSensorI;

public class ControllerSensorInboundPort extends AbstractInboundPort implements ControllerSensorI
{
	
	private static final long serialVersionUID = 1L;

	public		ControllerSensorInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerSensorI.class, owner) ;

		assert	owner != null && owner instanceof AdmissionController ;
	}

	public				ControllerSensorInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerSensorI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void sendData() throws Exception {
		final Controller c = (Controller) this.owner;
		 c.sendData();
	}


}