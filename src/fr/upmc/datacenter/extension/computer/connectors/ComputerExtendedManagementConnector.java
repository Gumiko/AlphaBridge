package fr.upmc.datacenter.extension.computer.connectors;

import fr.upmc.components.connectors.AbstractConnector;
import fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI;
import fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore;
/**
 * The class <code>ComputerExtendedManagementConnector</code>implements a
 * connector for ports exchanging through the interface
 * <code>ComputerExtendedManagementI</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
/**
 * @author Mokrane
 *
 */
/**
 * @author Mokrane
 *
 */
/**
 * @author Mokrane
 *
 */
/**
 * @author Mokrane
 *
 */
public class ComputerExtendedManagementConnector 	extends		AbstractConnector implements ComputerExtendedManagementI{

	
	
	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#releaseCore(fr.upmc.datacenter.hardware.computers.Computer.AllocatedCore)
	 */
	@Override
	public void releaseCore(AllocatedCore ac) throws Exception {
		((ComputerExtendedManagementI)this.offering).releaseCore(ac);
		
	}

	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(Integer)
	 */
	@Override
	public AllocatedCore[] reserveCore(int number) throws Exception {
		return ((ComputerExtendedManagementI)this.offering).reserveCore(number);
	}

	/* 
	 * @see fr.upmc.datacenter.extension.computer.interfaces.ComputerExtendedManagementI#reserveCore(Integer,Integer)
	 */
	@Override
	public void reserveCore(int processorNo, int coreNo) {
		((ComputerExtendedManagementI)this.offering).reserveCore(processorNo, coreNo);
		
	}

}
