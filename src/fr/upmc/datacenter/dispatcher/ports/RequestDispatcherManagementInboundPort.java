package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.admissioncontroller.AdmissionController;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
/**
 * The class <code>RequestDispatcherManagementInboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class RequestDispatcherManagementInboundPort extends		AbstractInboundPort implements RequestDispatcherManagementI{
		private static final long serialVersionUID = 1L;

		public		RequestDispatcherManagementInboundPort(
				ComponentI owner
				) throws Exception
		{
			super(RequestDispatcherManagementI.class, owner) ;

			assert	owner != null && owner instanceof AdmissionController ;
		}

		public				RequestDispatcherManagementInboundPort(
				String uri,
				ComponentI owner
				) throws Exception
		{
			super(uri, RequestDispatcherManagementI.class, owner);

			assert	owner != null && owner instanceof AdmissionController ;
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
		 */
		@Override
		public void deployVM(int rd, String RequestDispatcherURIDVM) {
			final AdmissionController c = (AdmissionController) this.owner;
			c.deployVM(rd, RequestDispatcherURIDVM);
			
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#destroyVM(String uriComputerParent, String vm)
		 */
		@Override
		public void destroyVM(String uriComputerParent, String vm) {
			final AdmissionController c = (AdmissionController) this.owner;
			c.destroyVM(uriComputerParent, vm);
			
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#initVM(int application, String uriComputerParent, String vm)
		 */
		@Override
		public void initVM(int application, String uriComputerParent, String vm) {
			final AdmissionController c = (AdmissionController) this.owner;
			c.initVM(application, uriComputerParent, vm);
			
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
		 */
		@Override
		public void unbindVM(String uriComputerParent, String vm) throws Exception {
			final AdmissionController c = (AdmissionController) this.owner;
			c.unbindVM(uriComputerParent, vm);
			
		}
	}
