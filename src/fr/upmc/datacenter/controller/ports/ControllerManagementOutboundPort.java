package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;

public class ControllerManagementOutboundPort extends AbstractOutboundPort implements ControllerManagementI{
	public	ControllerManagementOutboundPort(ComponentI owner) throws Exception
	{
		super(ControllerManagementI.class, owner) ;
		assert	owner != null && owner instanceof Controller ;
	}

	public	ControllerManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ControllerManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
		((ControllerManagementI)this.connector).linkComputer(c_out);
	}

}
