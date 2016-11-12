package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.software.ports.RequestNotificationInboundPort;
import fr.upmc.datacenter.software.ports.RequestSubmissionOutboundPort;
/**
 * The class <code>RequestDispatcherManagementOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
@SuppressWarnings("unused")
public class RequestDispatcherManagementOutboundPort
extends		AbstractOutboundPort
implements	RequestDispatcherManagementI{


	public		RequestDispatcherManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(RequestDispatcherManagementI.class, owner) ;

		assert	owner != null && owner instanceof Controller ;
	}

	public				RequestDispatcherManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, RequestDispatcherManagementI.class, owner);

		assert	owner != null && owner instanceof Controller ;
	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
	 */
	@Override
	public void deployVM(int rd, String RequestDispatcherURIDVM) {
		((RequestDispatcherManagementI)this.connector).deployVM(rd, RequestDispatcherURIDVM);

	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#destroyVM(String uriComputerParent, String vm)
	 */
	@Override
	public void destroyVM(String uriComputerParent, String vm) {
		((RequestDispatcherManagementI)this.connector).destroyVM(uriComputerParent, vm);

	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#initVM(int application, String uriComputerParent, String vm)
	 */
	@Override
	public void initVM(int application, String uriComputerParent, String vm) {
		((RequestDispatcherManagementI)this.connector).initVM(application, uriComputerParent, vm);

	}
	/**
	 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
	 */
	@Override
	public void unbindVM(String uriComputerParent, String vm) throws Exception {
		((RequestDispatcherManagementI)this.connector).unbindVM(uriComputerParent, vm);

	}
}
