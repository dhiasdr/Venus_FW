package com.venus.test;

import com.venus.orm.annotation.Column;
import com.venus.orm.annotation.Entity;
import com.venus.orm.annotation.GeneratedValue;
import com.venus.orm.annotation.Id;
import com.venus.orm.annotation.Table;
import com.venus.orm.enum_.GenerationType;

@Entity
@Table(name = "Person3")
public class Personne3 {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "Id", notNull = true , unique= true)
	private int id;
	@Column(name = "nom", notNull = true, unique = true)
	private String nom;
	@Column(name = "prenom", notNull = true ,unique = false)
	private String Prenom;
	public Personne3() {
		
	}
	public Personne3(String nom, String prenom) {
		super();
		this.nom = nom;
		Prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return Prenom;
	}
	public void setPrenom(String prenom) {
		Prenom = prenom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
