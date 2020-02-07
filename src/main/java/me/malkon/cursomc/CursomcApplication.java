package me.malkon.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.malkon.cursomc.services.S3Service;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private S3Service s3Service;

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

		s3Service.uploadFile("/home/anonymous/Imagens/pinguin.jpg");

	}
}// continuar vendo perguntas parei aula 13
