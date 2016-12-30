package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;

public interface ControllerSensorI extends OfferedI,RequiredI{
	
	/**
	 * Send the data of the Request dispatcher
	 * Average time of a request
	 * @throws Exception
	 */
	public void sendData() throws Exception;
}
