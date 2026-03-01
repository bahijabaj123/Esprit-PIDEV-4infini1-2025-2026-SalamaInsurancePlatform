package org.example.salamainsurance.Repository.Expert;

import org.example.salamainsurance.Entity.Expert.ExpertHassen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertHassenRepository extends JpaRepository<ExpertHassen, Integer> {

    List<ExpertHassen> findByInterventionZone(ExpertHassen.InterventionZone zone);

    List<ExpertHassen> findByStatus(ExpertHassen.Status status);

    List<ExpertHassen> findByCity(String city);

    // Recherche par spécialité
    List<ExpertHassen> findBySpecialtyContainingIgnoreCase(String specialty);

    // Experts avec au moins X années d'expérience
    List<ExpertHassen> findByYearsOfExperienceGreaterThanEqual(Integer minYears);

    // Recherche par nom ou prénom
    @Query("SELECT e FROM ExpertHassen e WHERE LOWER(e.lastName) LIKE LOWER(CONCAT('%',:nom,'%')) OR LOWER(e.firstName) LIKE LOWER(CONCAT('%',:nom,'%'))")
    List<ExpertHassen> searchByName(@Param("nom") String nom);

    // Nombre d'experts par statut
    long countByStatus(ExpertHassen.Status status);

    // Statistiques : nombre d'experts par zone
    @Query("SELECT e.interventionZone, COUNT(e) FROM ExpertHassen e GROUP BY e.interventionZone")
    List<Object[]> countParZone();
}