package fr.upmc.tests.multijvm;

import fr.upmc.components.cvm.AbstractDistributedCVM;

public class TestDistributedCVM extends AbstractDistributedCVM{

	public TestDistributedCVM(String[] args) throws Exception {
		super(args);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void			instantiateAndPublish()
	throws Exception
	{	
		
		super.instantiateAndPublish();
	}
	
	
	@Override
	public void			interconnect() throws Exception
	{
		assert	this.instantiationAndPublicationDone ;

		super.interconnect();
	}
	
	@Override
	public void			start() throws Exception
	{
		super.start() ;

	}
	
	@Override
	public void			shutdown() throws Exception
	{

		super.shutdown();
	}
	
	@Override
	public void			shutdownNow() throws Exception
	{
	
		super.shutdownNow();
	}
	
	public static void	main(String[] args)
	{
		System.out.println("Beginning") ;
		try {
			TestDistributedCVM da = new TestDistributedCVM(args) ;
			da.deploy() ;
			System.out.println("starting...") ;
			da.start() ;
			Thread.sleep(5000L) ;
			System.out.println("shutting down...") ;
			da.shutdown() ;
			System.out.println("ending...") ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		System.out.println("Main thread ending") ;
		System.exit(0);
	}

}
