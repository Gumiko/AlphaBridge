package fr.upmc.datacenter.admissioncontroller.factory;

import java.util.HashMap;
import java.util.Map;

import fr.upmc.components.cvm.AbstractCVM;
import fr.upmc.datacenter.software.applicationvm.ApplicationVM;
/**
 * The class <code>VMFactory</code>defines the methods
 * to create new VM.
 *
 * <p><strong>Description</strong></p>
 * 
 * 
 * @author	<a href="#">C�dric Ribeiro & Mokrane Kadri</a>
 * @version	$Name$ -- $Revision$ -- $Date$
 */
public class VMFactory{
	public static final String URI_PREFIX = "vm_";
	public static final String INBOUND_URI_PREFIX = URI_PREFIX+"RequestSubmissionInboundPortURI_";
	public static final String OUTBOUND_URI_PREFIX = URI_PREFIX+"RequestNotificationOutboundPortURI_";

	protected static int counter = 0;
	/**
	 * add a computer which the controller will use to create new vm
	 * @param number			number of vm wanted.
	 * @param inboundPortURI	inboundPortURI.
	 * @return Map containing each vm with the associated id
	 * @throws Exception
	 */
	public static Map<Integer, ApplicationVM> createVMs(int number, String inboundPortURI) throws Exception {
		Map<Integer, ApplicationVM> virtualMs = new HashMap<>();
		for(int i = 0; i < number; i++) {

			ApplicationVM virtualM = new ApplicationVM(
					URI_PREFIX + counter,
					inboundPortURI + counter,
					INBOUND_URI_PREFIX + counter,
					OUTBOUND_URI_PREFIX + counter);

			virtualM.toggleTracing() ;
			virtualM.toggleLogging() ;

			AbstractCVM.theCVM.addDeployedComponent(virtualM);

			virtualMs.put(counter++, virtualM);
		}
		return virtualMs;
	}	
}

