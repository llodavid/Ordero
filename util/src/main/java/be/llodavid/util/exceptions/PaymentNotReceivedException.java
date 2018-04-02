package be.llodavid.util.exceptions;

import java.math.BigDecimal;

public class PaymentNotReceivedException extends OrderoException {
    public PaymentNotReceivedException(BigDecimal amountToPay) {
        super(String.format("We have not yet received payment for your order.\n The total amount to pay is %s euro", amountToPay.toPlainString()));
    }
}
