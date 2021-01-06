package com.pima.crowdlending;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pima.crowdlending.entities.Admin;
import com.pima.crowdlending.repositories.AdminRepository;

@SpringBootApplication
public class PimaCrowdlendingApplication implements CommandLineRunner {
	
	@Autowired
	private AdminRepository adminRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(PimaCrowdlendingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (adminRepository.findAll().size() == 0) {
			Admin admin = new Admin("root", "root", "root", "root", "000000000", "root@crowdlending.com");
			adminRepository.save(admin);
		}
	}
	

}
