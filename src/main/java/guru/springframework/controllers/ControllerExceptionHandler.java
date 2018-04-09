package guru.springframework.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import guru.springframework.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(NotFoundException e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception",e);
		mv.setViewName("404error");
		return mv;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NumberFormatException.class)
	public ModelAndView handleNotFound(Exception e) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception",e);
		mv.setViewName("400error");
		return mv;
	}
	
	
}
