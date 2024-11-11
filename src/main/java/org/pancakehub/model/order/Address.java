package org.pancakehub.model.order;

import org.pancakehub.model.exception.InvalidAddressException;

public record Address (int building, int room) {
  public Address {
    if (building < 1 || room < 1) {
      throw new InvalidAddressException("Building and room must be greater than 0.");
    }
  }
}
