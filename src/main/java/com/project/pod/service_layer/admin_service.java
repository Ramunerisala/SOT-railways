package com.project.pod.service_layer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pod.userRepository.trainRepository;

@Service
public class admin_service {
    
	@Autowired
	private trainRepository trepo;
	
public boolean getBoolean(String value) {
		
		if(value.equals("1")) {return true;}
		return false;
	}
	
	
public List<String>departurestations() {
	List<String> p=trepo.departureStations();
	Set<String> k=new HashSet<>();
	for(String i:p) {k.add(i);}
	p.clear();
	for(String i:k) {p.add(i);}
	Collections.sort(p);
	return p;
	
}








	
	
}
