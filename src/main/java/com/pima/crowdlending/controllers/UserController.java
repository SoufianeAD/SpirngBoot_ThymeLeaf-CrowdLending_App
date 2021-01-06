package com.pima.crowdlending.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pima.crowdlending.entities.Admin;
import com.pima.crowdlending.entities.Material;
import com.pima.crowdlending.entities.User;
import com.pima.crowdlending.repositories.AdminRepository;
import com.pima.crowdlending.repositories.UserRepository;


@Controller
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@GetMapping("/signupUserForm")
	public String signupUserForm(Model model) {
		model.addAttribute("user", new User());
		return "SignupUser";
	}
	
	@PostMapping("/signupUser")
	public String signupUser(@ModelAttribute("user") User user) {
		user.setStatus(true);
		userRepository.save(user);
		return "redirect:/user/signinForm";
	}
	
	// forgot password area
	
	@GetMapping("/forgotPassword")
	public String forgotPasswordForm(Model model) {
		model.addAttribute("user", new User());
		return "forgotPasswordForm";
	}
	
	@PostMapping("/forgotPassword")
	public String forgotPasswordUser(@RequestParam("email") String  email,Model model) {
		System.out.println("helloooo");
		User  user = userRepository.findByEmail(email);
		System.out.println("i am :" + user);
		if(null != user) {
			//model.addAttribute("usrID",user.getId());

			return "redirect:/user/changPassForm?usrid="+user.getId();

		}
		//userRepository.save(user);
		return "redirect:/user/forgotPassword";

	}
	
	//changing password area
	@GetMapping("/changPassForm")
	public String changPassForm(@RequestParam("usrid") int  usrid,Model model) {
		System.out.println(">>>>> >>>>> usrid :" + usrid);

		model.addAttribute("usrID",usrid);
		return "changPassForm";
	}
	
	@PostMapping("/changPassForm")
	public String changPass(@RequestParam("usrID") int  usrID,@RequestParam("pass") String  pass,@RequestParam("passConf") String  passConf) {
		System.out.println("pass :" + pass + " conf pass : " + passConf);
		Optional<User>  op = userRepository.findById(usrID);
		if(op.get() != null) {
			User user  = op.get();
			user.setPassword(pass);
			userRepository.save(user);
			//return "/user/successChangePass";
		}
		return "redirect:/";
	}
	
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") int id) {
		userRepository.deleteById(id);
		return "redirect:/user/getAll";
	}
	
	@GetMapping("/editUserForm/{id}")
	public String editUserForm(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", userRepository.findById(id));
		return "EditUser";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("user") User user) {
		System.out.println(user.getId());
		User u = userRepository.findById(user.getId()).orElse(null);
		user.setPassword(u.getPassword());
		user.setStatus(u.isStatus());
		userRepository.save(user);
		return "redirect:/user/getAll";
	}
	
	@GetMapping("/getAll")
	public String getAll(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "Users";
	}
	
	@GetMapping("/getByFilter")
	public String getByFilter(@RequestParam("filter") String filter, Model model) {
		model.addAttribute("users", userRepository.findByFilter(filter));
		return "Users";
	}
	
	@GetMapping("/block/{id}")
	public String block(@PathVariable("id") int id) {
		User user = userRepository.findById(id).orElse(null);
		user.setStatus(!user.isStatus());
		userRepository.save(user);
		return "redirect:/user/getAll";
	}
	
	@PostMapping("/signin")
	public String signIn(Model model, @RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
		User user = userRepository.findByEmailAndPasswaord(email, password);
		Admin admin = adminRepository.findByEmailAndPasswaord(email, password);
		if (admin != null) {
			request.getSession().setAttribute("user", admin.getId());
			request.getSession().setAttribute("userName", admin.getFirstName());
			request.getSession().setAttribute("role", "admin");
			return "redirect:/dashboard";
		} else if(user == null) {
			model.addAttribute("message", "Email or password is incorrect !");
			return "Signin";
		} else if(user.isStatus()) {
			request.getSession().setAttribute("user", user.getId());
			request.getSession().setAttribute("userName", user.getFirstName() + " " + user.getLastName());
			request.getSession().setAttribute("role", "user");
			return "redirect:/dashboardUser";
		}
		return "SigninBlocked";
	}
	
	@GetMapping("/signinForm")
	public String signInForm() {
		return "Signin";
	}
	
	@GetMapping("/signout")
	public String signout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userName");
		return "redirect:/user/signinForm";
	}
	
}
