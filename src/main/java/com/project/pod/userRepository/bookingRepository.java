package com.project.pod.userRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.pod.userEntity.bookings;

@Repository
public interface bookingRepository extends JpaRepository<bookings,Long>{

//	@Query("select count(*) from bookings  where trainName=?1 and departure=?2 and arrival=?3 and departureDate=?4")
	@Query("select count(id)from bookings  where trainName=?1 and departure=?2 and arrival=?3 and departureDate=?4")
	public String countBy(String a,String b,String c,String d);
	
	@Query("select u from bookings u where u.email=?1 and u.enable=1")
	public List<bookings> findByEmail(String email);
	
	@Query("select u from bookings u where u.ticketId=?1")
	public bookings findByTID(String ticketId);
	
	@Modifying
	@Transactional
	@Query("update bookings b set b.enable=0 where b.ticketId=?1")
	public void updateEnable(String ticketId);
	
	
	 @Query("select COALESCE(sum(fair),0) from bookings where departure=?1 and DATE(departure_date) between DATE(?2) and DATE(?3)")
	 public String finByStations(String a,String b,String c);
	 
	 @Query("select count(id) from bookings where departure=?1 and paymentType=?2 and DATE(departure_date) between DATE(?3) and DATE(?4)")
	public String finBycard(String a,String b,String c,String d);
	 
	@Query("select count(id) from bookings where departure=?1 and YEAR(departure_date)=YEAR(?2)")
	 public String countPass(String a,String b);
	
	
	@Modifying
	@Transactional
	@Query("update bookings b set b.name=?2,b.trainName=?3,b.departureDate=?4,b.departureTime=?5,b.arrivalTime=?6,b.departure=?7,b.arrival=?8,b.fair=?9 where b.ticketId=?1")     
	public void updateTotal(String a,String b,String c,String d,String e,String f,String g,String h,int i);
	
	
	
	
	
}
