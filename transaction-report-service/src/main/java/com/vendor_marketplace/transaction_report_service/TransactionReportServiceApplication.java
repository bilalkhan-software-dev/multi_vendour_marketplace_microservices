package com.vendor_marketplace.transaction_report_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TransactionReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionReportServiceApplication.class, args);
	}

}
