package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ApplicationRequestI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>ApplicationRequestInboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class ApplicationRequestInboundPort 
extends		AbstractInboundPort implements ApplicationRequestI
{
	private static final long serialVersionUID = 1L;

	public		ApplicationRequestInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ApplicationRequestI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				ApplicationRequestInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ApplicationRequestI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}

	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI) throws Exception {
		final Controller c = (Controller) this.owner;
		return c.acceptApplication(application, requestGeneratorURI);
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.ApplicationRequestI#acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip)
	 */
	@Override
	public boolean acceptApplication(Integer application, String requestGeneratorURI,
			RequestSubmissionOutboundPort rg_rsop, RequestNotificationInboundPort rg_rnip) throws Exception {
		final Controller c = (Controller) this.owner;
		return c.acceptApplication(application,requestGeneratorURI,rg_rsop,rg_rnip);
	}


}
