package presentation.demo.advizors;

import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView globalRintimeErrorHandling(RuntimeException e){
        System.out.println(Arrays.toString(e.getStackTrace()));
        ModelAndView view = new ModelAndView("global-errors");
        view.addObject("error","Runtime error !");
        return view;
    }

    @ExceptionHandler({PersistenceException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ModelAndView globalPersistenceErrorHandling(PersistenceException e){
        System.out.println(Arrays.toString(e.getStackTrace()));
        ModelAndView view = new ModelAndView("global-errors");
        view.addObject("error","Persistence error !");
        return view;
    }

    @ExceptionHandler({TransactionException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView globalTransactionErrorHandling(TransactionException e){
        System.out.println(Arrays.toString(e.getStackTrace()));
        ModelAndView view = new ModelAndView("global-errors");
        view.addObject("error","Transaction error !");
        return view;
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView globalIllegalErrorHandling(IllegalStateException e){
        System.out.println(Arrays.toString(e.getStackTrace()));
        ModelAndView view = new ModelAndView("global-errors");
        view.addObject("error","IllegalState error !");
        return view;
    }

}
