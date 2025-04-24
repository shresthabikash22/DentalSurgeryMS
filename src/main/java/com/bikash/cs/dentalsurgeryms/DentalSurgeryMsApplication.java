package com.bikash.cs.dentalsurgeryms;

import com.bikash.cs.dentalsurgeryms.dto.request.*;
import com.bikash.cs.dentalsurgeryms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class DentalSurgeryMsApplication {

	private final UserService userService;
	private final SurgeryService surgeryService;
	private final PatientService patientService;

	public static void main(String[] args) {

		SpringApplication.run(DentalSurgeryMsApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(DentistService dentistService) {
		return args -> {
			// List of users to insert into the database
//			List<UserRequestDto> users = List.of(
//					new UserRequestDto("jameson", "ihdsiadu", List.of("ROLE_DENTIST")),
//					new UserRequestDto("johnson", "qwerty123", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
//					new UserRequestDto("alexis", "password321", List.of("ROLE_PATIENT")),
//					new UserRequestDto("marissa", "admin2021", List.of("ROLE_MANAGER")),
//					new UserRequestDto("peter", "securepass", List.of("ROLE_DENTIST", "ROLE_PATIENT")),
//					new UserRequestDto("samantha", "s4m4nt@123", List.of("ROLE_DENTIST")),
//					new UserRequestDto("andrew", "andrew@123", List.of("ROLE_PATIENT")),
//					new UserRequestDto("victor", "v1ct0rpass", List.of("ROLE_MANAGER")),
//					new UserRequestDto("ellias", "ell@2023", List.of("ROLE_MANAGER")),
//					new UserRequestDto("paulo", "paulsp@ss123", List.of("ROLE_DENTIST")),
//					new UserRequestDto("claire", "claire@321", List.of("ROLE_PATIENT")),
//					new UserRequestDto("taylor", "t@ylor789", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
//					new UserRequestDto("catherine", "catherine2024", List.of("ROLE_MANAGER")),
//					new UserRequestDto("michael", "m!ch@el2025", List.of("ROLE_DENTIST", "ROLE_MANAGER")),
//					new UserRequestDto("lucas", "lucas#1234", List.of("ROLE_MANAGER"))
//			);


			// Save users to the database if not already existing
//					users.forEach(userService::createUser);


			List<SurgeryRequestDto> surgeries = List.of(
					new SurgeryRequestDto("SC-DT", "Smile Clinic", "+12025550123", new AddressRequestDto("123 Main St", "New York", "NY", "10001")),
					new SurgeryRequestDto("SC-AB", "Healthy Smiles Dental", "+12025550124", new AddressRequestDto("456 Oak Avenue", "Los Angeles", "CA", "90001")),
					new SurgeryRequestDto("DT-MK", "Downtown Dentistry", "+12025550125", new AddressRequestDto("789 Pine Road", "Chicago", "IL", "60601")),
					new SurgeryRequestDto("SC-XY", "Gentle Care Dentistry", "+12025550126", new AddressRequestDto("321 Maple Drive", "Houston", "TX", "77001")),
					new SurgeryRequestDto("SC-BT", "Bright Smiles Dentistry", "+12025550127", new AddressRequestDto("654 Elm Street", "Phoenix", "AZ", "85001")),
					new SurgeryRequestDto("DT-PL", "Pearl Dental Care", "+12025550128", new AddressRequestDto("987 Cedar Lane", "Philadelphia", "PA", "19101")),
					new SurgeryRequestDto("SC-QW", "Quality Dental Clinic", "+12025550129", new AddressRequestDto("111 Birch Way", "San Antonio", "TX", "78201")),
					new SurgeryRequestDto("SC-RT", "Root Canal Specialists", "+12025550130", new AddressRequestDto("222 Spruce Blvd", "San Diego", "CA", "92101")),
					new SurgeryRequestDto("SC-ZY", "Zentel Dental", "+12025550131", new AddressRequestDto("333 Walnut Circle", "Dallas", "TX", "75201")),
					new SurgeryRequestDto("DT-FG", "Family Dentistry Group", "+12025550132", new AddressRequestDto("444 Chestnut Court", "San Jose", "CA", "95101"))
			);

			surgeries.forEach(surgeryService::createSurgery);


			List<PatientRequestDto> patients = List.of(
					new PatientRequestDto(
							"James", "Anderson", "+12025550148", "james.anderson@example.com", LocalDate.of(1980, 6, 5),
							new AddressRequestDto("909 Chestnut Ct", "Dallas", "TX", "75202"),
							new UserRequestDto("jamesa", "jamespass987654", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Michael", "Brown", "+12025550142", "michael.brown@example.com", LocalDate.of(1978, 3, 10),
							new AddressRequestDto("303 Maple Rd", "Chicago", "IL", "60602"),
							new UserRequestDto("michaelb", "password789123", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Emily", "Davis", "+12025550143", "emily.davis@example.com", LocalDate.of(1995, 11, 30),
							new AddressRequestDto("404 Elm Dr", "Houston", "TX", "77002"),
							new UserRequestDto("emilyd", "emilypass321456", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"John", "Doe", "+12025550140", "john.doe@example.com", LocalDate.of(1985, 5, 15),
							new AddressRequestDto("101 Pine St", "New York", "NY", "10002"),
							new UserRequestDto("johndoe", "password12345678", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"William", "Johnson", "+12025550144", "william.johnson@example.com", LocalDate.of(1982, 7, 19),
							new AddressRequestDto("505 Cedar Ln", "Phoenix", "AZ", "85002"),
							new UserRequestDto("williamj", "willpass654321", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"David", "Martinez", "+12025550146", "david.martinez@example.com", LocalDate.of(1975, 9, 25),
							new AddressRequestDto("707 Spruce Blvd", "San Antonio", "TX", "78202"),
							new UserRequestDto("davidm", "davidpass321456", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Jane", "Smith", "+12025550141", "jane.smith@example.com", LocalDate.of(1990, 8, 22),
							new AddressRequestDto("202 Oak Ave", "Los Angeles", "CA", "90002"),
							new UserRequestDto("janesmith", "securepass456789", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Laura", "Taylor", "+12025550147", "laura.taylor@example.com", LocalDate.of(1992, 2, 18),
							new AddressRequestDto("808 Walnut Cir", "San Diego", "CA", "92102"),
							new UserRequestDto("laurat", "laurapass654321", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Emma", "Thomas", "+12025550149", "emma.thomas@example.com", LocalDate.of(1987, 12, 8),
							new AddressRequestDto("1010 Magnolia Pl", "San Jose", "CA", "95102"),
							new UserRequestDto("emmat", "emmapass12345678", List.of("ROLE_PATIENT"))
					),
					new PatientRequestDto(
							"Sarah", "Wilson", "+12025550145", "sarah.wilson@example.com", LocalDate.of(1988, 4, 12),
							new AddressRequestDto("606 Birch Way", "Philadelphia", "PA", "19102"),
							new UserRequestDto("sarahw", "sarahpass987654", List.of("ROLE_PATIENT"))
					)
			);


			patients.forEach(patientService::createPatient);

			List<DentistRequestDto> dentistRequestDtos =List.of(
					new DentistRequestDto(
							"Alice", "Johnson", "+12025550150", "alice.johnson@example.com", "Orthodontics",
							new UserRequestDto("alicej", "dentistpass123456", List.of("ROLE_DENTIST"))
					),
					new DentistRequestDto(
							"Bob", "Smith", "+12025550151", "bob.smith@example.com", "Periodontics",
							new UserRequestDto("bobsmith", "dentistpass789101", List.of("ROLE_DENTIST"))
					),
					new DentistRequestDto(
							"Carol", "Williams", "+12025550152", "carol.williams@example.com", "Endodontics",
							new UserRequestDto("carolw", "dentistpass112131", List.of("ROLE_DENTIST"))
					),
					new DentistRequestDto(
							"David", "Brown", "+12025550153", "david.brown@example.com", "Prosthodontics",
							new UserRequestDto("davidb", "dentistpass415161", List.of("ROLE_DENTIST"))
					),
					new DentistRequestDto(
							"Emma", "Davis", "+12025550154", "emma.davis@example.com", "Pediatric Dentistry",
							new UserRequestDto("emmad", "dentistpass718192", List.of("ROLE_DENTIST"))
					)
			);
			dentistRequestDtos.forEach(dentistService::createDentist);

		};

	}

}
