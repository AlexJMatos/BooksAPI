package com.itk.booksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class SpringProjectBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectBooksApplication.class, args);
	}
	
	// http://localhost:8080/swagger-ui.html
	@Bean
	public OpenAPI openAPIConfig() {
		return new OpenAPI().info(apiInfo());
	}
	
	public Info apiInfo() {
		Info info = new Info();
		info.setTitle("Book Management API");
		info.setDescription("An API for books and authors management");
		info.setVersion("1.0");
		Contact contact = new Contact();
		contact.setEmail("info@theksquaregroup.com");
		contact.setName("The Ksquare Group");
		contact.setUrl("https://itk.mx/");
		info.setContact(contact);
		return info;
	}
}
