package com.pima.crowdlending.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pima.crowdlending.entities.Demande;
import com.pima.crowdlending.entities.Material;
import com.pima.crowdlending.repositories.DemandeRepository;
import com.pima.crowdlending.repositories.MaterialRepository;
import com.pima.crowdlending.repositories.UserRepository;

import javassist.bytecode.stackmap.TypeData.ClassName;


//This file is for controlling material

@Controller
@CrossOrigin("*")
@RequestMapping("/material")
public class MaterialController {
	private static final Logger LOGGER = Logger.getLogger( MaterialController.class.getName() );

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private DemandeRepository demandeRepository;
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/newMaterialForm")
	public String newMaterialForm(Model model) {
		model.addAttribute("material", new Material());
		return "NewMaterial";
	}

	@PostMapping("/newMaterial")
	public String newMaterial(@ModelAttribute("material") Material material, HttpServletRequest request) {
		material.setStatus(false);
		int userID = (int) request.getSession().getAttribute("user");
		material.setOwner(userRepo.getOne(userID));
		
		materialRepository.save(material);
		return "redirect:/material/getAll";
	}

	@GetMapping("/getAll")
	public String getAll(Model model) {
		
		model.addAttribute("materials", materialRepository.findValidatedDispo());
		return "Materials";
	}

