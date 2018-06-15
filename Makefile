install:
	mvn clean install

fastinstall:
	mvn clean -DskipTests=true install

test:
	mvn clean test
	
runlocal:
	mvn spring-boot:run
