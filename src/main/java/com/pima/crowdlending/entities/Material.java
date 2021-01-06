package com.pima.crowdlending.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//Material entity
@Entity
public class Material {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	private String description;
	private String imagePath;
	@ManyToOne
	private User owner;
	private boolean status;	//disponible ou non disponnible
	private boolean validateAddMaterial;//si l'ajout du materiel est validé ou non par l'administrateur 
	@OneToMany(cascade=CascadeType.ALL, targetEntity=Demande.class)
	@JoinColumn
	private List<Demande> demandes ;
	@OneToMany(cascade=CascadeType.ALL, targetEntity=Operation.class)
	@JoinColumn
	private List<Operation> operations;

	public Material() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isValidateAddMaterial() {
		return validateAddMaterial;
	}

	public void setValidateAddMaterial(boolean validateAddMaterial) {
		this.validateAddMaterial = validateAddMaterial;
	}

	public List<Demande> getDemandes() {
		return demandes;
	}

	public void setDemandes(List<Demande> demandes) {
		this.demandes = demandes;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}
