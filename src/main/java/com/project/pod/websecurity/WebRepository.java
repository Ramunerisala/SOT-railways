package com.project.pod.websecurity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WebRepository extends JpaRepository<User,Long>{
      
	@Query("select u from User u where u.username=?1")
	public User findByUsername(String username);
	
	@Modifying
	@Transactional
	@Query("update User u set u.password=?2 where u.username=?1")
	public void updatePass(String a,String b);
}
