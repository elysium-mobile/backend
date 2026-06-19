package pe.edu.upc.soft.work.platform.payment.service.domain.model.valueobjects;

/**
 * Enumeration representing the status of a Payment transaction
 */
public enum PaymentStatus {


  PENDING(1),
  SUCCEEDED(2),
  FAILED(3),
  REFUNDED(4);

  private final int value;

  PaymentStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static PaymentStatus fromValue(int value) {
    for (PaymentStatus status : PaymentStatus.values()) {
      if (status.value == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("[PaymentStatus] Invalid value for PaymentStatus: " + value);
  }

}
