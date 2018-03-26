package be.llodavid.service.exceptions;

import java.math.BigDecimal;

public class PaimentException extends OrderoException {
    public PaimentException(BigDecimal amountToPay, BigDecimal amountReceived) {
        super(String.format("The total amount for the paiement is %s, we have only received %s\nPlease pay the sol"));
    }
}
