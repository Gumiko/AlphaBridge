package fr.upmc.datacenter.extension.vm;

import java.util.Comparator;
import java.util.Map;

import fr.upmc.datacenter.hardware.processors.Processor.ProcessorPortTypes;
/**
 * The class <code>VMData</code> represente the data format
 *  that is beeing exchanged throw data pull and pushes beweeen component in the ring 

 * <p><strong>Description</strong></p>
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public class VMData implements Comparator<VMData>,Comparable<VMData>{
	Map<String, Map<ProcessorPortTypes, String>> proc;
	String VMUri;
	String VMIntrospection;
	String VMManagement;
	String VMEManagement;
	String VMRequestSubmission;
	private int nbCore;
	
	
	/***
	 * 
	 * @param nbCore          number of cores
	 * @param VMUri           uri of the VM
	 * @param proc            processors list
	 * @param VMM             
	 * @param VMEM
	 * @param RequestSubmission  
	 * @throws Exception
	 */
	public VMData(int nbCore, String VMUri,Map<String, Map<ProcessorPortTypes, String>> proc, String VMM, String VMEM,String RequestSubmission) throws Exception{
		this.nbCore=nbCore;
		this.VMUri=VMUri;
		this.proc=proc;
		this.VMManagement=VMM;
		this.VMEManagement=VMEM;
		this.VMRequestSubmission=RequestSubmission;
	}
	
	
	public int getNbCore() {
		return nbCore;
	}

	public String getVMRequestSubmission() {
		return VMRequestSubmission;
	}


	public void setVMRequestSubmission(String vMRequestSubmission) {
		VMRequestSubmission = vMRequestSubmission;
	}

	public Map<String, Map<ProcessorPortTypes, String>> getProc() {
		return proc;
	}


	public void setProc(Map<String, Map<ProcessorPortTypes, String>> proc) {
		this.proc = proc;
	}


	public String getVMUri() {
		return VMUri;
	}

	public void setVMUri(String vMUri) {
		VMUri = vMUri;
	}
    

	public String getVMIntrospection() {
		return VMIntrospection;
	}
   
	public void setVMIntrospection(String vMIntrospection) {
		VMIntrospection = vMIntrospection;
	}
   
	public String getVMManagement() {
		return VMManagement;
	}

	public void setVMManagement(String vMManagement) {
		VMManagement = vMManagement;
	}


	public String getVMEManagement() {
		return VMEManagement;
	}


	public void setVMEManagement(String vMEManagement) {
		VMEManagement = vMEManagement;
	}

	@Override
	public int compare(VMData o1, VMData o2) {
		return o1.nbCore < o2.nbCore ? -1 : o1.nbCore == o2.nbCore ?  0 : 1;
	}


	@Override
	public int compareTo(VMData o) {
		return this.nbCore < o.nbCore ? -1 : this.nbCore == o.nbCore ?  0 : 1;
	}
}
