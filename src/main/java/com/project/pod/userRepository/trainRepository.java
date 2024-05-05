package com.project.pod.userRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.pod.userEntity.Train;

@Repository
public interface trainRepository extends JpaRepository<Train,Long> {

	@Query("select t.fromStation from Train t where t.enable=1")
	public List<String> departureStations();
	
	@Query("select t.toStation from Train t where t.enable=1")
	public List<String> arrivalStations();
	
	@Query("select t from Train t where t.name=?1 and t.fromStation=?2 and t.toStation=?3")
	public Train findByTDA(String name,String a,String b);
	
	
}
