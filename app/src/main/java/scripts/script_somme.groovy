package scripts

import com.example.app.service.FileDBService
import com.example.app.service.FileDBServiceImpl
import org.hibernate.cfg.Environment

x=2
y=3
println(x+y);
String relativePath=System.getProperty("user.dir").concat('\\src\\main\\java\\scripts');


GroovyShell gs = new GroovyShell( binding )
//gs.evaluate(new File(relativePath.concat("\\script_test.groovy")))