	@GetMapping("/getByFilter")
	public String getByFilter(@RequestParam("filter") String filter, Model model) {
		model.addAttribute("materials", materialRepository.findByFilter(filter));
		return "Materials";
	}
	@GetMapping("/searchLandedMaterial")
	public String searchLandedMaterial(@RequestParam("filter") String filter, Model model) {
		model.addAttribute("materials", materialRepository.findByFilter(filter));
		return "landedMaterials";
	}
	@PostMapping("/landMaterial")
	public String landMaterial(@RequestParam("idMat") Integer idMat, Model model, HttpServletRequest request) {
		Optional<Material>  mat = materialRepository.findById(idMat);
		model.addAttribute("mat",mat.get());
		if(null != mat.get()) {
			int userID = (int) request.getSession().getAttribute("user");
			mat.get().setStatus(false);
			mat.get().setOwner(userRepo.getOne(userID));
			System.out.println(mat.get().toString());
			Demande demande = new Demande();
			demande.setMaterial(mat.get());
			demande.setStatus(0);
			demande.setDatePrevuRetour(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			materialRepository.save(mat.get());
			
			demande.setLender(userRepo.getOne(userID));
			demandeRepository.save(demande);
			if(mat.get().isStatus() ==false) {
				model.addAttribute("msg","Félicitations, vous avez prêté un article (" + (mat.get()).getTitle() + ")" );
				model.addAttribute("policy","par notre politique, vous êtes obligé de retourner le matériel que vous avez prêté dans X jours que vous avez spécifié dans le formulaire");
			}else {
				model.addAttribute("msg",(mat.get()).getTitle() );
			}
		}else {
			model.addAttribute("msg", "Le matériel n'existe pas !!");
		}
		return "landingMaterialOperation";
	}

	@GetMapping("/returnlandMaterial")
	public String returnLandMaterial(@RequestParam("idMat") Integer id,Model model,HttpServletRequest request) {
		Optional<Material> m =  materialRepository.findById(id);
		System.out.println(id);

		model.addAttribute("m",m.get());
		if(null!=m.get()) {
			List<Demande> ls =demandeRepository.findDemandesByUser((int) request.getSession().getAttribute("user"));
			for (Demande dmd : ls) {
				if(dmd.getStatus() == 1 && dmd.getMaterial().getId() == id) {
					dmd.setStatus(3);
					demandeRepository.save(dmd);
				}
			}
			m.get().setStatus(true);
			materialRepository.save(m.get());



			if(m.get().isStatus()==false) {

				model.addAttribute("msg", "Vous avez retourné le matériel "+ (m.get()).getTitle());
			}

		}
		model.addAttribute("materials", materialRepository.findLandedConfirmedMaterials((int) request.getSession().getAttribute("user")));

		return "returnMaterial";
	}


	@GetMapping("/returnMaterial")
	public String returnMatDashboard(Model model,HttpServletRequest request) {
		// hnaya
		model.addAttribute("materials", materialRepository.findLandedConfirmedMaterials((int) request.getSession().getAttribute("user")));
		return "returnMaterial";
	}
	@PostMapping("/validateDemande")
	public String validateDemande(@RequestParam("idDem") Integer idDem, Model model) {
		Optional<Demande>  demande = demandeRepository.findById(idDem);
		demande.get().getMaterial().setStatus(true); // material indisponible pour la location
		model.addAttribute("dem",demande.get());

		if(null != demande.get()) {
			if(demande.get().getStatus() == 1) {
				demande.get().setStatus(0);
			}else {
				demande.get().setStatus(1);
			}
			demande.get().getMaterial().setStatus(false);
			demande.get().getMaterial().setValidateAddMaterial(true);
			
			demande.get().setId(idDem);

			demandeRepository.save(demande.get());

		}else {
			model.addAttribute("msg", "Cette demande ne peut pas etre valider!!");
			return "errno";
		}
		List<Demande> dmds = demandeRepository.findUnvalidatedDemandes();
		Collections.reverse(dmds);
		model.addAttribute("demandes", dmds);

		return "allCmds";
	}
	@GetMapping("/getUnvalidatedCmds")
	public String getAllDemandes(Model model) {
		List<Demande> dmds = demandeRepository.findUnvalidatedDemandes();
		Collections.reverse(dmds);
		model.addAttribute("demandes", dmds);
		return "ValidateCmd";
	}
	@GetMapping("/getValidateCmds")
	public String getValidateCmds(Model model) {
		List<Demande> dmds = demandeRepository.findValidatedDemandes();
		Collections.reverse(dmds);
		model.addAttribute("demandes", dmds);
		return "ValidateCmd";
	}


	@GetMapping("/getAllCmds")
	public String getAllCmds(Model model) {
		List<Demande> dmds = demandeRepository.findAll();
		Collections.reverse(dmds);
		model.addAttribute("demandes",dmds);
		return "allCmds";
	}
	/*
	 * 
	 * En tant qu'administrateur, je veux changer l'état d'un objet disponnible ou pas pour réagir en cas de problème.
	 */
	
	// rediction a la page du la ou il y a les options pour changer l'etat d'un material
	@GetMapping("/changesAvailableStat")
	public String changesAvailableStat(Model model) {
		model.addAttribute("materials", materialRepository.findAll());
		return "productStatePanel";
	}
	// filtrer materials by name
	@GetMapping("/filterObjectToChangeStat")
	public String filterObjectToChangeStat(@RequestParam("filter") String filter, Model model) {
		model.addAttribute("materials", materialRepository.findByFilter(filter));
		return "productStatePanel";
	}

	// changer l'etat d'un materiel en stock
	@PostMapping("/changeState")
	public String changeState(@RequestParam("idMat") Integer idMat, Model model) {
		
		Optional<Material>  mat = materialRepository.findById(idMat);
		model.addAttribute("mat",mat.get());
		
		if(null != mat.get()) {
			LOGGER.log( Level.FINE, "changing status of Material id = {0} from "+mat.get().isStatus()+" to " + !mat.get().isStatus(), mat.get().getId() );

				mat.get().setStatus(!mat.get().isStatus());
				
			materialRepository.save(mat.get());

		}
		model.addAttribute("materials", materialRepository.findAll());

		return "productStatePanel";
	}
	
	/*
	 * En tant qu'administrateur, je veux valider l'ajout d'un objet pour éviter les objets inutiles.
	 */
	
	// get all unvalidatet materials
	@GetMapping("/getUnvalidatedMaterials")
	public String getUnvalidatedMaterials(Model model) {
		model.addAttribute("materials", materialRepository.findUnvalidatedMaterials());
		return "unvalidatedMaterials";
	}
	
	
	// filtrer  unvalidated materials by name
		@GetMapping("/filterUnvalidatedMaterials")
		public String filterUnvalidatedMaterials(@RequestParam("filter") String filter, Model model) {
			model.addAttribute("materials", materialRepository.findUnvalidatedByNameOrDesc(filter));
			return "unvalidatedMaterials";
		}
	//validateMaterial
		@PostMapping("/validateMaterial")
		public String validateMaterial(@RequestParam("idMat") Integer idMat, Model model) {
			
			Optional<Material>  mat = materialRepository.findById(idMat);
			model.addAttribute("mat",mat.get());
			
			if(null != mat.get()) {
				LOGGER.log( Level.FINE, "Validating  Material id = {0} ", mat.get().getId() );

				// materiel valder et dispo
					mat.get().setValidateAddMaterial(true);
					mat.get().setStatus(true);

				materialRepository.save(mat.get());

			}
			model.addAttribute("materials", materialRepository.findUnvalidatedMaterials());

			return "unvalidatedMaterials";
		}
		
		
		
}
