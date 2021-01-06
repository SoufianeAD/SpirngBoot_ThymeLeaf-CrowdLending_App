package com.pima.crowdlending.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pima.crowdlending.entities.Admin;
//admin repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	@Query(value = "select * from admin u WHERE u.email LIKE %?1%"
			+ " And u.password LIKE %?2%", nativeQuery = true)
	public Admin findByEmailAndPasswaord(String email, String password);
}
