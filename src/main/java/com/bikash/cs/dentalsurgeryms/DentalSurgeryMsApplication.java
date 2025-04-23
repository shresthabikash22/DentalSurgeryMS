package com.bikash.cs.dentalsurgeryms;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.UserRequestDto;
import com.bikash.cs.dentalsurgeryms.service.AddressService;
import com.bikash.cs.dentalsurgeryms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class DentalSurgeryMsApplication {

	private final UserService userService;
	private final AddressService addressService;

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


			List<AddressRequestDto> addresses = List.of(
					new AddressRequestDto("123 Main St", "New York", "NY", "10001"),
					new AddressRequestDto("456 Oak Avenue", "Los Angeles", "CA", "90001"),
					new AddressRequestDto("789 Pine Road", "Chicago", "IL", "60601"),
					new AddressRequestDto("321 Maple Drive", "Houston", "TX", "77001"),
					new AddressRequestDto("654 Elm Street", "Phoenix", "AZ", "85001"),
					new AddressRequestDto("987 Cedar Lane", "Philadelphia", "PA", "19101"),
					new AddressRequestDto("111 Birch Way", "San Antonio", "TX", "78201"),
					new AddressRequestDto("222 Spruce Blvd", "San Diego", "CA", "92101"),
					new AddressRequestDto("333 Walnut Circle", "Dallas", "TX", "75201"),
					new AddressRequestDto("444 Chestnut Court", "San Jose", "CA", "95101"),
					new AddressRequestDto("555 Poplar Avenue", "Austin", "TX", "73301"),
					new AddressRequestDto("666 Ash Road", "Jacksonville", "FL", "32099"),
					new AddressRequestDto("777 Fir Street", "Fort Worth", "TX", "76101"),
					new AddressRequestDto("888 Palm Lane", "Columbus", "OH", "43004"),
					new AddressRequestDto("999 Cypress Street", "Charlotte", "NC", "28201")
			);


			addresses.forEach(addressService::createAddress);

		};

	}

}
