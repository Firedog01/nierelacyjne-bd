package model;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.Generated;
import model.EQ.Equipment;
import org.joda.time.LocalDateTime;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Rent.class)
public abstract class Rent_ extends model.AbstractEntity_ {

	public static volatile SingularAttribute<Rent, Boolean> shipped;
	public static volatile SingularAttribute<Rent, Integer> rent_id;
	public static volatile SingularAttribute<Rent, Boolean> eqReturned;
	public static volatile SingularAttribute<Rent, Equipment> equipment;
	public static volatile SingularAttribute<Rent, Client> client;
	public static volatile SingularAttribute<Rent, LocalDateTime> beginTime;
	public static volatile SingularAttribute<Rent, LocalDateTime> endTime;
	public static volatile SingularAttribute<Rent, Long> id;

	public static final String SHIPPED = "shipped";
	public static final String RENT_ID = "rent_id";
	public static final String EQ_RETURNED = "eqReturned";
	public static final String EQUIPMENT = "equipment";
	public static final String CLIENT = "client";
	public static final String BEGIN_TIME = "beginTime";
	public static final String END_TIME = "endTime";
	public static final String ID = "id";

}
