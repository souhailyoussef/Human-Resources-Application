package scripts



class Resultat {
    float january = 0
    float february = 0
    float march = 0
    float april = 0
    float may = 0
    float june = 0
    float july = 0
    float august = 0
    float september = 0
    float october = 0
    float november = 0
    float december = 0
    float sum = 0

    def aggregate() {
        this.sum = this.january + february + march + april + may + june + july + august + september + october + november + december;
    }
}
