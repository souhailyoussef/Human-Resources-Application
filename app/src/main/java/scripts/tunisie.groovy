package scripts

//formula : (salaires BRUT per month - (salary of SIVP)) / 3.3  * parametre tunisie(17.07)


url="http://localhost:8080/dashboard/node/values/12"
url2="http://localhost:8080/dashboard/node/parameters/18"
httpClient = new HttpClient()
try {
    salaires_bruts =  httpClient.sendGetRequest(url)
    println("salaires bruts : " + salaires_bruts)
    parameter =  httpClient.sendGetRequest(url2)
    println("parameter : "+ parameter)


     var res = []
     for (int i in 0..11) {
         res.add((salaires_bruts[i]*parameter/100))
     }
     println(res)
}
catch (Exception exception)  {
    println(exception.getMessage())
}



