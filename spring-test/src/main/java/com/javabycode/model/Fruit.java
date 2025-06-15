package com.javabycode.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fruit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fruit implements Serializable {
	
	private static final long serialVersionUID = 691914827490230925L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, name = "color")
	private String produceBy; // compatível com migration

}