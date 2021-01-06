package com.pima.crowdlending.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pima.crowdlending.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
	//this fonction can lend materials
	@Query(value = "select * from material m WHERE m.status = 0 AND owner_id LIKE ?1 ", nativeQuery = true)
	public List<Material> findLandedMaterialsByUser(int userID);
	
	@Query(value = "select * from material m WHERE owner_id LIKE ?1 ", nativeQuery = true)
	public List<Material> findUserMaterials(int userID);
	
	// ancien cmd :
	//+ "select * from material m,demande WHERE m.status = 0 AND owner_id LIKE ?1 AND (select demande.status = 1 where demande.material_id = m.id) "
	@Query(value = "select * from material m INNER JOIN demande ON demande.material_id = m.id AND demande.status = 1  WHERE m.status = 0 AND owner_id LIKE ?1", nativeQuery = true)
	public List<Material> findLandedConfirmedMaterials(int materialID );
	
	@Query(value = "select * from material m WHERE m.title LIKE %?1%"
			+ " Or m.description LIKE %?1%", nativeQuery = true)
	public List<Material> findByFilter(String filter);
	
	@Query(value = "select * from material m WHERE m.status = 1 AND m.validate_add_material = 1 ", nativeQuery = true)
	public List<Material> findValidatedDispo(); //selctionner toutes les materiaux disponibble et valider par ladmin
	
	
	
	@Query(value = "select * from material m WHERE m.validate_add_material = 0 ", nativeQuery = true)
	public List<Material> findUnvalidatedMaterials();
	
	@Query(value = "select * from material m WHERE m.validate_add_material = 0 AND m.title LIKE %?1%"
			+ " Or m.validate_add_material = 0 AND m.description LIKE %?1%", nativeQuery = true)
	public List<Material> findUnvalidatedByNameOrDesc(String filter);
}
