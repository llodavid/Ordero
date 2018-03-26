package be.llodavid.domain;

import java.util.Objects;

public class Customer {

    private int customerId;
    private String lastName;
    private String firstName;
    private String phonenumber;
    private Email email;
    private Address address;

    private Customer(CustomerBuilder customerBuilder) {
        this.lastName = customerBuilder.lastName;
        this.address = customerBuilder.address;
        this.firstName = customerBuilder.firstName;
        this.phonenumber = customerBuilder.phonenumber;
        this.email = customerBuilder.email;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\n\tlastName='").append(lastName).append('\'');
        sb.append(", \n\tfirstName='").append(firstName).append('\'');
        sb.append(", \n\tcity='").append(address.toString()).append('\'');
        sb.append(", \n\temail='").append(email.geteMail()).append('\'');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    public static class CustomerBuilder {
        private String lastName;
        private String firstName;
        private Address address;
        private String phonenumber;
        private Email email;

        public static CustomerBuilder BuildAPerson() {
            return new CustomerBuilder();
        }

        public Customer Build() {
            if (allFieldsSet()) {
                return new Customer(this);
            }
            throw new IllegalArgumentException("Please provide all the necessary arguments.");
        }

        private boolean allFieldsSet() {
            return (!lastName.equals("")
                    && !firstName.equals("")
                    && (!phonenumber.equals("")
                    || !email.geteMail().equals("")));
        }

        public CustomerBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder withAddress(String Address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder withPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
            return this;
        }

        public CustomerBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }
    }
}
