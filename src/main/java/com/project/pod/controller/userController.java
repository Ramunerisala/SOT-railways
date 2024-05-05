package com.project.pod.controller;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.pod.service_layer.user_service;
import com.project.pod.userEntity.Train;
import com.project.pod.userEntity.bookings;
import com.project.pod.userEntity.users;
import com.project.pod.userRepository.bookingRepository;
import com.project.pod.userRepository.trainRepository;
import com.project.pod.userRepository.userRepository;

@Controller
public class userController {

	
	@Autowired
	private user_service user_service;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	List<String> stations = new ArrayList<String>() {
        {
            add("chennai");
        	add("Mumbai");
        	add("pune");
        	add("patna");
        	add("Gujarath");
        	add("Goa");
        	add("Andhra");
        	add("Telangana");
        	add("karnataka");
        	add("kerala");
        }
    };

    List<String> trains = new ArrayList<String>() {
        {
            add("Anuexpress");
            add("bujjikanna");
            add("chittikanna");
            add("Kajalexpress");
            add("Nayanexpress");
            add("Nithyaexpress");
            add("pallaviexpress");
            add("priyaexpress");
            add("Rashmikaexpress");
            add("Rojaexpress");
            add("Samexpress");
            add("shradhaexpress");
            add("Trishaexpress");
            add("deepikaexpress");
            add("katrinaexpress");
            add("kritiexpress");
            add("poojaexpress");
            add("rashiexpress");
            
        }
    };
	
	
	
	@Autowired
	public userRepository urepo;
	
	@Autowired
	public user_service service;
	
	@Autowired
	private trainRepository trepo;
	
	@Autowired
	private bookingRepository brepo;
	
	
	@GetMapping("/bookTicket")
	public String bookTicket(Model model) {
		bookings booking=new bookings();
		model.addAttribute("booking",booking);
		model.addAttribute("Stations",stations);
		System.out.println(stations);
		return "TicketBooking";
	}
	
