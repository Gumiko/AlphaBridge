package fr.upmc.datacenter.admissioncontroller.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>ApplicationRequestConnector</code>implements a
 * connector for ports exchanging through the interface
 * <code>ApplicationRequestI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
@SuppressWarnings("unused")
public class ApplicationRequestConnector
extends		AbstractConnector
implements	ApplicationRequestI
{
	
	
	/* 
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI#acceptApplication(Integer, String, String,String)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI, String rg_rsop, String rg_rnip)
			throws Exception {
		return ((ApplicationRequestI)this.offering).acceptApplication(application, requestGeneratorURI, rg_rsop, rg_rnip);
	}
}

