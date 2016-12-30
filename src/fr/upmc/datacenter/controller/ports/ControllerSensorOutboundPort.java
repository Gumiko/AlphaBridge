package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerSensorI;

public class ControllerSensorOutboundPort extends AbstractOutboundPort implements ControllerSensorI{
	public		ControllerSensorOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ControllerSensorI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				ControllerSensorOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerSensorI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void sendData() throws Exception {
		((ControllerSensorI)this.connector).sendData();
	}
	
}