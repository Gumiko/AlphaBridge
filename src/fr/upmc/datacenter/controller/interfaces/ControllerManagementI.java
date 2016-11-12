package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;

public interface ControllerManagementI {
	public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception;
}
