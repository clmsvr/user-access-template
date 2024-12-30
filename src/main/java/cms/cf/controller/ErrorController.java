package cms.cf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) 
    {
    	log.error(throwable.toString(),throwable);
        String errorMessage = (throwable != null ? throwable.toString() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        
        return "error";
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String noresource(final Throwable throwable, final Model model) 
    {
    	log.error(throwable.toString());
        String errorMessage = (throwable != null ? throwable.toString() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        
        return "error";
    }
}