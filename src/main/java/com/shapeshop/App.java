package com.shapeshop;

import com.shapeshop.entity.CompanyEntity;
import com.shapeshop.entity.OrderEntity;
import com.shapeshop.repository.CompanyRepository;
import com.shapeshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
public class App {

	@Autowired
	CompanyRepository cRes;

	public static void main(String[] args) {
		System.out.println("<<<<<>>>>> XXXXXXXXXXXXX   !!!!!!!!!!!!!!!!!! STARTING SPRING BOOT !!!!!!!!!!!!!!!!!!!!!!!   XXXXXXXXXX      <<<<<>>>>> ");
		SpringApplication.run(App.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("<<<<<>>>>> XXXXXXXXXXXXX   !!!!!!!!!!!!!!!!!! STARTING SHAPE SHOP !!!!!!!!!!!!!!!!!!!!!!!   XXXXXXXXXX      <<<<<>>>>> ");
		};
	}

	@Bean
	public CommandLineRunner testDBConnectionWorks() {
		return (args) -> {

			CompanyEntity result = cRes.findByName("higgins");

			System.out.println("CompanyEntity ! " + (result != null ? result.toString() : " no higgins"));

			if (result != null) {
				System.out.println("\t\t\t _________.__                             _________.__");
				System.out.println("\t\t\t/   _____/|  |__ _____  ______   ____    /   _____/|  |__   ____ ______");
				System.out.println(" \t\t\t\\_____  \\ |  |  \\\\__  \\ \\____ \\_/ __ \\   \\_____  \\ |  |  \\ /  _ \\\\____ \\");
				System.out.println(" \t\t\t/        \\|   Y  \\/ __ \\|  |_> >  ___/   /        \\|   Y  (  <_> )  |_> >");
				System.out.println("\t\t   /_______  /|___|  (____  /   __/ \\___  > /_______  /|___|  /\\____/|   __/");
				System.out.println("\t\t\t       \\/      \\/     \\/|__|        \\/          \\/      \\/       |__|");
				System.out.println(":: Shape Shop :: v.1.0 (Copyright Oliver Watkins)");
				System.out.println("");
				System.out.println("");
				System.out.println("Test if reaching the app over controller : http://localhost:8080/test ");
				System.out.println("Test products for one of the companies : http://localhost:8080/alpenhof/products ");

			}else {
				System.err.println("No company data found for higgins");
			}
		};
	}
}
