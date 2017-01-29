package fr.upmc.datacenter.admissioncontroller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>ApplicationRequestInboundPort</code>implements the
 * inbound port offering the interface <code>ApplicationRequestI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 *
 * 
 *     	
 * @author	C�dric Ribeiro et Mokrane Kadri
 */
public class ApplicationRequestInboundPort 
extends		AbstractInboundPort implements ApplicationRequestI
{
	private static final long serialVersionUID = 1L;

	
	
	/**
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	owner != null && owner instanceof AdmissionController
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri			uri of the port.
	 * @param owner			owner component.
	 * @throws Exception
	 */
	public		ApplicationRequestInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ApplicationRequestI.class, owner) ;

		assert	owner != null && owner instanceof AdmissionController ;
	}

	

	/**
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	owner != null && owner instanceof AdmissionController
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri			uri of the port.
	 * @param owner			owner component.
	 * @throws Exception
	 */
	public				ApplicationRequestInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ApplicationRequestI.class, owner);

		assert	owner != null && owner instanceof AdmissionController ;
	}
	
   
	/* 
	 * @see fr.upmc.datacenter.admissioncontroller.interfaces.ApplicationRequestI#acceptApplication(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI, String rg_rsop, String rg_rnip)
			throws Exception {
		final AdmissionController c = (AdmissionController) this.owner;
		return c.acceptApplication(application,requestGeneratorURI,rg_rsop,rg_rnip);
	}


}
