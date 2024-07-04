package com.literalura.cristian;

import com.literalura.cristian.principal.Principal;
import com.literalura.cristian.repository.AutorRepository;
import com.literalura.cristian.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CristianApplication implements CommandLineRunner {
	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(CristianApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraMenu();
	}
}
