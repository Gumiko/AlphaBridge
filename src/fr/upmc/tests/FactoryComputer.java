package fr.upmc.tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.upmc.datacenter.hardware.computers.Computer;


/**
 * The class <code>FactoryComputer</code> 
 *
 *
 * <p><strong>Description</strong></p>
 * 
 * A  factory in charge of creating computer components

 * @author	Cédric Ribeiro et Mokrane Kadri
 *
 */
public class FactoryComputer {

	/**
	 * 
	 * @param computerURI                             URI of the computer newly created computer.
	 * @param numberOfProcessors                      the number of processor of the new computer.
	 * @param numberOfCores                           the number of cores per processor
	 * @param ComputerServicesInboundPortURI          URI of the VM management inbound port.
	 * @param ComputerStaticStateDataInboundPortURI   URI of the computer's static state data inbound port.
	 * @param ComputerDynamicStateDataInboundPortURI  URI of the computer's dynamic state data inbound port.
	 * @return                                        the newly created computer
	 * @throws Exception
	 */
	public static Computer createComputer(String computerURI,int numberOfProcessors,int numberOfCores,String ComputerServicesInboundPortURI,String ComputerStaticStateDataInboundPortURI,String ComputerDynamicStateDataInboundPortURI) throws Exception{
		Set<Integer> admissibleFrequencies = new HashSet<Integer>() ;
		admissibleFrequencies.add(1500) ;	// Cores can run at 1,5 GHz
		admissibleFrequencies.add(3000) ;	// and at 3 GHz
		Map<Integer,Integer> processingPower = new HashMap<Integer,Integer>() ;
		processingPower.put(1500, 1500000) ;	// 1,5 GHz executes 1,5 Mips
		processingPower.put(3000, 3000000) ;	// 3 GHz executes 3 Mips
		Computer c = new Computer(
				computerURI,
				admissibleFrequencies,
				processingPower,  
				1500,		// Test scenario 1, frequency = 1,5 GHz
				// 3000,	// Test scenario 2, frequency = 3 GHz
				1500,		// max frequency gap within a processor
				numberOfProcessors,
				numberOfCores,
				ComputerServicesInboundPortURI,
				ComputerStaticStateDataInboundPortURI,
				ComputerDynamicStateDataInboundPortURI) ;
		
		return c;
	}
}
