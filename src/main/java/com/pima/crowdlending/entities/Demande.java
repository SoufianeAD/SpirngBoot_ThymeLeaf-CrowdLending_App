package com.pima.crowdlending.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// Demande Entity
@Entity
public class Demande {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int status; // 1=encour , 2= refusée ; 3 = validée
	private Date datePrevuRetour;
	@ManyToOne
	private User lender;
	@ManyToOne
	private Material material;

	public Demande() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDatePrevuRetour() {
		return datePrevuRetour;
	}

	public void setDatePrevuRetour(Date datePrevuRetour) {
		this.datePrevuRetour = datePrevuRetour;
	}

	public User getLender() {
		return lender;
	}

	public void setLender(User lender) {
		this.lender = lender;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
}
