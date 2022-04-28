package scripts

url="http://localhost:8080/dashboard/tjm/1"
url2="http://localhost:8080/dashboard/workdays/1"
httpClient=new HttpClient()

try {
    tjm = httpClient.sendGetRequest(url)
    println("TJM : " + tjm)

    workdays = httpClient.sendGetRequest(url2)
    println("workdays : " + workdays.getClass())

    var res = []
    for (int i in 0..11) {
        res.add(tjm[i]*workdays[i])
    }
    println(res)
}
catch (Exception exception)  {
    println(exception.getMessage())
}