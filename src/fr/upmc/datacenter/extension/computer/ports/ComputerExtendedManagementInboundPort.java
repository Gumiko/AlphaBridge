package fr.upmc.datacenter.extension.computer.ports;

import fr.upmc.components.ComponentI;

import fr.upmc.components.ports.AbstractInboundPort;
import fr.upmc.datacenter.extension.computer.ComputerExtended;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

/**
 * The class <code>ComputerExtendedManagementInboundPort</code> implements the
 * inbound port through which the component management methods are called.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 *  @author	Cédric Ribeiro et Mokrane Kadri
 * @version	$Name$ -- $Revision$ -- $Date$
 */

public class ComputerExtendedManagementInboundPort extends	AbstractInboundPort
implements	ComputerExtendedManagementI{

	private static final long serialVersionUID = 1L;

	/***
	 * 
	 * @param owner          owner component.
	 * @throws Exception
	 */
	public		ComputerExtendedManagementInboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ComputerExtendedManagementI.class, owner) ;

		assert	owner != null && owner instanceof ComputerExtended ;
	}
	/***
	 * @param uri			uri of the port.
	 * @param owner			owner component.
	 * @throws Exception

	 */
	public				ComputerExtendedManagementInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ComputerExtendedManagementI.class, owner);

		assert	owner != null && owner instanceof ComputerExtended ;
	}

	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#releaseCore(fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore)
	 */
	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		final ComputerExtended c = (ComputerExtended) this.owner;
		c.releaseCore(ac);

	}


	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(int)
	 */
	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		final ComputerExtended c = (ComputerExtended) this.owner;
		return c.reserveCore(number);
	}


	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(int, int)
	 */
	@Override
	public void reserveCore(int processorNo, int coreNo) {
		final ComputerExtended c = (ComputerExtended) this.owner;
		c.reserveCore(processorNo,coreNo);

	}


}
