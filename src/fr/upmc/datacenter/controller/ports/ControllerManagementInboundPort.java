package fr.upmc.datacenter.controller.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.controller.interfaces.ControllerManagementI;
import fr.upmc.datacenter.hardware.computers.ports.ComputerServicesOutboundPort;
/**
 * The class <code>ControllerManagementInboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">Cédric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class ControllerManagementInboundPort
	extends		AbstractInboundPort implements ControllerManagementI
	{
		private static final long serialVersionUID = 1L;

		public		ControllerManagementInboundPort(
				ComponentI owner
				) throws Exception
		{
			super(ControllerManagementI.class, owner) ;

			assert	owner != null && owner instanceof Controller ;
		}

		public				ControllerManagementInboundPort(
				String uri,
				ComponentI owner
				) throws Exception
		{
			super(uri, ControllerManagementI.class, owner);

			assert	owner != null && owner instanceof Controller ;
		}
		/**
		 * @see fr.upmc.datacenter.controller.interfaces.ControllerManagementI#linkComputer(ComputerServicesOutboundPort c_out) 
		 */
		@Override
		public void linkComputer(ComputerServicesOutboundPort c_out) throws Exception {
			final Controller c = (Controller) this.owner;
			c.linkComputer(c_out);
		}
}
