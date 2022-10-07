package model;

public class Client {
    private String firstName;
    private String lastName;
    private String ID;

    private idType idType;
    private boolean archive;

    private Address address;

    //TODO current rents? List?


    public Client(String firstName, String lastName, Address address, String ID, model.idType idType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.archive = false; //FIXME po co to jest???
        this.ID = ID;
        this.idType = idType;
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("model.Client{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", ID='").append(ID).append('\'');
        sb.append(", archive=").append(archive);
        sb.append('}');
        return sb.toString();
    }

    //TODO Ogólnie są gettery i settery, ale nie wszystkie są potrzebne więc idk, można wywalić?

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID() {
        return ID;
    }

    public model.idType getIdType() {
        return idType;
    }

    public boolean isArchive() {
        return archive;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setIdType(model.idType idType) {
        this.idType = idType;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
}
