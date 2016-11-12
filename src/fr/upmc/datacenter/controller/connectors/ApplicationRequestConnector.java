package fr.upmc.datacenter.controller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.controller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>ApplicationRequestConnector</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
@SuppressWarnings("unused")
public class ApplicationRequestConnector
extends		AbstractConnector
implements	ApplicationRequestI
{
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI) throws Exception {
		return ((ApplicationRequestI)this.offering).acceptApplication(application, requestGeneratorURI);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ApplicationRequestI#acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip) throws Exception {
		return ((ApplicationRequestI)this.offering).acceptApplication(application, requestGeneratorURI, rg_rsop, rg_rnip);
	}
}

