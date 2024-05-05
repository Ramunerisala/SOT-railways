package com.project.pod.service_layer;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.pod.userEntity.users;
import com.project.pod.userRepository.userRepository;
import com.project.pod.websecurity.User;
import com.project.pod.websecurity.WebRepository;

import net.bytebuddy.utility.RandomString;



@Service
public class service_layer {
     
	
	@Autowired
	private WebRepository arepo;
	
	@Autowired
	private userRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public List<users> listAll(){return repo.findAll();}
	
	public void register(users user,String siteURL) throws UnsupportedEncodingException,MessagingException{
		
		System.out.println(user.getPassword());
		
		 String encodePassword =passwordEncoder.encode(user.getPassword());
		 user.setPassword(encodePassword);
		 
		String randomCode =RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnable(false);
		repo.save(user);
		sendVerificationEmail(user,siteURL);
	
	}
	private void sendVerificationEmail(users user,String siteURL) throws MessagingException,UnsupportedEncodingException{
		String toAddress=user.getEmail();
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String senderName="Ramu_Nerisala";
		String subject="please verify your registartion";
		String content="Dear [name],<br/>"+"please click the link below to verify your registration:<br/>"
		+"<h3><a href=\"[URL]\" target=\"_self\">VERIFY</a></h3>"+"Thank you <br/>"+"Ramu_Nerisala.";
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		content=content.replace("[name]", user.getFirstName()+" "+user.getSurName());
		String verifyURL=siteURL+"/verify?code="+user.getVerificationCode();
		content=content.replace("[URL]", verifyURL);
		helper.setText(content,true);
		mailSender.send(message);
		
		
	}
	
	public users findUserByVerificationCode(String code) {
		users user=repo.findUserByVerificationCode(code);
		return user;
	}
	
	
	public boolean verificate(String code) {
		users user=repo.findUserByVerificationCode(code);
		if((user==null)||(user.isEnable())) {return false;}
		
		else {
			user.setVerificationCode(null);
			user.setEnable(true);
			repo.save(user);
			return true;
			
		}
	}
	
	
	public boolean check(users user) {
		String a=user.getEmail();
		/* String b=passwordEncoder.encode(user.getPassword()); */
		
		users k=repo.findUser(a);
		/*
		 * System.out.println(k.getPassword()); System.out.println(user.getPassword());
		 */
		
		System.out.println(a);
		if((k==null)||k.isEnable()==false) {return false;}
		
		
		
		else if(passwordEncoder.matches(user.getPassword(),k.getPassword())) {return true;}
		return false;
	}
	
	public boolean findUser(users user) {
		users k=repo.findUser(user.getEmail());
		if(k==null) {return false;}
		return true;
	}
	
	public users findByEmail(String a) {
		users user=repo.findUser(a);
		return user;
	}
	
public void reset(users user,String siteURL) throws UnsupportedEncodingException,MessagingException{
		String randomCode =RandomString.make(64);
		user.setVerificationCode(randomCode);
		repo.updateByEmail(randomCode,user.getEmail());
		user.setEnable(true);
		
		ResetVerificationEmail(user,siteURL);
	
	}

private void ResetVerificationEmail(users user,String siteURL) throws MessagingException,UnsupportedEncodingException{
	String toAddress=user.getEmail();
	String fromAddress="vrprasadnerisala2001@gmail.com";
	String senderName="Ramu_Nerisala";
	String subject="Reset password";
	String content="Dear [name],<br/>"+"please click the link below to reset your password:<br/>"
	+"<h3><a href=\"[URL]\" target=\"_self\">RESET</a></h3>"+"Thank you <br/>"+"Ramu_Nerisala.";
	
	MimeMessage message=mailSender.createMimeMessage();
	MimeMessageHelper helper=new MimeMessageHelper(message);
	
	helper.setFrom(fromAddress,senderName);
	helper.setTo(toAddress);
	helper.setSubject(subject);
	content=content.replace("[name]", user.getFirstName()+" "+user.getSurName());
	String verifyURL=siteURL+"/reset?code="+user.getVerificationCode();
	content=content.replace("[URL]", verifyURL);
	helper.setText(content,true);
	mailSender.send(message);
	
	
}

public boolean resetificate(String code) {
	users user=repo.findUserByVerificationCode(code);
	if(user==null) {return false;}
	return true;
}

   public void updateByPassword(users user) {
	   System.out.println(user.getPassword());
	   repo.updateByPassword(user.getPassword(),user.getEmail());
   }
 
   
   public boolean adminCheck(User user) {
	   User k=arepo.findByUsername(user.getUsername());
	   if(k==null) {return false;}
	   else if(passwordEncoder.matches(user.getPassword(),k.getPassword())) {return true;}
	   return false;
   }
   
   public void sendotp(users user,HttpSession session) throws MessagingException,UnsupportedEncodingException{
	   int otp=new Random().nextInt(900000)+100000;
	   session.setAttribute("otp", otp);session.setMaxInactiveInterval(60);
	   mailotp(user,otp,session);
   
   }
   
   private void mailotp(users user,int otp,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		String toAddress=user.getEmail();
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String senderName="Ramu_Nerisala";
		String subject="Reset password";int m=(int)session.getAttribute("otp");
		users p=(users)session.getAttribute("user");
		String content="Dear Customer"+"<br/>"+" OTP: "+m+"<br/>"+"<br/>"+
		" Above is the otp for reset password.Do not share with anyone.<br/>"+"Thank you <br/>"+senderName;
		
		
		
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
		
		
	}
   
   
   
   
   
   
   
   
	
	
	
	
}
