package be.llodavid.domain.customers;

import be.llodavid.domain.RepositoryRecord;
import be.llodavid.util.exceptions.OrderoException;
import be.llodavid.util.helperClasses.EmailValidation;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="CUSTOMERS")
public class Customer {

    @Id
    @SequenceGenerator(name = "customers_generator", sequenceName = "customers_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_generator")
    @Column(name = "CUSTOMER_ID")
    private long customerId;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "PHONE_NUMBER")
    private String phonenumber;
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "EMAIL")
    private String email;
    @Embedded
    private Address address;

    private Customer(CustomerBuilder customerBuilder) {
        this.lastName = customerBuilder.lastName;
        this.address = customerBuilder.address;
        this.firstName = customerBuilder.firstName;
        this.phonenumber = customerBuilder.phonenumber;
        this.email = customerBuilder.email;
    }

    public Customer() {
    }

//    public void setId(int valueId) {
//        this.customerId = valueId;
//    }
    public long getId() {
        return customerId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\tlastName='").append(lastName).append('\'');
        sb.append(", \n\tfirstName='").append(firstName).append('\'');
        sb.append(", \n\tcity='").append(address.toString()).append('\'');
        sb.append(", \n\temail='").append(email).append('\'');
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
        private String email;

        public Customer build() {
            if (allFieldsSet()) {
                return new Customer(this);
            }
            throw new OrderoException("Please provide all the necessary arguments.");
        }

        public static CustomerBuilder buildCustomer() {
            return new CustomerBuilder();
        }

        private boolean allFieldsSet() {
            return (isFilledIn(lastName)
                    && isFilledIn(firstName)
                    && (isFilledIn(phonenumber)
                    || isFilledIn(email)))
                    && address!=null;
        }

        private boolean isFilledIn(String field) {
            return field != null && !field.isEmpty();
        }

        public CustomerBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder withPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
            return this;
        }

        public CustomerBuilder withEmail(String email) {
            if (!EmailValidation.isValidEmail(email)) {
                throw new OrderoException("Please provide a valid E-mail address!");
            }
            this.email = email;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomerBuilder that = (CustomerBuilder) o;
            return Objects.equals(lastName, that.lastName) &&
                    Objects.equals(firstName, that.firstName) &&
                    Objects.equals(address, that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lastName, firstName, address);
        }
    }
}
