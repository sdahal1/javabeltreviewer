package com.instructorrob.beltreviewer.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.instructorrob.beltreviewer.models.User;
import com.instructorrob.beltreviewer.services.UserService;
import com.instructorrob.beltreviewer.validation.UserValidator;

@Controller
public class MainController {
	
	private final UserService userService;
	private final UserValidator userValidator;
    
    public MainController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

	
	

	@GetMapping("/")
	public String home(@ModelAttribute("user") User user) {
		
		return "index.jsp";
	}
	
	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session ) {
		System.out.println("*********************");
		System.out.println(user.getEmail());
		System.out.println("*********************");
//		System.out.println(this.userService.findByEmail(user.getEmail()).toString());
		userValidator.validate(user, result);
		if(result.hasErrors()) {
			return "index.jsp";
		}
		//TODO later after login reg works, prevent dupe emails
		
		//create a user with this information
		User u = this.userService.registerUser(user);
		//get the user that just got created's id and put it in session
		session.setAttribute("userid", u.getId());
		return "redirect:/dashboard";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		//retrieve the userobject from the db who'se id matches the id stored in session
		Long id = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(id);
		
		model.addAttribute("loggedinuser", loggedinuser);
		return "dashboard.jsp";
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes ) {
		
		
		Boolean isLegit = this.userService.authenticateUser(email, password);
		
		if(isLegit) {
			//if the email password combo is correct, log them in using session and redirecting them to dashboard
			
			//get the user with that email
			User user = this.userService.findByEmail(email);
			//put that users id in session
			session.setAttribute("userid", user.getId());
			return "redirect:/dashboard";
		}
		//if login is not successful, flash them a message
		redirectAttributes.addFlashAttribute("error", "Invalid login attempt");
		return "redirect:/";
	}
	
	
	
	
}
