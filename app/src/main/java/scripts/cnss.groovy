package scripts

url = "http://localhost:8081/dashboard/salaries/cdi"

url2="http://localhost:8081/dashboard/node/parameters/16"

url3="http://localhost:8081/dashboard/currency_rate"

url4="http://localhost:8081/dashboard/node/values/16"

httpClient = new HttpClient()
salaires_cdi = httpClient.sendGetRequest(url) //charger la liste des salaires de type CDI
parametre_cnss = httpClient.sendGetRequest(url2) //recupèrer le paramètre de la rubrique cnss (17.07%)
tauxDevise = httpClient.sendGetRequest(url3) //charger le taux Euro/TND par mois

ArrayList<Double> cnss = []
for (int i=0;i<12;i++) {
    //formule rubrique cnss = ( salairesCDI * 17.07% ) / 3.3
    var valeurMensuelle = Math.round((salaires_cdi[i]*parametre_cnss/100) / tauxDevise[i])
    cnss.add(valeurMensuelle)
}

httpClient.sendPostRequest(url4,cnss) //enregistrer les valeurs de la rubrique cnss dans la base de données


