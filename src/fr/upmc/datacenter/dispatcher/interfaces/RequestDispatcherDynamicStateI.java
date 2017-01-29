package fr.upmc.datacenter.dispatcher.interfaces;

import java.util.ArrayList;

import fr.upmc.components.interfaces.DataOfferedI;
import fr.upmc.components.interfaces.DataRequiredI;
import fr.upmc.datacenter.extension.vm.VMData;
import fr.upmc.datacenter.interfaces.TimeStampingI;
/**
 * 
 * 
 * @author	Cédric Ribeiro et Mokrane Kadri
 */
public interface RequestDispatcherDynamicStateI extends 	DataOfferedI.DataI,
DataRequiredI.DataI,
TimeStampingI{

	/**
	 * 
	 * @return the Average time of request termination of the request dispatcher
	 */
	long getAverageTime();

	/***
	 * 
	 * @return list of informations about the Virtual Machines used by the request dispatcher
	 */
	ArrayList<VMData> getVMDatas();

	/***
	 * 
	 * @return the number of requests
	 */
	int getNbreq();

}
