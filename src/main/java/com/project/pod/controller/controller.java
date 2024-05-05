package com.project.pod.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.pod.service_layer.service_layer;
import com.project.pod.userEntity.users;
import com.project.pod.userRepository.userRepository;
import com.project.pod.websecurity.User;

@Controller
public class controller {
    
	@Autowired
	private service_layer service;
	
	@Autowired
	private userRepository urepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/home")
	public String home() {
		return "this is home page";
	}
  
	@GetMapping("/register")
	public String register_form(Model model) {
		users users=new users();
		model.addAttribute("users",users);
		return "register_form";
	}
	
	
	@GetMapping("/")
	public String main() {
		return "main";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 @PostMapping("/register")
	 public ModelAndView register(@Valid @ModelAttribute("users")users users,BindingResult errors,HttpServletRequest request) 
	 throws UnsupportedEncodingException,MessagingException{ 
		 ModelAndView mav;
		 if(errors.hasErrors()) {mav=new ModelAndView("register_form");mav.addObject("users",users);return mav;} 
		 else if(service.findUser(users)){
			 mav=new ModelAndView("register_form");mav.addObject("message","You are already register with this EmailID");return mav;
		 }
		 service.register(users, getSiteURL(request));
		 mav=new ModelAndView("register_sucess");
		 return mav;
	   
	  }
	 public String getSiteURL(HttpServletRequest request) {
		 String siteURL=request.getRequestURL().toString();
		 return siteURL.replace(request.getServletPath(), "");
	 }
	 
	 @GetMapping("/verify")
	 public String verify(@Param("code")String code) {
		 
		 if(service.verificate(code)) {return "registration_sucess";}
		 else {return "unsuccesful registration";}
		 
	 }
	 
	   @GetMapping("/login")
	   public ModelAndView login(){
		   users users=new users();
		   ModelAndView mav=new ModelAndView("login_page");
		   mav.addObject("users",users);
		   return mav;
	   }
	 
	 
	 @PostMapping("/login")
	 public ModelAndView credentials(@ModelAttribute("users") users users,HttpSession session) {
		 ModelAndView mav;
		 if(service.check(users)==false) {
			 mav=new ModelAndView("login_page");
			 session.setAttribute("message","Login fail,please check your credentials.Thank you.");
				/*
				 * mav.addObject("message","Login fail,please check your credentials.Thank you."
				 * );
				 */
			 
			 return mav;
		 }
		 System.out.println(".......................");
		 users m=urepo.findUser(users.getEmail());System.out.println(users.getEmail());
		 session.setAttribute("user", m);session.setMaxInactiveInterval(60);
		 session.removeAttribute("guest");
		 users p=(users)session.getAttribute("user");
		 mav=new ModelAndView("login_success");
		 mav.addObject("message","Welcome "+p.getFirstName()+" "+p.getSurName());
		 return mav;
	 }
	
	 @GetMapping("/forgot password")
	 public String forgot(Model model) {
		 users user=new users();
		 model.addAttribute("users",user);
		 return "forgot";
	 }
	 
	 @PostMapping("/forgot password")
	 public ModelAndView forgot(@ModelAttribute("users") users user,HttpSession session) throws MessagingException,UnsupportedEncodingException{
		 
		 ModelAndView mav;session.setAttribute("email", user.getEmail());
		 if(service.findUser(user)==false) {mav=new ModelAndView("forgot");session.setAttribute("message","This Email is not registered");
		 return mav;}
		 service.sendotp(user,session);
		 mav=new ModelAndView("otp verify");
		 return mav;
		 
		 
	 }
	 
	 @PostMapping("/verifyotp")
	 public ModelAndView otp(@RequestParam("otp")int otp,HttpSession session) {
		 int x=(int)session.getAttribute("otp");ModelAndView mav;
		 if(otp!=x) {mav=new ModelAndView("otp verify");session.setAttribute("message", "invalid otp");return mav;}
		 else {mav=new ModelAndView("reset_form");return mav;}
	 }
	
	 @PostMapping("/savenew")
	 public ModelAndView otp(@RequestParam("password") String password,HttpSession session) {
		 String email=(String)session.getAttribute("email");
		 
		 String encode=passwordEncoder.encode(password);
		 ModelAndView mav=new ModelAndView("reset_form");
		 session.setAttribute("message","password reset successful");
		 
		 urepo.updateByPassword(encode,email);
		 return mav;
	 }
	 
		 
	 
	 
	 
	 
	 
	 
	 
	 
	
	
	
	
}
