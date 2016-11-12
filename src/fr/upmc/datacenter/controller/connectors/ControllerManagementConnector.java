package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;

public class ControllerManagementConnector
	extends		AbstractConnector
	implements	ControllerManagementI
	{
		@Override
		public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
			((ControllerManagementI)this.offering).linkComputer(c_out);
		}
	}

