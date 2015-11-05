#newHybrid

### 1. Some changes need to be made to OS to make sure the run of newhybrid.

>1.1. change system's limited number of open files by system command *ulimit*


###2. newhybrid constraints now

>2.1 supported data types are *tinyint,smallint,int,bigint,decimal,float,varchar,timestamp*

>2.2 *PRIMARY KEY* is the only table constraint now

>2.3 Every column in table must be *not null*

>2.4 all names in the DDL must not contain whitespace
