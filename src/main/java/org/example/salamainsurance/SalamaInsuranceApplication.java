package org.example.salamainsurance;

import org.example.salamainsurance.Entity.IndemnitySarra;
import org.example.salamainsurance.Service.IndemnitySarraService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalamaInsuranceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SalamaInsuranceApplication.class, args);
  }}

  /*@Bean
  CommandLineRunner testInsuranceLogic(IndemnitySarraService service) {
    return args -> {
      System.out.println("\n---    STARTING ADVANCED BUSINESS LOGIC TEST ---");


      IndemnitySarra testIndemnity = new IndemnitySarra();
      testIndemnity.setGrossAmount(20000.0);
      testIndemnity.setResponsibility(100); //
      testIndemnity.setDeductibleValue(500.0);


      IndemnitySarra saved = service.saveInitialData(20000.0, 100, 500.0);
      Long id = saved.getIdIndemnity();

      System.out.println("Step 1: Expert data saved (Gross: 20000 DT, Resp: 100%)");


      double marketValue = 40000.0;
      double insuredValue = 30000.0;

      System.out.println("Step 2: Running Advanced Payout Engine (Art. 33)...");
      IndemnitySarra finalResult = service.calculateAdvancedPayout(id, marketValue, insuredValue);


      System.out.println("\n---  TEST RESULTS ---");
      System.out.println("Indemnity ID: " + finalResult.getIdIndemnity());
      System.out.println("Final Net Amount: " + finalResult.getNetAmount() + " TND");
      System.out.println("Status: " + finalResult.getStatus());
      System.out.println("Calculation Date: " + finalResult.getCalculationDate());


      System.out.println("-----------------------------------------------\n");
    };
  }
}*/