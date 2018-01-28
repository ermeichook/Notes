package com.example.demo.notes.views;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

public class NotesHolder extends RecyclerView.ViewHolder {

    private CardView cardView;

    public NotesHolder(CardView cardView) {
        super(cardView);
        this.cardView = cardView;
    }

    public CardView getCardView() {
        return cardView;
    }
}
