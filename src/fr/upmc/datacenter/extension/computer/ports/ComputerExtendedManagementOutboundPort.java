package fr.upmc.datacenter.extension.computer.ports;

import fr.upmc.components.ComponentI;
import fr.upmc.components.ports.AbstractOutboundPort;
import fr.upmc.datacenter.controller.Controller;
import fr.upmc.datacenter.dispatcher.interfaces.RequestDispatcherManagementI;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;

/**
* The class <code>ComputerExtendedManagementOutboundPort</code> implements the
* outbound port requiring the interface <code>ComputerExtendedManagementI</code>.
*
* 
* <p>Created on : 2016-2017</p>
* 
* @author	Cédric Ribeiro et Mokrane Kadri
*/
public class ComputerExtendedManagementOutboundPort extends		AbstractOutboundPort
implements	ComputerExtendedManagementI{


	/***
	 * 
	 * @param owner     owner component
	 * @throws Exception
	 */
	public		ComputerExtendedManagementOutboundPort(
			ComponentI owner
			) throws Exception
	{
		super(ComputerExtendedManagementI.class, owner) ;

		assert	owner != null;
	}

	/***
	 * 
	 * @param uri          uri of the component
	 * @param owner
	 * @throws Exception   owner component
	 */
	public				ComputerExtendedManagementOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
	{
		super(uri, ComputerExtendedManagementI.class, owner);

		assert	owner != null ;
	}

	/*
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#releaseCore(fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore)
	 */
	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		((ComputerExtendedManagementI)this.connector).releaseCore(ac);
		
	}

	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(int)
	 */
	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		return ((ComputerExtendedManagementI)this.connector).reserveCore(number);
	}

	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(int, int)
	 */
	@Override
	public void reserveCore(int processorNo, int coreNo) {
		((ComputerExtendedManagementI)this.connector).reserveCore(processorNo, coreNo);
		
	}


}
