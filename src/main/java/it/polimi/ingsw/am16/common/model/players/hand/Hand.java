package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

import java.util.ArrayList;
import java.util.List;

public class Hand implements HandModel{
    private final List<PlayableCard> cards;
    private int size;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public List<PlayableCard> getCards() {
        return cards;
    }

    @Override
    public int getSize(){
        /*
        int i = 0;
        while(cards[i] != null && i < cards.length){
            ++i;
        }
        return i;
         */
        return size;
    }

    @Override
    public PlayableCard getCard(int index){
        return cards.get(index);
    }




}
