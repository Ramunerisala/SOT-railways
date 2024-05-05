package com.project.pod.userEntity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Train")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train {
    

	
	
	
	
	
	public Train() {
		super();
	}
	public Train(Long id, String name, String fromStation, String toStation, String departureTime, String arrivalTime,
			boolean enable) {
		super();
		this.id = id;
		this.name = name;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.enable = enable;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String fromStation;
	private String toStation;
	
	private String departureTime;
	private String arrivalTime;
	private boolean enable;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	
	
}
