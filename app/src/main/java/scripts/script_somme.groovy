package scripts

import com.example.app.service.FileDBService
import com.example.app.service.FileDBServiceImpl
import groovy.json.JsonSlurper
import org.hibernate.cfg.Environment


String relativePath=System.getProperty("user.dir").concat('\\src\\main\\java\\scripts');


GroovyShell gs = new GroovyShell( binding )



var res  =new Resultat();
res.january=2;
println(res.aggregate())

URL url = new URL("http://localhost:8080/api/users");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestProperty("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJFbW1hIiwicm9sZXMiOlsiQ09MTEFCT1JBVE9SIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvbG9naW4iLCJleHAiOjE2NTAzMDQ3MTF9.HGT8oC3q0ylzvHKYDwwvwxxP-ijhnXoRmEfnoJJ0ts8")
con.setRequestMethod("GET");

def getRC = con.getResponseCode()
println(getRC)







if (getRC.equals(200)) {
    def jsonSlurper = new JsonSlurper()
    print(con.getInputStream().getText())
}
//gs.evaluate(new File(relativePath.concat("\\script_test.groovy")))

