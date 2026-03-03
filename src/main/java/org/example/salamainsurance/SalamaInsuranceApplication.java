package org.example.salamainsurance;

import org.example.salamainsurance.Service.IndemnitySarraService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SalamaInsuranceApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(SalamaInsuranceApplication.class, args);

    System.out.println("Application Salama Insurance lancée avec succès !");

    // Test du calcul d'indemnité après le démarrage
    testIndemnityCalculation(context);
  }

  private static void testIndemnityCalculation(ConfigurableApplicationContext context) {
    try {
      IndemnitySarraService indemnityService = context.getBean(IndemnitySarraService.class);

      System.out.println("\n" + "=".repeat(40));
      System.out.println("   TEST DU CALCUL D'INDEMNITÉ IA");
      System.out.println("=".repeat(40));

      // Cas d'un client mécontent
      String texteSinistre = "Ma voiture est totalement détruite, je suis très en colère par ce délai !";
      double dommages = 5000.0;
      double franchise = 400.0;

      String resultat = indemnityService.genererFactureSeule(texteSinistre, dommages, franchise);
      System.out.println(resultat);
      System.out.println("=".repeat(40));

    } catch (Exception e) {
      System.err.println("Erreur lors du test : " + e.getMessage());
      e.printStackTrace();
    }
  }
}
