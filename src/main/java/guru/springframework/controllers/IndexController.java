package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;

@Controller
public class IndexController {

	CategoryRepository catRepo;
	UnitOfMeasureRepository uomRepo;




	@RequestMapping({"","/", "/index"})
	public String getIndexPage() {
		
		return "index";
	}
	
}
