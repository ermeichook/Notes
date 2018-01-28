package com.example.demo.notes.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.notes.R;
import com.example.demo.notes.fragments.NotesFragment;
import com.example.demo.notes.models.Note;
import com.example.demo.notes.ormLite.HelperFactory;
import com.example.demo.notes.ormLite.NotesDAO;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private NotesDAO notesDAO;
    private NotesFragment fragment;
    private boolean isFavOnScreen = false;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            isFavOnScreen = savedInstanceState.getBoolean("isFav", false);
        }
        try {
            notesDAO = HelperFactory.getInstanse().getNotesDAO();
        } catch (SQLException e) {
            Toast.makeText(this, "Error of access do DB", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> {
            dialog = createDialogToAddNote();
            dialog.show();
        });

        if (!isFavOnScreen) {
            try {
                fragment = new NotesFragment();
                fragment.setNoteList(notesDAO.getAllNotes());
                doTransaction(fragment);
            } catch (Exception ex) {
                //error
            }
        } else {
            try {
                fragment = new NotesFragment();
                fragment.setNoteList(notesDAO.getFavoriteNotes());
                doTransaction(fragment);
            } catch (Exception ex) {
                //error
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = new NotesFragment();
       //List<Note> notesList = null;
        doTransaction(fragment);

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("isFav", isFavOnScreen);
    }

    //===================================================//
    //Metods
    //===================================================//
    public void doTransaction(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameForFragments, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void setListNotes() {
        try {
            List<Note> notes = null;
            if (isFavOnScreen) {
                notes = notesDAO.getFavoriteNotes();
            } else if (!isFavOnScreen) {
                notes = notesDAO.getAllNotes();
            }
            if (notes != null && fragment != null) {
                fragment.setNoteList(notes);
            }
        } catch (Exception ex) {
            //
        }

    }

    private Dialog createDialogToAddNote() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.dialog_note, null);
        adb.setCancelable(false);
        adb.setTitle("Создание заметки");
        adb.setView(dialogView);
        adb.setPositiveButton("Да", (dialogInterface, i) -> {
            String titleNote = ((EditText) dialogView.findViewById(R.id.edTitle)).getText().toString();
            String bodyNote = ((EditText) dialogView.findViewById(R.id.edBody)).getText().toString();
            if (!titleNote.equals("")) {
                try {
                    Note note = new Note(titleNote, bodyNote, System.currentTimeMillis());
                    notesDAO.create(note);
                } catch (SQLException e) {
                    Toast.makeText(this, "error of creating note", Toast.LENGTH_SHORT).show();
                }
                if (fragment != null) {
                    try {
                        if (!isFavOnScreen) fragment.setNoteList(notesDAO.getAllNotes());
                        else if (isFavOnScreen) fragment.setNoteList(notesDAO.getFavoriteNotes());
                    } catch (Exception ex) {
                        Toast.makeText(this, "error..", Toast.LENGTH_SHORT).show();
                    }
                }
                if (dialog != null) dialog.dismiss();
            } else {
                Toast.makeText(this, "Заголовок не может быть пустым", Toast.LENGTH_SHORT).show();
                dialog = createDialogToAddNote();
                dialog.show();
            }
        });
        adb.setNegativeButton("Нет", (dialogInterface, i) -> {
            if (dialog != null) dialog.dismiss();
        });
        return adb.create();
    }

    
}
