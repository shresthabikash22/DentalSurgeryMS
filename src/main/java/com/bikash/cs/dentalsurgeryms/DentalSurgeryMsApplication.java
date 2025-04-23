package com.bikash.cs.dentalsurgeryms;

import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor

public class DentalSurgeryMsApplication {

	private final UserService userService;

	public static void main(String[] args) {

		SpringApplication.run(DentalSurgeryMsApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			// List of users to insert into the database
			List<UserRequestDto> users = List.of(
					new UserRequestDto("jameson", "ihdsiadu", List.of("ROLE_DENTIST")),
					new UserRequestDto("johnson", "qwerty123", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
					new UserRequestDto("alexis", "password321", List.of("ROLE_PATIENT")),
					new UserRequestDto("marissa", "admin2021", List.of("ROLE_MANAGER")),
					new UserRequestDto("peter", "securepass", List.of("ROLE_DENTIST", "ROLE_PATIENT")),
					new UserRequestDto("samantha", "s4m4nt@123", List.of("ROLE_DENTIST")),
					new UserRequestDto("andrew", "andrew@123", List.of("ROLE_PATIENT")),
					new UserRequestDto("victor", "v1ct0rpass", List.of("ROLE_MANAGER")),
					new UserRequestDto("ellias", "ell@2023", List.of("ROLE_MANAGER")),
					new UserRequestDto("paulo", "paulsp@ss123", List.of("ROLE_DENTIST")),
					new UserRequestDto("claire", "claire@321", List.of("ROLE_PATIENT")),
					new UserRequestDto("taylor", "t@ylor789", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
					new UserRequestDto("catherine", "catherine2024", List.of("ROLE_MANAGER")),
					new UserRequestDto("michael", "m!ch@el2025", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
					new UserRequestDto("lucas", "lucas#1234", List.of("ROLE_MANAGER"))
			);


			// Save users to the database if not already existing
					users.forEach(userService::createUser);
		};

	}

}
