DAO FOR LDAP
------------------

A light weight library for multitenancy authentication and LDAP CURED operations.  

* Dynamically configurable.
* Provides authentication services.
* Provides CRUD operation for Tenants, Roles, and Users.
* Provide bulk importer to import data from CVS file to LDAP.
* Layered architecture provide flexibility for extension. 
* Comprehensive unit tests via embedded LDAP.


TO RUN UNIT TESTS
------------------
There are two way to run unit test

1- with embedded LDAP.
	- mvn test -Dtest="AllTests".
	- This will launch Apache embedded LDAP server and will do the bootstrapping.
	- You can use ApacheDs studio or Eclipse plugin to browse the server contents 
	  during the execution of the tests. Followings are configurations for the connection: 
	  
	  	Hostname="localhost"
		Port="1024"
		encryption method="no encryption"
		Authentication Method = "Simple"
		Authentication Parameters 
		Bind DN = "uid=admin,ou=system"
		Bind Password = "secret"
	  

2- Stand alone ApacheDs LDAP server. 
	- Create partition into your server. Configuration is provided in "easy_ldap_server.xml".
	- Import "easy-ldap.ldif" schema into your LDAP server.
	- Open "ldap-context-test.properties". Change the server port to 10389  
	- mvn test -Dtest="AllTests"


MAVEN
--------
mvn install:install-file -Dfile=dao-for-ldap-1.0.jar -DgroupId=org.easy -DartifactId=dao-for-ldap -Dversion=1.0 -Dpackaging=jar
mvn test -Dtest=AllTests
mvn clean pacakge -Dtest=AllTests	
mvn clean pacakge -DskipTests=true	
	
	
LICENSE
---------
    Copyright © 2011 Aftab Mahmood
  
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 

 
 REFERENCES
 ----