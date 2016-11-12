package fr.upmc.datacenter.controller.interfaces;

import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The interface <code>ApplicationRequestI</code>defines the methods
 * to manage an admission of a request generator to the controller.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public interface ApplicationRequestI {
	public boolean acceptApplication(Integer application,
			String requestGeneratorURI) throws Exception;

	/**
	 * Send a request generator to the controller
	 * @param application			id of the application.
	 * @param requestGeneratorURI			uri of the request generator.
	 * @param rg_rsop			request submission outbound port of the request generator
	 * @param rg_rnip			request notification outbound port of the request generator
	 * @return boolean
	 * @throws Exception
	 */
	public boolean acceptApplication(Integer application, String requestGeneratorURI, RequestSubmissionOutboundPort rg_rsop,
			RequestNotificationInboundPort rg_rnip) throws Exception;

}

