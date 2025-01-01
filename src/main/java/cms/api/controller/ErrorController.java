package cms.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import cms.api.exceptions.BadRequestException;
import cms.api.exceptions.InternalErrorException;
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
        model.addAttribute("errorMessage", "Desculpe! Ocorreu um erro inesperado: "+errorMessage);
        
        return "error";
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String noresource(final NoResourceFoundException e, final Model model) 
    {
    	log.warn(e.toString());
        model.addAttribute("errorMessage", "Recusro Inesistente: "+e.getMessage());
        
        return "error";
    }
    
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequessst(final BadRequestException e, final Model model) 
    {
    	log.warn(e.toString());
        model.addAttribute("errorMessage", "Requisição Inválida: "+e.getMessage());
        
        return "error";
    }
    
    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String badRequessst(final InternalErrorException e, final Model model) 
    {
    	log.error(e.toString());
        model.addAttribute("errorMessage", "Desculpe! Ocorreu uma falha inesperada: "+e.getMessage());
        
        return "error";
    }
}