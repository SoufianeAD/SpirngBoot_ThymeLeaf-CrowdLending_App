package com.pima.crowdlending.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pima.crowdlending.entities.Material;
import com.pima.crowdlending.entities.User;
import com.sun.el.stream.Optional;

//this is the repository of user
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value = "select * from user u WHERE u.first_name LIKE %?1%"
			+ " Or u.last_name LIKE %?1%", nativeQuery = true)
	public List<User> findByFilter(String filter);
	
	@Query(value = "select * from user m WHERE m.email  LIKE ?1", nativeQuery = true)
	public User findByEmail(String filter);
	
	@Query(value = "select * from user u WHERE u.email LIKE %?1%"
			+ " And u.password LIKE %?2%", nativeQuery = true)
	public User findByEmailAndPasswaord(String email, String password);
}
