package fr.upmc.datacenter.dispatcher.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.dispatcher.RequestDispatcher;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
* The class <code>RequestDispatcherManagementInboundPort</code> implements the
 * inbound port through which the component management methods are called.
* 
* <p>Created on : 2016-2017</p>
* 
* @author	Cédric Ribeiro et Mokrane Kadri
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
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#bindVM(String,String,String,String)
		 */
		@Override
		public void bindVM(String vmUri, String requestSubmissionInboundPortURI, String applicationVMManagementInboundPortURI,String VMExtendedManagementInboundPortURI) throws Exception {
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			c.bindVM(vmUri,requestSubmissionInboundPortURI, applicationVMManagementInboundPortURI,VMExtendedManagementInboundPortURI);
			
		}
		/**
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#unbindVM(String)
		 */
		@Override
		public void unbindVM(String vmUri) throws Exception {
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			c.unbindVM(vmUri);
			
		}
		
		/**
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeCore(int,String)
		 */
		@Override
		public AllocatedCore[] removeCore(int number,String vmURI) throws Exception{
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			return c.removeCore(number, vmURI);
		}
		/**
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#addCore(int,String)
		 */
		@Override
		public int addCore(int number,String vmURI) throws Exception{
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			return c.addCore(number, vmURI);
		}
		/**
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#removeAll(String)
		 */
		@Override
		public AllocatedCore[] removeAll(String vmURI) throws Exception{
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			return c.removeAll(vmURI);
		}
		/**
		 * @see fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI#resetRequestNumber()
		 */
		@Override
		public void resetRequestNumber() throws Exception{
			final RequestDispatcher c = (RequestDispatcher) this.owner;
			c.resetRequestNumber();
		}
		
		
	}
