package be.llodavid.domain.helperClass;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AddressBuilderUnitTest {

    @Test
    public void buildAddress_givenAllRequiredFields_returnAddressObject() {
        Address newAdress = Address.AddressBuilder.buildAddress()
                .withStreet("steenweg")
                .withHousenumber("53")
                .withCity("Welle")
                .withZipcode("9473")
                    .withCountry("Belgium")
                .build();
        Assertions.assertThat(newAdress.toString()).isEqualTo("\tcity='Welle', \n" +
                "\tstreet='steenweg', \n" +
                "\thousenumber='53', \n" +
                "\tzipcode='9473'");
    }

    @Test
    public void buildAddress_givenMissingRequiredFields_throwsException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->
                Address.AddressBuilder.buildAddress()
                .withStreet("steenweg")
                .withHousenumber("53")
                .withCity("Welle")
                .build()).withMessage("Please provide all the necessary arguments for the Address.");
    }

}