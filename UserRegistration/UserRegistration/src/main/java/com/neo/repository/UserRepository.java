package com.neo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByFirstName(String firstName);

	User findBySurname(String surname);

	User findByPinCode(String pinCode);

	User findByUserId(int userId);

	User findByOrderByDob();

	List<User> findByOrderByDojAsc();

	List<User> findByOrderByDobAsc();

}
