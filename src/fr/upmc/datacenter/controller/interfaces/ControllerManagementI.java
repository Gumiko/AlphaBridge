package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;

public interface ControllerManagementI extends OfferedI,RequiredI{
	
	public void stopSending() throws Exception;
	
	public void startSending() throws Exception;
	
	public String getNextControllerUri();
	
	public String getPreviousControllerUri();
	
	public void bindSendingDataUri(String DataInboundPortUri) throws Exception;

}
