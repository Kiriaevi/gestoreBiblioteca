package entities;

public enum Stato {
    LETTO, DA_LEGGERE, IN_LETTURA;
    public static Stato fromStringToStato(String input) {
        String s = input.toUpperCase();
        if(s.equals("DA LEGGERE"))
            return Stato.DA_LEGGERE;
        if (s.equals("IN LETTURA"))
            return Stato.IN_LETTURA;
        if(s.equals("LETTO"))
            return Stato.LETTO;
        return null;
    }
}
