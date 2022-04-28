package scripts

class HttpClient {
    HttpURLConnection urlConnexion
    //TODO : fix random error (repeat request 3 times if error or repeat function call until we get a result)
    def String sendPostRequest(String url, value) {

        urlConnexion = new URL(url).openConnection()
        urlConnexion.setRequestMethod("POST")
        urlConnexion.setDoOutput(true)
        urlConnexion.setRequestProperty("Content-Type", "application/json")
        try(OutputStream os = urlConnexion.getOutputStream()) {
            byte[] input = value.toString().getBytes("utf-8");
            os.write(input, 0, input.length)
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(urlConnexion.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder()
            String responseLine = null
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            println("server response code: "+urlConnexion.getResponseCode())
            println("response body: "+ response)
            return response

        }

    }
    def Object sendGetRequest(String url) {
        urlConnexion = new URL(url).openConnection()
        urlConnexion.setRequestMethod("GET")
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConnexion.getInputStream()))
        String inputLine
        StringBuffer content = new StringBuffer()
        while ((inputLine = input.readLine()) != null) {
            content.append(inputLine)
        }
                input.close()
        urlConnexion.disconnect()
        //convert http response to an object
        return (new groovy.json.JsonSlurper().parseText(content.toString()))
    }

}

