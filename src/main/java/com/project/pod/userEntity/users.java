package com.project.pod.userEntity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class users {
     
	public users() {
		super();
	}
	public users(Long id, String email, String firstName, String surName, String password, String phoneNumber, Date dob,
			String verificationCode, boolean enable,String Re_password) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.surName = surName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.dob = dob;
		this.verificationCode = verificationCode;
		this.enable = enable;
		this.Re_password=Re_password;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	@Email(regexp="^(.+)@(.+)$",message="Invalid email pattern") 
	private String email;
	@NotNull
	@Size(min=2,message="Length should be more than 2")
	private String firstName;
	@NotNull(message="SurName should be empty")
	private String surName;
	@NotEmpty(message="password should not be empty")
	@NotNull(message="password should not be empty")
	private String password;
	private String Re_password;
	@Size(max=10,message="Number of digits should be 10")
	@Pattern(regexp="[7-9]{1}[0-9]{9}",message="Mobile number invalid")
	private String phoneNumber;
	private Date dob;
	private String verificationCode;
	private boolean enable;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getRe_password() {
		return Re_password;
	}
	public void setRe_password(String re_password) {
		Re_password = re_password;
	}
	
	
	
	
	
}
