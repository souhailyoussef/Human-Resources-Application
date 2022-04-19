package scripts

import com.example.app.service.ClientService
import groovy.sql.Sql
import org.hibernate.cfg.Environment

import java.time.LocalDate
import java.time.Month

Map dbConnParams = [
        url: 'jdbc:postgresql://localhost:5432/db',
        user: 'postgres',
        password: 'password',
        driver: 'org.postgresql.Driver']

sql = Sql.newInstance(dbConnParams)

//TODO : fetch projects related to client1 (done)
int id=3;
def data_query = " SELECT * FROM\n" +
        "(SELECT project.id as project_id,client.id as client_id, contract.id as contract_id ,\n" +
        "project.name as project_name, client.name as client_name, contract.cost as contract_cost \n" +
        "FROM project, client, contract\n" +
        "WHERE  project.client_id=client.id \n" +
        "AND project.id=contract.project_id\n" +
        "\n" +
        ")\n" +
        "as t1\n" +
        "WHERE t1.client_id= ${id}"

def listOfProjects = " SELECT project_id FROM\n" +
        "(SELECT project.id as project_id,client.id as client_id, contract.id as contract_id ,\n" +
        "project.name as project_name, client.name as client_name, contract.cost as contract_cost \n" +
        "FROM project, client, contract\n" +
        "WHERE  project.client_id=client.id \n" +
        "AND project.id=contract.project_id\n" +
        "\n" +
        ")\n" +
        "as t1\n" +
        "WHERE t1.client_id=3"
//TODO : fetch employees working on those projects


//fetch tasks of those employees for those clients



try {
    println("here")
    var clientService = binding.variables.get("clientService")
    ArrayList result = fetchData(data_query)
    println(result)

}

//TODO : CALCULATE COST OF EVERY PROJECT

//TODO : - nbre de consultant
//TODO : - nbre d'heures de chaque consultant
//TODO : calcul TJM
catch (Exception e ) {
    println("exception , " + e.toString())
}


var res  =new Resultat();

println(res.aggregate())

def fetchData(String query)  {
    return sql.rows(query)
}
def fetchConsultants(String query) {
    return sql.rows(query)
}



//GroovyShell gs = new GroovyShell( binding )
//gs.evaluate(new File(relativePath.concat("\\script_test.groovy")))
/*binding.setVariable( "fileDBService", fileDBService )
GroovyShell gs = new GroovyShell( binding )
gs.evaluate(new File(relativePath.concat("\\script_test.groovy")))*/

