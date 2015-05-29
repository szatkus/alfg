DROP TABLE IF EXISTS "user";
CREATE TABLE "user" ("email_address" VARCHAR PRIMARY KEY  NOT NULL  UNIQUE , "password_hash" VARCHAR);
