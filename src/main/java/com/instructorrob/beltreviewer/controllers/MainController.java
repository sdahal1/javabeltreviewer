package com.instructorrob.beltreviewer.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.instructorrob.beltreviewer.models.Group;
import com.instructorrob.beltreviewer.models.User;
import com.instructorrob.beltreviewer.models.UserGroupMembers;
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
	
	//////////////////////REST OF BELT STARTS HERE///////////
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		//retrieve the userobject from the db who'se id matches the id stored in session
		Long id = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(id);
		
		model.addAttribute("loggedinuser", loggedinuser);
		model.addAttribute("allgroups", loggedinuser.getGroupsBelongTo());
		model.addAttribute("notmygroups", this.userService.findGroupsNotBelongingTo(loggedinuser));
		return "dashboard.jsp";
		
	}
	
	@GetMapping("/groups/new")
	public String newGroup(@ModelAttribute("group") Group group, Model model) {
		
		model.addAttribute("allusers", this.userService.findAllUsers() );
		
		return "newgroup.jsp";
	}
	
	@PostMapping("/groups/create")
	public String createGroup(@Valid @ModelAttribute("group") Group group, BindingResult result, Model model, HttpSession session) {
		
		if(result.hasErrors()) {
			
			model.addAttribute("allusers", this.userService.findAllUsers() );
			return "newgroup.jsp";
		}
		
		//grab the logged in user so we can assign this user to be the group's creator
		Long id = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(id);
		
		group.setCreator(loggedinuser);
		
		this.userService.createGroup(group);
		
		return "redirect:/dashboard";
	}
	
	
	@GetMapping(value="/groups/{id}")
	public String showAGroup(@PathVariable("id") Long id, Model model, HttpSession session ) {
		//get logged in user
		Long loggedinuserid = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(loggedinuserid);
		
		
		model.addAttribute("loggedinuser", loggedinuser);
		model.addAttribute("groupToshow", this.userService.findAGroup(id));
		return "showgroup.jsp";
	}
	
	@GetMapping("/edit/{id}")
	public String editGroup(@PathVariable("id") Long id, Model model) {
		
		model.addAttribute("group", this.userService.findAGroup(id));
		
		model.addAttribute("allusers", this.userService.findAllUsers() );
		
		return "edit.jsp";
	}
	
	@PostMapping("/groups/update/{id}")
	public String updateGroup(@PathVariable("id") Long id, @Valid @ModelAttribute("group") Group group, BindingResult result, Model model, HttpSession session ) {
		if(result.hasErrors()) {
			//gets all the users to populate the drop down for vps' and sends to html using model.addattribute
			model.addAttribute("allusers", this.userService.findAllUsers() );
			
			return "edit.jsp";
		}
		
		//send the group info to service to update
		System.out.println("********************");
		System.out.println(group.getId());
		System.out.println(group.getName());
		System.out.println(group.getMembers());
		
		Group g = this.userService.findAGroup(id);
		group.setMembers(g.getMembers());
//		
		
		Long loggedinuserid = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(loggedinuserid);
		
		group.setCreator(loggedinuser);
		System.out.println("********************");
		
		this.userService.updateAGroup(group);
		
		return "redirect:/dashboard";
	}
	
	
	@GetMapping("/delete/{id}")
	public String deletegroup(@PathVariable("id")Long id) {
		Group g = this.userService.findAGroup(id);
		this.userService.deleteGroup(g);
		
		return "redirect:/dashboard";
	}
	
	
	@GetMapping("/join/{id}")
	public String joinGroup(@PathVariable("id")Long id, HttpSession session) {
		//get the group from the id from the pathvariable
		Group g = this.userService.findAGroup(id);
		
		//get the logged in user
		Long loggedinuserid = (Long)session.getAttribute("userid");
		User loggedinuser = this.userService.findUserById(loggedinuserid);
		
		
		//if the loggedinuser is already a member of group, just redirect, else, create the association
		
		if(g.getMembers().contains(loggedinuser)) {
			System.out.println("YOU ALREADY IN THE GROUP FOOO");
			return "redirect:/";
		}
		//create an object that we can save to the many to many table by calling on the constructor of the many to many table
		UserGroupMembers ugMembers = new UserGroupMembers(loggedinuser, g);
		
		//tell the service to save this object to the third table(many to many table)
		this.userService.createAssociation(ugMembers);
		
		return "redirect:/dashboard";
	}
	
	
	@GetMapping("/leave/{id}")
	public String leaveGroup(@PathVariable("id")Long id, HttpSession session) {
		//get the group from the id from the pathvariable
				Group g = this.userService.findAGroup(id);
				
				//get the logged in user
				Long loggedinuserid = (Long)session.getAttribute("userid");
				User loggedinuser = this.userService.findUserById(loggedinuserid);
				
				
				//if the loggedinuser is already a member of group, just redirect, else, create the association
				
				if(!g.getMembers().contains(loggedinuser)) {
					System.out.println("YOU WERENT WIT ME SHOOTING IN THE GYM? HOW U GONA LEAVE A GROUP U NOT A PART OF?");
					return "redirect:/";
				}
				
				
				//update the user objects list of groups that they belong to to have the selected group removed from that list
				loggedinuser.getGroupsBelongTo().remove(g);
				
				//save that updated user to the db using the service
				this.userService.leaveGroup(loggedinuser);
				
			
				
				return "redirect:/dashboard";
				
	}
	
	

	
	
}
