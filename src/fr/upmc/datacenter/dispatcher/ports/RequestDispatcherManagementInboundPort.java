package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
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

			assert	owner != null && owner instanceof RequestDispatcher ;
		}

		public				RequestDispatcherManagementInboundPort(
				String uri,
				ComponentI owner
				) throws Exception
		{
			super(uri, RequestDispatcherManagementI.class, owner);

			assert	owner != null && owner instanceof RequestDispatcher ;
		}
		/**
		 * @throws Exception 
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#deployVM(int rd, String RequestDispatcherURIDVM)
		 */
		@Override
		public void bindVM(int id, String requestSubmissionOutboundPortURI, String applicationVMManagementOutboundPortURI) throws Exception {
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			c.bindVM(id,requestSubmissionOutboundPortURI, applicationVMManagementOutboundPortURI);
			
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.RequestDispatcherManagementI#unbindVM(String uriComputerParent, String vm)
		 */
		@Override
		public void unbindVM(int id) throws Exception {
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			c.unbindVM(id);
			
		}
	}
