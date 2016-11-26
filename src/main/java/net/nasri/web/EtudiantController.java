package net.nasri.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.nasri.dao.EtudiantRepository;
import net.nasri.entities.Etudiant;

@Controller
@RequestMapping(value="/Etudiant")
public class EtudiantController {
	
	@Autowired
	private EtudiantRepository etudiantRepository;
	@Value("${dir.images}")
	private String imageDir;
	@RequestMapping(value="/Index")
	public String Index(Model model, @RequestParam(name="page",defaultValue="0")int page, @RequestParam(name="motCle",defaultValue="")String motCle)
	{
		Page<Etudiant> etd = etudiantRepository.chercherEtudiants("%"+motCle+"%", new PageRequest(page, 5));
		int pagesCount = etd.getTotalPages();
		int[] pages = new int[pagesCount];
		for(int i=0;i<pagesCount;i++) pages[i]=i;
		model.addAttribute("pages",pages);
		model.addAttribute("pageEtudiants",etd);
		model.addAttribute("pagecourant",page);
		model.addAttribute("motCle",motCle);

		return "etudiant";
	}
	
	@RequestMapping(value="/Form", method=RequestMethod.GET)
	public String FormEtudiant(Model model)
	{
		model.addAttribute("etudiant",new Etudiant());
		return "FormEtudiant";
	}

	@RequestMapping(value="/saveEtudiant", method=RequestMethod.POST)
	public String SaveEtudiant(@Valid Etudiant et,BindingResult bindingresult,@RequestParam(name="picture")MultipartFile photo ) throws IllegalStateException, IOException
	{
		if(bindingresult.hasErrors())
		{
			return "FormEtudiant";
		}
		if(!photo.isEmpty())
		{
			et.setPhoto(photo.getOriginalFilename());
			photo.transferTo(new File(imageDir+photo.getOriginalFilename()));
		}
			etudiantRepository.save(et);
			if(!photo.isEmpty())
			{
				et.setPhoto(photo.getOriginalFilename());
				photo.transferTo(new File(imageDir+et.getId()));
			}
			return "redirect:/Etudiant/Index";		
	}
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String SaveEtudiant(@RequestParam(name="picture")MultipartFile photo ) throws IllegalStateException, IOException
	{
		
		if(!photo.isEmpty())
		{		
			photo.transferTo(new File(imageDir+photo.getOriginalFilename()));
		}
			
		
			return "redirect:/Etudiant/Index";		
	}
	
	@RequestMapping(value="/updateEtudiant", method=RequestMethod.POST)
	public String UpdateEtudiant(@Valid Etudiant et,BindingResult bindingresult,@RequestParam(name="picture")MultipartFile photo ) throws IllegalStateException, IOException
	{
		if(bindingresult.hasErrors())
		{
			return "EditEtudiant";
		}
		if(!photo.isEmpty())
		{
			et.setPhoto(photo.getOriginalFilename());
			photo.transferTo(new File(imageDir+photo.getOriginalFilename()));
		}
			etudiantRepository.save(et);
			if(!photo.isEmpty())
			{
				et.setPhoto(photo.getOriginalFilename());
				photo.transferTo(new File(imageDir+et.getId()));
			}
			return "redirect:/Etudiant/Index";		
	}
	
	
	@RequestMapping(value="/getPhoto",produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long id) throws FileNotFoundException, IOException
	{
		File f = new File(imageDir+id);
		return IOUtils.toByteArray(new FileInputStream(f));
		
	}
	
	@RequestMapping(value="/supprimer")
	public String supprimer(Long id)
	{
		etudiantRepository.delete(id);
		return "redirect:Index";
		
	}

	@RequestMapping(value="/edit")
	public String edit(Long id,Model model)
	{
		Etudiant etudiant = etudiantRepository.getOne(id);
		model.addAttribute("etudiant",etudiant);
		return "EditEtudiant";
		
	}

}
