package org.pancakehub.model.order;

import org.junit.jupiter.api.Test;
import org.pancakehub.model.exception.InvalidAddressException;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    @Test
    void GivenValidBuildingAndRoom_WhenCreatingAddress_ThenAddressIsCreatedSuccessfully_Test() {
        // given
        // when
        Address address = new Address(5, 10);
        // then
        assertEquals(5, address.building());
        assertEquals(10, address.room());
    }

    @Test
    void GivenInvalidBuilding_WhenCreatingAddress_ThenInvalidAddressExceptionIsThrown_Test() {
        // given
        // when
        // then
        assertThrows(InvalidAddressException.class, () -> {
            new Address(0, 10);
        });
    }

    @Test
    void GivenInvalidRoom_WhenCreatingAddress_ThenInvalidAddressExceptionIsThrown_Test() {
        // given
        // when
        // then
        assertThrows(InvalidAddressException.class, () -> {
            new Address(5, 0);
        });
    }
}