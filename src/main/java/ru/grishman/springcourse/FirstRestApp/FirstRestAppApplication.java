package ru.grishman.springcourse.FirstRestApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ru.grishman.springcourse.FirstRestApp.Properties.PropertyReader;

import java.io.IOException;

@SpringBootApplication
public class FirstRestAppApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(FirstRestAppApplication.class, args);

		PropertyReader propertyReader = new PropertyReader();
		propertyReader.readerProperties();



	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
