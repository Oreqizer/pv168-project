CREATE TABLE "COMPUTERS" (
    "ID" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "SLOTS" SMALLINT,
    "COOLING" SMALLINT,
    "PRICE" SMALLINT
);

CREATE TABLE "COMPONENTS" (
    "ID"     BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "PC"     BIGINT REFERENCES "COMPUTERS" ("ID"),
    "NAME"   VARCHAR(255),
    "HEAT"   SMALLINT,
    "PRICE"  SMALLINT,
    "ENERGY" SMALLINT
);