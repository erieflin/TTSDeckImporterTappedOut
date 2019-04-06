package importObjects;

public class CardDetails
{
    public enum Tag
    {
        FOIL, ALTER, COMMANDER
    }

    public enum Board
    {
        MAIN, SIDEBOARD, MAYBEBOARD, ACQUIRE;

        public static Board getFromString(String inputStr){
            if(inputStr.toLowerCase().contains("main")){
                return MAIN;
            }

            if(inputStr.toLowerCase().contains("side")){
                return SIDEBOARD;
            }

            if(inputStr.toLowerCase().contains("maybe")){
                return MAYBEBOARD;
            }

            if(inputStr.toLowerCase().contains("acquire")){
                return ACQUIRE;
            }

            return Board.valueOf(inputStr);
        }
    }
}
