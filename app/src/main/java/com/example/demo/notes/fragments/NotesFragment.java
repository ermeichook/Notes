package com.example.demo.notes.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.notes.R;
import com.example.demo.notes.adapters.NotesAdapter;
import com.example.demo.notes.models.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesFragment extends Fragment {

    @BindView(R.id.recyclerViewNotesFragment)
    RecyclerView recyclerView;

    private NotesAdapter notesAdapter;
    private List<Note> noteList;

    public NotesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);

        //noinspection ConstantConditions
        notesAdapter = new NotesAdapter(getActivity(), getActivity().getLayoutInflater(), noteList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesAdapter);

        return view;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        if (notesAdapter != null) {
            notesAdapter.setNotesList(noteList);
        }
    }
}
