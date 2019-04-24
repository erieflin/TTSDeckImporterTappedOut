package importObjects;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.CardUtils;
import core.Constants;
import exportObjects.TTS_Card;
import importObjects.CardDetails.*;
import org.jsoup.Connection;

public class Card implements BaseCard
{
    //TODO these might change to something other than final
    private String cardName;
    private String set;
    private int quantity;
    private Board board;
    private Tag[] modifiers;
    private File cardImage;
    private List<Token> relatedTokens = new ArrayList<Token>();
    private HashMap<Integer, TTS_Card> exportCards = new HashMap<Integer, TTS_Card>();
    private String desiredTTSPile = "";
    public Card(CardParams params, File cardImage)
    {
        this.cardName = params.cardName;
        this.set = params.set;
        this.quantity = params.qty;
        this.board = params.board;
        this.modifiers = params.modifiers;
        this.cardImage = cardImage;
    }

    public String getCardName() {
        return cardName;
    }

    public String getSet() {
        return set;
    }

    public int getQuantity() {
        return quantity;
    }

    public Board getBoard() {
        return board;
    }

    public Tag[] getModifiers() {
        return modifiers;
    }

    public File getCardImage() {
        return cardImage;
    }

    public List<Token> getRelatedTokens() {
        return relatedTokens;
    }

    public void setRelatedTokens(List<Token> relatedTokens) {
        this.relatedTokens = relatedTokens;
    }

    public String getTTSPile() {
        if(desiredTTSPile!= null && desiredTTSPile.length() >0) {
            return desiredTTSPile;
        }
        if( CardUtils.checkCardHasTag(this, CardDetails.Tag.COMMANDER)){
            return Constants.COMMANDER;
        }
        if(board!= null){
            return board.toString();
        }
        return Board.SIDEBOARD.toString();
    }

    public void setDesiredTTSPile(String desiredTTSPile) {
        this.desiredTTSPile = desiredTTSPile;
    }

    public String toString(){
        return getBoard() + ": " + getQuantity() + "x " + getCardName() + " " + getSet() + " " + getModifiers();
    }

}
