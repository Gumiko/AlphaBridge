package fr.upmc.datacenter.admissioncontroller.interfaces;

import fr.upmc.components.interfaces.OfferedI;
import fr.upmc.components.interfaces.RequiredI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The interface <code>ApplicationRequestI</code>defines the methods
 * to manage an admission of a request generator to the controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface ApplicationRequestI extends OfferedI,RequiredI{

	/**
	 * Send a request generator to the controller
	 * @param application			id of the application.
	 * @param requestGeneratorURI			uri of the request generator.
	 * @param rg_rsop			request submission outbound port of the request generator
	 * @param rg_rnip			request notification outbound port of the request generator
	 * @return boolean
	 * @throws Exception e
	 */
	
	boolean acceptApplication(Integer application, String requestGeneratorURI, String rg_rsop, String rg_rnip)
			throws Exception;

}

