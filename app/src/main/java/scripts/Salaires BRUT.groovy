package scripts

url = "http://localhost:8080/dashboard/salaries"
url2 = "http://localhost:8080/dashboard/currency_rate"
httpClient = new HttpClient()

try {
    salaires =  httpClient.sendGetRequest(url)
    println("salaires : " + salaires)

    currency_rates =  httpClient.sendGetRequest(url2)
    println("currency_rates : " + currency_rates)

    var res = [0,0,0,0,0,0,0,0,0,0,0,0]
    for (def salaire_employee : salaires) {

        for (def i = 1; i < 13; i++)
        {
            res[i] = res[i] +  (currency_rates[i-1] * salaire_employee[i])
        }


    }
    println(res)
}
catch (Exception exception)  {
    println(exception.getMessage())
}