	@PostMapping("/bookTicket")
	public ModelAndView bookTicket(@ModelAttribute("booking") bookings booking) {
		ModelAndView mav;
		List<bookings> book=new ArrayList<>();
		String p=booking.getDepartureDate();
		String q=booking.getDeparture();
		String r=booking.getArrival();
		System.out.println(q+" "+r);
		DateTimeFormatter dateformatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localdate=LocalDate.parse(p,dateformatter);
		LocalDate currentdate=LocalDate.now();
		DateTimeFormatter timeformatter=DateTimeFormatter.ofPattern("H:mm:ss");
		/* DateTimeFormatter timeformat=DateTimeFormatter.ofPattern("HH:mm:ss"); */
		LocalTime currenttime=LocalTime.now();
		
		int start=stations.indexOf(q);
		int end=stations.indexOf(r);
		
		for(int i=0;i<trains.size();i++) {
			if(isgoing(trains.get(i),q,r)) {
				
				
				if(start<end) {
					List<String>a=new ArrayList<>();
					
					if(start!=0) {a=substation(stations,0,start-1);}
					List<String>b=substation(stations,start,stations.size()-1);
					
					Train t=trepo.findByTDA(trains.get(i),q,stations.get(start+1));
					Train t1=trepo.findByTDA(trains.get(i), stations.get(end-1),r);
					String pk=t.getDepartureTime();String bk=t1.getArrivalTime();
					
					LocalTime localtime=LocalTime.parse(pk,timeformatter);
					
					if(currentdate.isBefore(localdate)) {int count=0;
						 for(int m=0;m<a.size();m++) {
							
							for(int n=0;n<b.size();n++) {
								if(n!=0) {
									String y=brepo.countBy(trains.get(i),a.get(m),b.get(n),p);
									System.out.println(y+" "+trains.get(i)+" "+a.get(m)+" "+b.get(n)+" "+p);
									
									int x=Integer.parseInt(brepo.countBy(trains.get(i),a.get(m),b.get(n),p));
									count=count+x;
								}
							}
						}
						
						 for(int m=0;m<b.size()-1;m++) {
							 
							if(b.get(m).equals(r)) {break;}
							for(int n=m+1;n<b.size();n++) {
								String y=brepo.countBy(trains.get(i),b.get(m),b.get(n),p);

								System.out.println(y+" "+trains.get(i)+" "+b.get(m)+" "+b.get(n)+" "+p);

								int x=Integer.parseInt(brepo.countBy(trains.get(i),b.get(m),b.get(n),p));
								count=count+x;
							}
						}
						
						if(count!=500) {
						bookings ticket=new bookings();
						ticket.setTrainName(trains.get(i));
						ticket.setDeparture(q);
						ticket.setArrival(r);
						ticket.setDepartureDate(p);
						ticket.setDepartureTime(t.getDepartureTime());
						int fare=100*Math.abs(start-end);
						if(early(t.getDepartureTime(),"8:00:00","11:00:00")) {fare=fare+(30*fare)/100;}
						 ticket.setFair(fare);
						 ticket.setArrivalTime(bk);
						ticket.setSeats(500-count);
						book.add(ticket);	}
					}
					
					else {
						
						 if(localtime.compareTo(currenttime)>0) {int count=0;
						 System.out.println(currenttime);System.out.println(localtime);
						 for(int m=0;m<a.size();m++) {
								for(int n=0;n<b.size();n++) {
									if(n!=0) {
										int x=Integer.parseInt(brepo.countBy(trains.get(i),a.get(m),b.get(n),p));
										count=count+x;
									}
								}
							}
							
							for(int m=0;m<b.size()-1;m++) {
								if(b.get(m).equals(r)) {break;}
								for(int n=m+1;n<b.size();n++) {
									int x=Integer.parseInt(brepo.countBy(trains.get(i),b.get(m),b.get(n),p));
									count=count+x;
								}
							}
							if(count!=500) {
							bookings ticket=new bookings();
							ticket.setTrainName(trains.get(i));
							ticket.setDeparture(q);
							ticket.setArrival(r);
							ticket.setDepartureDate(p);
							int fare=100*Math.abs(start-end);
							if(early(t.getDepartureTime(),"8:00:00","11:00:00")) {fare=fare+(30*fare)/100;}
							 ticket.setFair(fare);
							ticket.setDepartureTime(t.getDepartureTime());
							ticket.setArrivalTime(bk);
							ticket.setSeats(500-count);
							book.add(ticket);}
							 
						 }
						
						
						
						
						
						
						
						
						
					}
				}
					
					else {
						List<String> a=subrev(stations,stations.size()-1,start+1);
						List<String> b=subrev(stations,start,0);
						Train t=trepo.findByTDA(trains.get(i),q,stations.get(start-1));
						Train t1=trepo.findByTDA(trains.get(i),stations.get(end+1), r);
						System.out.println("ramu");
						String pk=t.getDepartureTime();String bk=t1.getArrivalTime();
						LocalTime localtime=LocalTime.parse(pk,timeformatter);
						
						if(currentdate.isBefore(localdate)) {int count=0;
						for(int m=0;m<a.size();m++) {
							for(int n=0;n<b.size();n++) {
								if(n!=0) {
									int x=Integer.parseInt(brepo.countBy(trains.get(i),a.get(m),b.get(n),p));
									count=count+x;
								}
							}
						}
						
						for(int m=0;m<b.size()-1;m++) {
							if(b.get(m).equals(r)) {break;}
							for(int n=m+1;n<b.size();n++) {
								int x=Integer.parseInt(brepo.countBy(trains.get(i),b.get(m),b.get(n),p));
								count=count+x;
							}
						}
						if(count!=500) {
						bookings ticket=new bookings();
						ticket.setTrainName(trains.get(i));
						ticket.setDeparture(q);
						ticket.setArrival(r);
						ticket.setDepartureDate(p);
						int fare=100*Math.abs(start-end);
						if(early(t.getDepartureTime(),"8:00:00","11:00:00")) {fare=fare+(30*fare)/100;}
						 ticket.setFair(fare);
						ticket.setDepartureTime(t.getDepartureTime());
						ticket.setArrivalTime(bk);
						ticket.setSeats(500-count);
						book.add(ticket);}
					}
						
						else {
							 if(localtime.compareTo(currenttime)>0) {int count=0;
							 for(int m=0;m<a.size();m++) {
									for(int n=0;n<b.size();n++) {
										if(n!=0) {
											int x=Integer.parseInt(brepo.countBy(trains.get(i),a.get(m),b.get(n),p));
											count=count+x;
										}
									}
								}
								
								for(int m=0;m<b.size()-1;m++) {
									if(b.get(m).equals(r)) {break;}
									for(int n=m+1;n<b.size();n++) {
										int x=Integer.parseInt(brepo.countBy(trains.get(i),b.get(m),b.get(n),p));
										count=count+x;
									}
								}
								if(count!=500) {
								bookings ticket=new bookings();
								ticket.setTrainName(trains.get(i));
								ticket.setDeparture(q);
								ticket.setArrival(r);
								ticket.setDepartureDate(p);
								int fare=100*Math.abs(start-end);
								if(early(t.getDepartureTime(),"8:00:00","11:00:00")) {fare=fare+(30*fare)/100;}
								 ticket.setFair(fare);
								ticket.setDepartureTime(t.getDepartureTime());
								ticket.setArrivalTime(bk);
								ticket.setSeats(500-count);
								book.add(ticket);}
								 
							 }
							
					
						}
						
			
					}	
					
	
				}
				
			
			}
		
		if(book.size()==0) {mav=new ModelAndView("no_trains_are_available");mav.addObject("message","no trains are available");return mav;}
		else {
			
			mav=new ModelAndView("list_of_available_trains");
			
			mav.addObject("books",book);return mav;
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		}
		
		
		
		
		
		
		
		

	
	public boolean isgoing(String t,String q,String r) {
		int start=stations.indexOf(q);
		int end=stations.indexOf(r);
		if(start<end) {
			for(int i=start;i<end;i++) {
				Train p=(Train)trepo.findByTDA(t,stations.get(i),stations.get(i+1));
				if(p==null) {return false;}
			}
			return true;
		}
		
		else {
			for(int i=start;i>=end+1;i--) {
				Train p=(Train)trepo.findByTDA(t,stations.get(i),stations.get(i-1));
				if(p==null) {return false;}
			}
			return true;
		}
		
		
	}
	
	public List<String> substation(List<String> stations,int start,int end){
		List<String>k=new ArrayList<>();
		for(int i=start;i<=end;i++) {
			k.add(stations.get(i));
		}
		return k;
	}
	public List<String> subrev(List<String> stations,int end,int start){
		List<String>k=new ArrayList<>();
		for(int i=end;i>=start;i--) {
			k.add(stations.get(i));
		}
		return k;
	}
	
	public boolean early(String a,String b,String c) {
		String arr[]=a.split(":");
		String brr[]=b.split(":");
		String crr[]=c.split(":");
		System.out.println(arr[0]);System.out.println(brr[0]);System.out.println(crr[0]);
		int x=Integer.parseInt(arr[0]);int y=Integer.parseInt(brr[0]);int z=Integer.parseInt(crr[0]);
		if(y<=x && x<=z) {return true;}
		return false;
	}
	
	@GetMapping("/bookNow")
	public String booknow(@RequestParam("trainName")String trainName,
			@RequestParam("departure")String departure,
			@RequestParam("arrival") String arrival,
			@RequestParam("departureTime") String departureTime,
	        @RequestParam("departureDate") String departureDate,@RequestParam("arrivalTime") String arrivalTime,
	        @RequestParam("fair")int fair,Model model,HttpSession session) {
		
		bookings k=new bookings();
		k.setTrainName(trainName);
		k.setDeparture(departure);
		k.setArrival(arrival);
		k.setDepartureTime(departureTime);
		k.setDepartureDate(departureDate);k.setArrivalTime(arrivalTime);
		
		if(session.getAttribute("modifyId")!=null) {
			System.out.println("fbdhfhb");
			String y=(String)session.getAttribute("modifyId");System.out.println("fbdhfhb");
			int x=(int)session.getAttribute("modifyfare");System.out.println("fbdhfhb");
			fair=fair+(30*fair)/100;k.setTicketId(y);k.setFair(fair-x);
			model.addAttribute("booking",k);
			return "modified ticket";
		}
		
		
		
		
		k.setFair(fair);
		/*
		 * if(session.getAttribute("tickets")==null) { List<bookings>p=new
		 * ArrayList<>(); session.setAttribute("tickets", p); } List<bookings>
		 * s=(List<bookings>)session.getAttribute("tickets"); s.add(k);
		 * session.setAttribute("tickets", s);
		 */
		
		model.addAttribute("ticket",k);
		return "user_ticket";
		
	}
	
	@PostMapping("/bookNow")
	public String booknow(@ModelAttribute("ticket") bookings k,HttpSession session) {
		
		
		if(session.getAttribute("list of tickets")==null) {
			List<bookings>p=new ArrayList<>();
			session.setAttribute("list of tickets", p);
		}
		
		List<bookings> s=(List<bookings>)session.getAttribute("list of tickets");
		if(k!=null) {s.add(k);}
		session.setAttribute("list of tickets", s);session.setMaxInactiveInterval(60);
		
		
		
		return "redirect:/tickets";
	}
	
	 
		@GetMapping("/tickets")
		public ModelAndView tickets(HttpSession session) {
			ModelAndView mav=new ModelAndView("tickets");
			List<bookings> s=(List<bookings>)session.getAttribute("list of tickets");
			
			int amount=0;
			for(bookings p:s) {
				amount=amount+(int)p.getFair();
			}
			
			session.setAttribute("total",amount);session.setMaxInactiveInterval(60);
			mav.addObject("amount","Total amount:"+amount);
			
			
			
			mav.addObject("x",s);
			return mav;
			
		}
	
	
	
	
	
	 @GetMapping("/edit")
	 public ModelAndView cancel(@RequestParam("name") String name,@RequestParam("t") String m,@RequestParam("departureDate") String
	 departureDate,@RequestParam("departureTime") String departureTime,@RequestParam("arrivalTime") String arrivalTime,@RequestParam("departure") String departure,@RequestParam("arrival") String arrival,
	 
	 @RequestParam("fair") int fair,Model model,HttpSession session) {
	 
	 ModelAndView mav=new ModelAndView("user_ticket");
	 bookings b=new bookings();
	 List<bookings>s=(List<bookings>)session.getAttribute("list of tickets");
	 List<bookings>p=new ArrayList<>(s);
	 for(bookings l:s){
		 if((l.getName().equals(name))&& (l.getTrainName().equals(m))&&
	 (l.getDepartureDate().equals(departureDate)) &&
	 (l.getDepartureTime().equals(departureTime)) && (l.getArrivalTime().equals(arrivalTime)) &&
	 (l.getDeparture().equals(departure))&& (l.getArrival().equals(arrival))&&
	 (l.getFair()==fair)) { p.remove(l);b=l;break;} }
	 
	 session.setAttribute("list of tickets",p);session.setMaxInactiveInterval(60);
	 
	  mav.addObject("ticket",b);
	 
	 return mav;
	 
	  }
	
	
   
	 @GetMapping("/cancel")
	 public String dippa(@RequestParam("name") String name,@RequestParam("t") String m,@RequestParam("departureDate") String
	 departureDate,@RequestParam("departureTime") String departureTime,@RequestParam("arrivalTime") String arrivalTime,@RequestParam("departure") String departure,@RequestParam("arrival") String arrival,
	 
	 @RequestParam("fair") int fair,Model model,HttpSession session) {
	 
	 
	 
	 List<bookings>s=(List<bookings>)session.getAttribute("list of tickets");
	 List<bookings>p=new ArrayList<>(s);
	 for(bookings l:s){
		 if((l.getName().equals(name))&& (l.getTrainName().equals(m))&&
	 (l.getDepartureDate().equals(departureDate)) &&
	 (l.getDepartureTime().equals(departureTime)) && (l.getArrivalTime().equals(arrivalTime)) &&
	 (l.getDeparture().equals(departure))&& (l.getArrival().equals(arrival))&&
	 (l.getFair()==fair)) { p.remove(l);break;} }
	 
	 session.setAttribute("list of tickets",p);session.setMaxInactiveInterval(60);
	 
	  
	 
	 return "redirect:/tickets";
	 
	  }
	
	
	@GetMapping("/checkout")
	public String checkout(Model model,HttpSession session) {
		int x=(int)session.getAttribute("total");session.setMaxInactiveInterval(60);
		model.addAttribute("amount","Total Amount:"+x);
		if(session.getAttribute("user")==null && session.getAttribute("guest")==null) {return "getguest";}
		return "checkout";
	}
	
	@PostMapping("/checkout")
	public String checkout(@ModelAttribute("email") String email,HttpSession session) {
		session.setAttribute("guest",email);
		return "redirect:/checkout";
	}
	
	
	
	
	@GetMapping("/cash")
	public ModelAndView cash(HttpSession session) throws UnsupportedEncodingException,MessagingException{
		ModelAndView mav=new ModelAndView("checkout");
		user_service.sendticket(session);session.setMaxInactiveInterval(60);
		session.setAttribute("message","Tickets sent to your given email,Please check it");
		/*
		 * mav.addObject(
		 * "message","Tickets Send to your registered email>please check it.");
		 */
		session.removeAttribute("guest");
		return mav;
		
	}
 
	@GetMapping("/history")
		public String history(HttpSession session,Model model) {
		users k=(users)session.getAttribute("user");session.setMaxInactiveInterval(60);
		List<bookings>m=(List<bookings>)brepo.findByEmail(k.getEmail());
		List<bookings>j=new ArrayList<>();
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatime=DateTimeFormatter.ofPattern("H:mm:ss");
		LocalDate currentdate=LocalDate.now();LocalTime currenttime=LocalTime.now();
		for(bookings l:m) {
			if(l.isEnable()==true) {
			String x=l.getDepartureDate();String z=l.getDepartureTime();
			LocalTime z1=LocalTime.parse(z,formatime);System.out.println(z1);
			LocalDate y=LocalDate.parse(x,formatter);
			if(y.isBefore(currentdate)||(y.isEqual(currentdate) && Duration.between(z1, currenttime).toMinutes()>0)) {j.add(l);}}
		}
		
		model.addAttribute("f",j);return "history";
	}
	
	
	
	@GetMapping("/upcoming")
	public String upcoming(HttpSession session,Model model) {
	users k=(users)session.getAttribute("user");session.setMaxInactiveInterval(60);
	List<bookings>m=(List<bookings>)brepo.findByEmail(k.getEmail());
	List<bookings>j=new ArrayList<>();
	DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter formatime=DateTimeFormatter.ofPattern("H:mm:ss");
	LocalDate currentdate=LocalDate.now();LocalTime currenttime=LocalTime.now();
	for(bookings l:m) {
		if(l.isEnable()==true) {
		String x=l.getDepartureDate();String z=l.getDepartureTime();
		LocalTime z1=LocalTime.parse(z,formatime);System.out.println(z1);
		LocalDate y=LocalDate.parse(x,formatter);
		if(y.isAfter(currentdate)||(y.isEqual(currentdate) && Duration.between(z1, currenttime).toMinutes()<0)) {j.add(l);}}
	}
	System.out.println("thinnavara");
	model.addAttribute("f",j);return "upcoming";
}
	
	
	
	@GetMapping("/card")
	public String card() {
		return "card_details";
	}
	
	@PostMapping("/card")
	public ModelAndView card(@RequestParam("card no") String number,HttpSession session)throws UnsupportedEncodingException,MessagingException {
		ModelAndView mav;
		System.out.println(number);
		if(user_service.luhnTest(number)) {
			user_service.sendit(session);
			mav=new ModelAndView("card_details");session.setAttribute("message", "Payment Succesful.Tickets send to your registerd Email.");return mav;
		}
		else {
			mav=new ModelAndView("card_details");session.setAttribute("message", "Payment UnSuccesful.Please check cardnumber and cvv");return mav;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/cancelTicket")
	public String cancelTicket(Model model) {
		bookings k=new bookings();
		model.addAttribute("ticket",k);
		return "cancelTicket";
	}
	
	@PostMapping("/cancelTicket")
	public ModelAndView cancelTicket(@ModelAttribute("ticket")bookings k) {
		bookings p=(bookings)brepo.findByTID(k.getTicketId());
		ModelAndView mav=new ModelAndView("cancelDetails");
		mav.addObject("z",p);
		return mav;
	}
	
	
	@GetMapping("/cancelit")
	public ModelAndView cancelit(@RequestParam("ticketId") String ticketId,HttpSession session)throws MessagingException,UnsupportedEncodingException {
		bookings p=(bookings)brepo.findByTID(ticketId);
		ModelAndView mav;
		if(p.isEnable()==false) {mav=new ModelAndView("cancelDetails");mav.addObject("z",p);
		
		session.setAttribute("message","Ticket is already cancelled");
			/* mav.addObject("message","Ticket is already cancelled."); */
		
		return mav;}
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate currentdate=LocalDate.now();
		LocalTime currenttime=LocalTime.now();
		
		DateTimeFormatter formatime=DateTimeFormatter.ofPattern("H:mm:ss");
		LocalTime localtime=LocalTime.parse(p.getDepartureTime(),formatime);
		LocalDate localdate=LocalDate.parse(p.getDepartureDate(),formatter);
		if(currentdate.isBefore(localdate)) {
			
			user_service.canceltick(p,session);
			mav=new ModelAndView("cancelDetails");session.setAttribute("message","cancellation successfull");
			return mav;}
		else if(currentdate.isEqual(localdate) && Duration.between(currenttime,localtime).toMinutes()>60) {
			user_service.canceltick(p,session);mav=new ModelAndView("cancelDetails");session.setAttribute("message","cancellation successful");
			return mav;
		}
		user_service.can(p,session);session.setMaxInactiveInterval(60);
		mav=new ModelAndView("cancellation unsuccesfull");
		return mav;
		
		
		
	}
	
	
	@GetMapping("/editProfile")
	public String profile(Model model,HttpSession session) {
		users p=(users)session.getAttribute("user");
		model.addAttribute("profile",p);
		return "profile";
	}
	
	
	@PostMapping("/editProfile")
	 public ModelAndView profile(@Valid @ModelAttribute("profile")users p,BindingResult errors,HttpSession session) 
	 throws UnsupportedEncodingException,MessagingException{ 
		 ModelAndView mav;
		 if(errors.hasErrors()) {mav=new ModelAndView("profile");mav.addObject("users",p);return mav;} 
		 
		 String password=passwordEncoder.encode(p.getPassword());
		 
		 urepo.updateUser(p.getEmail(),p.getFirstName(),p.getSurName(),password,p.getPhoneNumber());
		 users m=(users)urepo.findUser(p.getEmail());
		 session.setAttribute("user",m);
		 mav=new ModelAndView("profile");
		 session.setAttribute("message","Profile succesfully updated");
		 
		 return mav;
	   
	  }
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("message");
		session.removeAttribute("user");
		session.removeAttribute("total");
		
		return "redirect:/login";
	}
	
	
	@GetMapping("/modify")
	public String modify(Model model) {
		return "modify";
	}
	
	@PostMapping("/modify")
	public ModelAndView modify(@ModelAttribute("ticketId") String ticketId,HttpSession session) {
		bookings b=(bookings)brepo.findByTID(ticketId);
		ModelAndView mav;
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate currentdate=LocalDate.now();
		LocalTime currenttime=LocalTime.now();
		
		DateTimeFormatter formatime=DateTimeFormatter.ofPattern("H:mm:ss");
		LocalTime localtime=LocalTime.parse(b.getDepartureTime(),formatime);
		LocalDate localdate=LocalDate.parse(b.getDepartureDate(),formatter);
		
		
		if(currentdate.isBefore(localdate) ||(currentdate.isEqual(localdate) && Duration.between(currenttime,localtime).toMinutes()>60)) {
		mav=new ModelAndView("booking");
		mav.addObject("b",b);
		return mav;}
		
		else {
			
			mav=new ModelAndView("modify");session.setAttribute("message","This ticket can't modify right now as duration is less than 60min");
		return mav;
		}
		
		
		
	}
	
	@GetMapping("/modifyticket")
	public String modifyticket(HttpSession session,@RequestParam("ticketId") String ticketId,@RequestParam("fair") int fare) {
		session.setAttribute("modifyId",ticketId);
		session.setAttribute("modifyfare",fare);
		
		return "redirect:/bookTicket";
	}
	
	
	@PostMapping("/modifyticket")
	public String modifyticket(HttpSession session,@ModelAttribute("booking") bookings b) throws UnsupportedEncodingException,MessagingException{
		
		session.removeAttribute("modifyId");
		session.removeAttribute("modifyfare");
		brepo.updateTotal(b.getTicketId(),b.getName(),b.getTrainName(),b.getDepartureDate(),b.getDepartureTime(),b.getArrivalTime(),b.getDeparture(),b.getArrival(),b.getFair());
		service.moditicket(session,b);
		session.setAttribute("message","ticket modified succesfully");
		return "modified ticket";
		
	}
	
	
	@GetMapping("/guest")
	public String guest(HttpSession session) {
		
		session.removeAttribute("user");
		return "redirect:/bookTicket";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
