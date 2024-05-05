package com.project.pod.userRepository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.pod.userEntity.users;

@Repository

public interface userRepository extends JpaRepository<users,Long>{

	@Query("select u from users u where u.verificationCode=?1")
	public users findUserByVerificationCode(String code);
	
	@Query("select u from users u where u.email=?1")
	public users findByEmail(String email);
	
	@Query("select u from users u where u.email=?1 and u.enable=1")
	public users findUser(String email);
	
	
	@Modifying
	@Transactional
	@Query("update users u set u.verificationCode=?1 where u.email=?2 and u.enable=1")
	public void updateByEmail(String code,String email);
	
	
	@Modifying
	@Transactional
	@Query("update users u set u.password=?1 where u.email=?2 and u.enable=1")
	public void updateByPassword(String password,String email);
	
	@Modifying
	@Transactional
	@Query("update users u set u.firstName=?2,u.surName=?3,u.password=?4,u.phoneNumber=?5 where u.email=?1")
	public void updateUser(String a,String b,String c,String d,String e);
	
	
	
	
	
	
	
	
	
	
}
