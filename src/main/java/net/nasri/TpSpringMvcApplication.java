package net.nasri;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.nasri.dao.EtudiantRepository;
import net.nasri.entities.Etudiant;

@SpringBootApplication
public class TpSpringMvcApplication {

	public static void main(String[] args) throws ParseException {
		ApplicationContext ctx = SpringApplication.run(TpSpringMvcApplication.class, args);
		EtudiantRepository etudiantrepository = ctx.getBean(EtudiantRepository.class);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//etudiantrepository.save(new Etudiant("Nasri", df.parse("1993-07-05"), "nasri@gmail.com", "nasri.jpg"));
		//etudiantrepository.save(new Etudiant("Med", df.parse("2000-05-05"), "med@gmail.com", "med.jpg"));
		//etudiantrepository.save(new Etudiant("Ahmed", df.parse("1993-04-05"), "ahmed@gmail.com", "ahmed.jpg"));
		
		Page<Etudiant> etds = etudiantrepository.findAll(new PageRequest(0, 5));
		etds.forEach(e->System.out.println(e.getNom()));
		
		Page<Etudiant> etds1 = etudiantrepository.chercherEtudiants("%A%",new PageRequest(0, 5));
		etds1.forEach(e->System.out.println(e.getNom()));

	}
}
