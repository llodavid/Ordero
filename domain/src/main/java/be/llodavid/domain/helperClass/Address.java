package be.llodavid.domain;

public class Address {
    private String street;
    private String housenumber;
    private String zipcode;
    private String city;
    private String Country;

    public Address(AddressBuilder adressBuilder) {
        this.street=adressBuilder.street;
        this.housenumber=adressBuilder.housenumber;
        this.zipcode=adressBuilder.zipcode;
        this.city=adressBuilder.city;
        this.Country=adressBuilder.Country;
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
        return Country;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(", \n\tcity='").append(city).append('\'');
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
        private String Country;

        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withCountry(String Country) {
            this.Country = Country;
            return this;
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
    }
}
