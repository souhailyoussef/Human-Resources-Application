package scripts

import com.example.app.domain.FileDB
import com.example.app.domain.FileDBParams
import com.example.app.service.FileDBService
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.context.support.GenericApplicationContext
import org.springframework.web.multipart.MultipartFile

import java.nio.charset.StandardCharsets
import java.util.stream.Stream



println("hello world!")


Map dbConnParams = [
        url: 'jdbc:postgresql://localhost:5432/db',
        user: 'postgres',
        password: 'password',
        driver: 'org.postgresql.Driver']

def sql = Sql.newInstance(dbConnParams)

sql.eachRow('select * from employee') { row ->
    println "${row.id} ($row.username)"
}
fileDBService = binding.variables.get("fileDBService")
var file = fileDBService.loadFileByNodeId(4).getData()
evaluate(new String(file, StandardCharsets.UTF_8))
sql.close()

// TODO : this finally works !


/*
  import com.example.app.domain.TreeNode
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Value





/*sql.eachRow("SELECT * FROM PROJECT") { rs ->
 println(rs)
}
//def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/db","postgres", "password", "org.postgresql.Driver")

create nodes (rubriques)
sum functions pour calculer le total
implémenter le logique de chaque rubrique séparement */


