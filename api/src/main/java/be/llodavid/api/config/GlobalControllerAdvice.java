package be.llodavid.api.config;

import be.llodavid.service.CustomerService;
import be.llodavid.service.exceptions.DoubleEntryException;
import be.llodavid.service.exceptions.OrderoException;
import be.llodavid.service.exceptions.PaymentNotReceivedException;
import be.llodavid.service.exceptions.UnknownResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice(basePackages = {"be.llodavid"})
public class GlobalControllerAdvice {
    private final static Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    @ExceptionHandler(UnknownResourceException.class)
    public ResponseEntity<String> handleUnknownIdException(final UnknownResourceException exception) {
        LOGGER.log(Level.WARNING,"ERROR: " + exception.getMessage());
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoubleEntryException.class)
    public ResponseEntity<String> handleDoubleEntryException(final DoubleEntryException exception) {
        LOGGER.log(Level.WARNING,"ERROR: " + exception.getMessage());
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotReceivedException.class)
    public ResponseEntity<String> handlePaymentNotReceivedExceptionException(final PaymentNotReceivedException exception) {
        LOGGER.log(Level.WARNING,"ERROR: " + exception.getMessage());
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(OrderoException.class)
    public ResponseEntity<String> handleGeneralOrderoException(final OrderoException exception){
        LOGGER.log(Level.WARNING,"ERROR: " + exception.getMessage());
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneralException(final RuntimeException exception){
        LOGGER.log(Level.SEVERE,"ERROR: " + exception.getMessage());
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
