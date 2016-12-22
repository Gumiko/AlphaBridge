package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>ApplicationRequestOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class ApplicationRequestOutboundPort extends AbstractOutboundPort implements ApplicationRequestI{
	public		ApplicationRequestOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ApplicationRequestI.class, owner) ;

		assert	owner != null && owner instanceof AdmissionController ;
	}

	public				ApplicationRequestOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ApplicationRequestI.class, owner);

		assert	owner != null && owner instanceof AdmissionController ;
	}

	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI) throws Exception {
		return ((ApplicationRequestI)this.connector).acceptApplication(application, requestGeneratorURI) ;
	}
	/**
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI#acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip) throws Exception {
		return ((ApplicationRequestI)this.connector).acceptApplication(application, requestGeneratorURI, rg_rsop, rg_rnip);
	}
}
