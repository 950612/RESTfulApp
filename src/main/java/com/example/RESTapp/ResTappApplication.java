package com.example.RESTapp;

import com.example.RESTapp.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * The main Spring boot class with main method which starts application
 */
@SpringBootApplication
public class ResTappApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(ResTappApplication.class, args);
	}

	@Override
	public void run(String... arg) {
		storageService.deleteAll();
		storageService.init();
	}
}
