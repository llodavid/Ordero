package be.llodavid.domain.customer;

import be.llodavid.util.exceptions.OrderoException;

public class Address {
    private String street;
    private String housenumber;
    private String zipcode;
    private String city;
    private String country;

    private Address(AddressBuilder adressBuilder) {
        this.street = adressBuilder.street;
        this.housenumber = adressBuilder.housenumber;
        this.zipcode = adressBuilder.zipcode;
        this.city = adressBuilder.city;
        this.country = adressBuilder.country;
    }

    public String getStreet() {
        return street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\tcity='").append(city).append('\'');
        sb.append(", \n\tstreet='").append(street).append('\'');
        sb.append(", \n\thousenumber='").append(housenumber).append('\'');
        sb.append(", \n\tzipcode='").append(zipcode).append('\'');
        return sb.toString();
    }

    public static class AddressBuilder {
        private String street;
        private String housenumber;
        private String zipcode;
        private String city;
        private String country;

        public static AddressBuilder buildAddress() {
            return new AddressBuilder();
        }

        public Address build() {
            if (allFieldsSet()) {
                addDefaultCountryIfNotSet();
                return new Address(this);
            } else {
                throw new OrderoException("Please provide all the necessary arguments for the Address.");
            }
        }

        private void addDefaultCountryIfNotSet() {
            if (country == null) {
                country = "Belgium";
            }
        }

        private boolean allFieldsSet() {
            return street != null && housenumber != null
                    && zipcode != null && city != null;
        }

        public AddressBuilder withHousenumber(String housenumber) {
            this.housenumber = housenumber;
            return this;
        }

        public AddressBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder withZipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withCountry(String country) {
            this.country = country;
            return this;
        }
    }
}
