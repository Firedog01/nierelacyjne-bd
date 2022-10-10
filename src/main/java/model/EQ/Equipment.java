package model.EQ;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "EQ")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Equipment {

    @Version
    protected int version; //FIXME not sure

    @Id
    @Column(name = "id", unique = true)
    @NotEmpty
    private int id; //FIXME I'm worried about this type of id, because it's just created based on number in list

    @Column(name = "name")
    @NotNull
    private String name;

    @NotEmpty
    @Column(name = "bail")
    private double bail;
    @NotEmpty
    @Column(name = "firstDayCost")
    private double firstDayCost;

    @NotEmpty
    @Column(name = "nextDayCost")
    private double nextDaysCost;

    @NotEmpty
    @Column(name = "archive")
    private boolean archive;

    @Column(name = "description")
    private String description;

    @NotEmpty
    @Column(name = "missing")
    private boolean missing;

    public Equipment(double firstDayCost, double nextDaysCost, double bail, String name, int id) {

        this.firstDayCost = firstDayCost;
        this.nextDaysCost = nextDaysCost;
        this.bail = bail;
        this.name = name;
        this.archive = true;
        this.description = null;
        this.id = id;
        this.missing = false;
    }

    public Equipment() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Equipment{");
        sb.append("firstDayCost=").append(firstDayCost);
        sb.append(", nextDaysCost=").append(nextDaysCost);
        sb.append(", bail=").append(bail);
        sb.append(", name='").append(name).append('\'');
        sb.append(", archive=").append(archive);
        sb.append(", description='").append(description).append('\'');
        sb.append(", id=").append(id);
        sb.append(", missing=").append(missing);
        sb.append('}');
        return sb.toString();
    }

    public double getFirstDayCost() {
        return firstDayCost;
    }

    public double getNextDaysCost() {
        return nextDaysCost;
    }

    public double getBail() {
        return bail;
    }

    public String getName() {
        return name;
    }

    public boolean isArchive() {
        return archive;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setFirstDayCost(double firstDayCost) {
        this.firstDayCost = firstDayCost;
    }

    public void setNextDaysCost(double nextDaysCost) {
        this.nextDaysCost = nextDaysCost;
    }

    public void setBail(double bail) {
        this.bail = bail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }
}
