package fr.upmc.datacenter.extension.vm;

import java.util.Map;

import fr.upmc.components.ComponentI;
import fr.upmc.datacenter.hardware.processors.Processor.ProcessorPortTypes;
import fr.upmc.datacenter.hardware.processors.ports.ProcessorManagementOutboundPort;

public class VMData {

	int nbCore;
	Map<String, Map<ProcessorPortTypes, String>> proc;
	String VMUri;
	String VMIntrospection;
	String VMManagement;
	String VMEManagement;
	String VMRequestSubmission;
	
	public VMData(int nbCore, Map<String, Map<ProcessorPortTypes, String>> proc, String VMM, String VMEM,String RequestSubmission) throws Exception{
		this.nbCore=nbCore;
		this.proc=proc;
		this.VMManagement=VMM;
		this.VMEManagement=VMEM;
		this.VMRequestSubmission=RequestSubmission;
	}
	
	public String getVMRequestSubmission() {
		return VMRequestSubmission;
	}

	public void setVMRequestSubmission(String vMRequestSubmission) {
		VMRequestSubmission = vMRequestSubmission;
	}

	public VMData(int nbCore, Map<String, Map<ProcessorPortTypes, String>> proc, String VMM, String VMEM) throws Exception{
		this.nbCore=nbCore;
		this.proc=proc;
		this.VMManagement=VMM;
		this.VMEManagement=VMEM;
	}

	public int getNbCore() {
		return nbCore;
	}

	public void setNbCore(int nbCore) {
		this.nbCore = nbCore;
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
	
	
}
