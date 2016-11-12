package fr.upmc.tests.multijvm;

import java.nio.file.Files;
import java.nio.file.Paths;

import fr.upmc.components.registry.GlobalRegistry;

public class LaunchRegistry {
	public static void	main(String[] args) throws Exception {
		System.out.println(args[0]);
		System.out.println(Files.exists(Paths.get(args[0])));
		try {
			GlobalRegistry.main(args);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
