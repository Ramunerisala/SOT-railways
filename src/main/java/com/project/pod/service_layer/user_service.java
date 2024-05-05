package com.project.pod.service_layer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.pod.userEntity.bookings;
import com.project.pod.userEntity.users;
import com.project.pod.userRepository.bookingRepository;
import com.project.pod.userRepository.userRepository;

@Service
public class user_service {
    
	public static int n=1;
	@Autowired
	public userRepository urepo;
	
	@Autowired
	public bookingRepository brepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public  void sendticket(HttpSession session) throws UnsupportedEncodingException,MessagingException{
		List<bookings> x=(List<bookings>)session.getAttribute("list of tickets");
		List<bookings>z=new ArrayList<>();
		String f;
		if(session.getAttribute("guest")==null) {users k=(users)session.getAttribute("user");f=k.getEmail();}
		else {f=(String)session.getAttribute("guest");}
		session.setMaxInactiveInterval(60);
		
		for(bookings y:x) {
			String l=Integer.toString(new Random().nextInt(900000)+100000);
			y.setEnable(true);String ticketId="";y.setPaymentType("cash");
			ticketId=ticketId+y.getDeparture().charAt(0)+y.getArrival().charAt(0)+"000"+l+y.getArrival().charAt(0)+y.getDeparture().charAt(0);
			y.setTicketId(ticketId);z.add(y);y.setEmail(f);brepo.save(y);
		}
		session.setAttribute("list of tickets", z);session.setMaxInactiveInterval(60);
		mailtickets(session);
	}
	private void mailtickets(HttpSession session) throws MessagingException,UnsupportedEncodingException{
		
		String f;
		if(session.getAttribute("guest")==null) {users k=(users)session.getAttribute("user");f=k.getEmail();}
		else {f=(String)session.getAttribute("guest");}
		
		session.setMaxInactiveInterval(60);
		String toAddress=f;
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String senderName="Ramu_Nerisala";
		String subject="Booking Confirmed";
		String content="Thank you for booking tickets.Please check tickets details below.<br/>";
		
		
		List<bookings> r=(List<bookings>)session.getAttribute("list of tickets");session.setMaxInactiveInterval(60);
		session.removeAttribute("list of tickets");
		
		for(bookings l:r) {
			content+=l.getTicketId()+" "+l.getTrainName()+" "+l.getDeparture()+" "+l.getArrival()+" "+l.getDepartureTime()+" "+
		l.getDepartureDate()+"<br/>";
		}
		
		content+="Thank you for booking.Happy journey.";
		
		
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
		
		
	}
	
	public void canceltick(bookings p,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		
		brepo.updateEnable(p.getTicketId());
		cancelmail(p,session);session.setMaxInactiveInterval(60);
	}
	
	private void cancelmail(bookings p,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		users k=(users)session.getAttribute("user");session.setMaxInactiveInterval(60);
		String toAddress=k.getEmail();
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String sendername="Ramu_Nerisala";
		String subject="Ticket cancelled";
		
		String content="Dear "+p.getName()+"<br/>"+"Your ticket from "+p.getDeparture()+" to "+p.getArrival()+" on "+p.getDepartureDate()+
				" is successfully cancelled.Amount "+p.getFair()+" will be refund with in a week to your bank account"+"<br/>"+
				"Here are the journey details:<br/>"+p.getTicketId()+" "+p.getName()+" "+p.getDeparture()+" "+p.getArrival()+" "+
				p.getDepartureTime()+" "+p.getArrivalTime()+" "+p.getFair()+"<br/>"+".Thank you";
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,sendername);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
		
		
		
	}
	
	
public void can(bookings p,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		
		brepo.updateEnable(p.getTicketId());session.setMaxInactiveInterval(60);
		cancelm(p,session);
	}
	
	private void cancelm(bookings p,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		users k=(users)session.getAttribute("user");session.setMaxInactiveInterval(60);
		String toAddress=k.getEmail();
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String sendername="Ramu_Nerisala";
		String subject="Ticket cancelled";
		
		String content="Dear "+p.getName()+"<br/>"+"Your ticket from "+p.getDeparture()+" to "+p.getArrival()+" on "+p.getDepartureDate()+
				" is successfully cancelled.Amount "+p.getFair()+" will not be refund as you cancelled less than an hour before departure time"+"<br/>"+
				"Here are the journey details:<br/>"+p.getTicketId()+" "+p.getName()+" "+p.getDeparture()+" "+p.getArrival()+" "+
				p.getDepartureTime()+" "+p.getArrivalTime()+" "+p.getFair()+"<br/>"+".Thank you";
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,sendername);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
		
		
		
	}
	
	public static boolean luhnTest(String number) {
		int s1=0;int s2=0;
		String reverse=new StringBuffer(number).reverse().toString();
		for(int i=0;i<reverse.length();i++) {
			int digit=Character.digit(reverse.charAt(i), 10);
			if(i%2==0) {
				s1+=digit;
			}
			else {
				s2+=2*digit;
				if(digit>=5) {s2-=9;}
			}
		}
		return ((s1+s2)%10==0);
	}
	
	
	
	public  void sendit(HttpSession session) throws UnsupportedEncodingException,MessagingException{
		List<bookings> x=(List<bookings>)session.getAttribute("list of tickets");
		List<bookings>z=new ArrayList<>();
		
		String f;
		if(session.getAttribute("guest")==null) {users k=(users)session.getAttribute("user");f=k.getEmail();}
		else {f=(String)session.getAttribute("guest");}
		session.setMaxInactiveInterval(60);
		for(bookings y:x) {
			String l=Integer.toString(new Random().nextInt(900000)+100000);
			
			y.setEnable(true);String ticketId="";y.setPaymentType("card");
			ticketId=ticketId+y.getDeparture().charAt(0)+y.getArrival().charAt(0)+"000"+l+y.getArrival().charAt(0)+y.getDeparture().charAt(0);
			y.setTicketId(ticketId);z.add(y);y.setEmail(f);brepo.save(y);
		}
		session.setAttribute("list of tickets", z);session.setMaxInactiveInterval(60);
		mailtickets(session);
	}
	
	
	
	public void moditicket(HttpSession session,bookings p) throws UnsupportedEncodingException,MessagingException{
		

		String f;
		if(session.getAttribute("guest")==null) {users k=(users)session.getAttribute("user");f=k.getEmail();}
		else {f=(String)session.getAttribute("guest");}
		
		session.setMaxInactiveInterval(60);
		String toAddress=f;
		String fromAddress="vrprasadnerisala2001@gmail.com";
		String sendername="Ramu_Nerisala";
		String subject="Ticket Modified";
		
		String content="Dear "+p.getName()+"<br/>"+"Your ticket from "+p.getDeparture()+" to "+p.getArrival()+" on "+p.getDepartureDate()+
				" is successfully Modified. "+
				"Here are the journey details:<br/>"+p.getTicketId()+" "+p.getName()+" "+p.getDeparture()+" "+p.getArrival()+" "+
				p.getDepartureTime()+" "+p.getArrivalTime()+" "+p.getFair()+"<br/>"+".Thank you";
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress,sendername);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		helper.setText(content,true);
		mailSender.send(message);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
