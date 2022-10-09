package exception;

public class EquipmentException extends Exception {
    public EquipmentException(String message) {
        super(message);
    }

    public EquipmentException(Throwable cause) {
        super(cause);
    }
}