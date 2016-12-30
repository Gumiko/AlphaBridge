package fr.upmc.datacenter.extension.computer;

import java.util.Map;
import java.util.Set;

import fr.upmc.datacenter.hardware.computers.Computer;

public class ComputerExtended extends Computer{

	public ComputerExtended(String computerURI, Set<Integer> possibleFrequencies, Map<Integer, Integer> processingPower,
			int defaultFrequency, int maxFrequencyGap, int numberOfProcessors, int numberOfCores,
			String computerServicesInboundPortURI, String computerStaticStateDataInboundPortURI,
			String computerDynamicStateDataInboundPortURI) throws Exception {
		super(computerURI, possibleFrequencies, processingPower, defaultFrequency, maxFrequencyGap, numberOfProcessors,
				numberOfCores, computerServicesInboundPortURI, computerStaticStateDataInboundPortURI,
				computerDynamicStateDataInboundPortURI);
	}

}
