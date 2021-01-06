package com.pima.crowdlending.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.pima.crowdlending.repositories.DemandeRepository;
import com.pima.crowdlending.repositories.MaterialRepository;
import com.pima.crowdlending.repositories.UserRepository;

@org.springframework.stereotype.Controller
@CrossOrigin("*")
public class Controller {
	
	@Autowired
	private UserRepository userRepository;
	
	//Repository of materials
	@Autowired
	private MaterialRepository materialRepository;
	
	@Autowired
	private DemandeRepository demandRepository;
	
	@GetMapping("/")
	public String index() {
		return "redirect:/user/signinForm";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		int nbUsers = userRepository.findAll().size();
		model.addAttribute("nbUsers", nbUsers);
		int nbMaterials = materialRepository.findAll().size();
		model.addAttribute("nbMaterials", nbMaterials);
		int nbrequests = demandRepository.findAll().size();
		model.addAttribute("nbRequests", nbrequests);
		int pendingRequests = demandRepository.findUnvalidatedDemandes().size();
		model.addAttribute("nbPendingRequests", pendingRequests);
		/*chart*/
		String []months = new String [] {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
		int data[] = new int[12];
		for(int i=1; i <= 9; i++) {
			data[i - 1] = demandRepository.countDemands("0"+i);
		}
		for(int i=10; i <= 12; i++) {
			data[i - 1] = demandRepository.countDemands(i+"");
		}
		model.addAttribute("label", months);
		model.addAttribute("data", data);
		
		return "DashBoard";
	}
	
	@GetMapping("/dashboardUser")
	public String dashboardUser(Model model, HttpServletRequest request) {
		System.out.println("session connected user" + (int) request.getSession().getAttribute("user"));
		int nbMaterials = materialRepository.findUserMaterials((int) request.getSession().getAttribute("user")).size();
		model.addAttribute("nbMaterials", nbMaterials);
		int nbrequests = demandRepository.findDemandesByUser((int) request.getSession().getAttribute("user")).size();
		model.addAttribute("nbRequests", nbrequests);
		int lands = demandRepository.findlandsByUser((int) request.getSession().getAttribute("user")).size();
		model.addAttribute("lands", lands);
		int pendingRequests = demandRepository.findPendingRequestsByUser((int) request.getSession().getAttribute("user")).size();
		model.addAttribute("pendingRequests", pendingRequests);
		
		/*chart*/
		String []months = new String [] {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
		int data[] = new int[12];
		for(int i=1; i <= 9; i++) {
			data[i - 1] = demandRepository.countDemands("0"+i, (int) request.getSession().getAttribute("user"));
		}
		for(int i=10; i <= 12; i++) {
			data[i - 1] = demandRepository.countDemands(i+"", (int) request.getSession().getAttribute("user"));
		}
		model.addAttribute("label", months);
		model.addAttribute("data", data);
		
		return "DashBoardUser";
	}
}
