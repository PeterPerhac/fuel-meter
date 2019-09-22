--------------------------------------------------------
--  DDL for Table VEHICLE
--------------------------------------------------------

CREATE TABLE "ADMIN"."VEHICLE"
(	"REG" CHAR(10 CHAR) COLLATE "USING_NLS_COMP",
     "MAKE" VARCHAR2(100 CHAR) COLLATE "USING_NLS_COMP",
     "MODEL" VARCHAR2(100 CHAR) COLLATE "USING_NLS_COMP",
     "COLOR" CHAR(20 CHAR) COLLATE "USING_NLS_COMP",
     "YEAR" NUMBER(12,0)
)  DEFAULT COLLATION "USING_NLS_COMP" SEGMENT CREATION DEFERRED
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  TABLESPACE "DATA" ;
--------------------------------------------------------
--  DDL for Index VEHICLE_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "ADMIN"."VEHICLE_PK" ON "ADMIN"."VEHICLE" ("REG")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
    TABLESPACE "DATA" ;
--------------------------------------------------------
--  Constraints for Table VEHICLE
--------------------------------------------------------

ALTER TABLE "ADMIN"."VEHICLE" MODIFY ("REG" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."VEHICLE" MODIFY ("MAKE" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."VEHICLE" MODIFY ("MODEL" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."VEHICLE" ADD CONSTRAINT "VEHICLE_PK" PRIMARY KEY ("REG")
    USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
        TABLESPACE "DATA"  ENABLE;

--------------------------------------------------------
--  DDL for Table READING
--------------------------------------------------------

CREATE TABLE "ADMIN"."READING"
(	"REFUEL_DATE" DATE,
     "REG" CHAR(10 CHAR) COLLATE "USING_NLS_COMP",
	"MILES" NUMBER(5,2),
	"MILEAGE" NUMBER(12,0),
	"LITERS" NUMBER(5,2),
	"COST" NUMBER(5,2)
   )  DEFAULT COLLATION "USING_NLS_COMP" SEGMENT CREATION DEFERRED
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
 NOCOMPRESS LOGGING
  TABLESPACE "DATA" ;
--------------------------------------------------------
--  DDL for Index READING_PK
--------------------------------------------------------

CREATE UNIQUE INDEX "ADMIN"."READING_PK" ON "ADMIN"."READING" ("REG", "MILEAGE")
    PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "DATA" ;
--------------------------------------------------------
--  Constraints for Table READING
--------------------------------------------------------

ALTER TABLE "ADMIN"."READING" MODIFY ("REFUEL_DATE" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" MODIFY ("REG" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" MODIFY ("MILES" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" MODIFY ("MILEAGE" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" MODIFY ("LITERS" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" MODIFY ("COST" NOT NULL ENABLE);
ALTER TABLE "ADMIN"."READING" ADD CONSTRAINT "READING_PK" PRIMARY KEY ("REG", "MILEAGE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255
  TABLESPACE "DATA"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table READING
--------------------------------------------------------

ALTER TABLE "ADMIN"."READING" ADD FOREIGN KEY ("REG")
    REFERENCES "ADMIN"."VEHICLE" ("REG") ON DELETE CASCADE ENABLE;
