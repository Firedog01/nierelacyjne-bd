package repository.codec;

import net.bytebuddy.asm.Advice;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.joda.time.LocalDateTime;

public class LocalDateTimeCodec implements Codec<LocalDateTime> {

    private Codec<String> stringCodec;
    public LocalDateTimeCodec(CodecRegistry registry) {
        stringCodec = registry.get(String.class);
    }

    @Override
    public LocalDateTime decode(BsonReader bsonReader, DecoderContext decoderContext) {
        System.out.println("decde local");
        String dateTimeString = stringCodec.decode(bsonReader, decoderContext);
        return LocalDateTime.parse(dateTimeString);
    }

    @Override
    public void encode(BsonWriter bsonWriter, LocalDateTime localDateTime, EncoderContext encoderContext) {
        System.out.println("encode local " + localDateTime.toString());
        String dateTimeString = localDateTime.toString();
        stringCodec.encode(bsonWriter, dateTimeString, encoderContext);
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }
}
