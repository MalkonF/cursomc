package me.malkon.cursomc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	/*
	 * método run executa antes de
	 * SpringApplication.run(CursomcApplication.class,args). Para usá-lo devemos
	 * implementar CommandLineRunner na classe
	 */
	@Override
	public void run(String... args) throws Exception {

	}
}
