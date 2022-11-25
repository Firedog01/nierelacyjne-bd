package mgd.EQ;

import com.fasterxml.jackson.annotation.JsonProperty;
import mgd.UniqueIdMgd;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "lens")
public class LensMgd extends EquipmentMgd {

    @BsonProperty("focal_length")
    @JsonProperty("focal_length")
    private String focalLength;

    @BsonCreator
    public LensMgd(@BsonProperty("_id") @JsonProperty("_id") UniqueIdMgd entityId,
                     @BsonProperty("name") @JsonProperty("name") String name,
                     @BsonProperty("bail") @JsonProperty("bail") double bail,
                     @BsonProperty("first_day_cost") @JsonProperty("first_day_cost") double firstDayCost,
                     @BsonProperty("next_day_cost") @JsonProperty("next_day_cost") double nextDaysCost,
                     @BsonProperty("archive") @JsonProperty("archive") boolean archive,
                     @BsonProperty("description") @JsonProperty("description") String description,
                     @BsonProperty("missing") @JsonProperty("missing") boolean missing,
                     @BsonProperty("focal_length") @JsonProperty("focal_length") String focalLength
    ) {
        super(entityId, name, bail, firstDayCost, nextDaysCost,
                archive, description, missing);
        this.focalLength = focalLength;
    }

    @Override
    public void close() throws Exception {

    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LensMgd)) return false;
        if (!super.equals(o)) return false;

        LensMgd lensMgd = (LensMgd) o;

        return focalLength != null ? focalLength.equals(lensMgd.focalLength) : lensMgd.focalLength == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (focalLength != null ? focalLength.hashCode() : 0);
        return result;
    }
}
