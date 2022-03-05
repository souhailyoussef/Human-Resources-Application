/*package scripts
  import com.example.app.domain.TreeNode
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Value

//TODO read properties file to fetch DB parameters


def sql = Sql.newInstance("jdbc:postgresql://localhost:5432/db","postgres", "password", "org.postgresql.Driver")


/*sql.eachRow("SELECT * FROM PROJECT") { rs ->
 println(rs)
}

create nodes (rubriques)
sum functions pour calculer le total
implémenter le logique de chaque rubrique séparement */
/*
TreeNode<List<Object>> rootNode = new TreeNode<>("production")
TreeNode<List<Object>> projects = new TreeNode<>("projets",rootNode,5.doubleValue())
TreeNode<List<Object>> stocks = new TreeNode<>("stocks",rootNode)
TreeNode<List<Object>> client1 = new TreeNode<>("client 1",projects,20.doubleValue())
TreeNode<List<Object>> client2 = new TreeNode<>("client 2",projects,1.2.doubleValue())
TreeNode<List<Object>> stocks_prestations = new TreeNode<>("stocks prestations",stocks)
TreeNode<List<Object>> stocks_matière = new TreeNode<>("stocks matière",stocks,5.doubleValue())



rootNode.sumNode()
projects.sumNode()

println("total = "+rootNode.getValue() )

System.out.println(rootNode.toString())
System.out.println(projects.toString())
System.out.println(stocks.toString())
System.out.println(client1.toString())
System.out.println(client2.toString())
System.out.println(stocks_prestations.toString())
System.out.println(stocks_matière.toString())


public static double[] add(double[] first, double[] second) {
 //TODO : add case for when length differs
 int length = first.length < second.length ? first.length
         : second.length;
 double[] result = new double[length];

 for (int i = 0; i < length; i++) {
  result[i] = first[i] + second[i];
 }

 return result;
}


*/