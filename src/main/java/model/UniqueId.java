package model;

import java.io.Serializable;
import java.util.UUID;

public class UniqueId implements Serializable {

    private String uniqueID;

    public UniqueId() {
        this.uniqueID = UUID.randomUUID().toString();
    }
}