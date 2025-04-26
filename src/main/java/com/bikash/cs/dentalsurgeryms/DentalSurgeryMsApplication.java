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
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class DentalSurgeryMsApplication {
	public static void main(String[] args) {

		SpringApplication.run(DentalSurgeryMsApplication.class, args);
	}

}
