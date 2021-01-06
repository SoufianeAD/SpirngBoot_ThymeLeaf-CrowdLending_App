package com.pima.crowdlending.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pima.crowdlending.entities.Demande;

public interface DemandeRepository extends JpaRepository<Demande, Integer> {
	@Query(value = "select * from demande  WHERE status=0 GROUP BY material_id", nativeQuery = true)
	public List<Demande> findUnvalidatedDemandes();
	
	@Query(value = "select * from demande WHERE status=1 GROUP BY material_id", nativeQuery = true)
	public List<Demande> findValidatedDemandes();
	
	@Query(value = "select * from demande WHERE lender_id=?1", nativeQuery = true)
	public List<Demande> findDemandesByUser(int idUser);
	
	@Query(value = "select * from demande WHERE lender_id=?1 and status=1", nativeQuery = true)
	public List<Demande> findlandsByUser(int idUser);
	
	@Query(value = "select * from demande WHERE lender_id=?1 and status=0", nativeQuery = true)
	public List<Demande> findPendingRequestsByUser(int idUser);
	
	@Query(value = "SELECT count(id) FROM demande a where a.date_prevu_retour like '%-' ?1 '-%'", nativeQuery = true)
	public int countDemands(String month);
	
	@Query(value = "SELECT count(id) FROM demande a where a.date_prevu_retour like '%-' ?1 '-%' and lender_id=?2", nativeQuery = true)
	public int countDemands(String month, int userId);
}
