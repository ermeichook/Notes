package com.example.demo.notes.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.notes.R;
import com.example.demo.notes.activities.MainActivity;
import com.example.demo.notes.models.Note;
import com.example.demo.notes.ormLite.HelperFactory;
import com.example.demo.notes.ormLite.NotesDAO;
import com.example.demo.notes.views.NotesHolder;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesAdapter extends RecyclerView.Adapter<NotesHolder> {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvBody)
    TextView tvBody;

    @BindView(R.id.tvTime)
    TextView tvTime;


    @BindView(R.id.tvIdInLL)
    TextView tvIdInLL;

    @BindView(R.id.linLayTVs)
    LinearLayout linearLayout;

    @OnClick(R.id.linLayTVs)
    void onClickNote(View view) {
        int id = Integer.parseInt(((TextView) view.findViewById(R.id.tvIdInLL)).getText().toString());
        dialog = createDialogDelOrChange(id);
        dialog.show();
    }


    private SimpleDateFormat sdf;
    private Context context;
    private List<Note> notesList;
    private Dialog dialog;
    private Dialog dialogChange;
    private LayoutInflater layoutInflater;

    @SuppressLint("SimpleDateFormat")
    public NotesAdapter(Context context, LayoutInflater layoutInflater, List<Note> noteList) {
        this.context = context;
        this.notesList = noteList;
        this.layoutInflater = layoutInflater;
        sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.notes_card_view, parent, false);
        return new NotesHolder(cv);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(NotesHolder holder, int position) {
        CardView cardView = holder.getCardView();
        ButterKnife.bind(this, cardView);

        Note note = notesList.get(position);

        if (note != null) {
            tvTitle.setText(note.getTitle());
            tvBody.setText(note.getBody());
            tvTime.setText(sdf.format(new Date(note.getDate())));
            tvIdInLL.setText(Integer.toString(note.getId()));
        }
    }

    @Override
    public int getItemCount() {
        if (notesList != null) return notesList.size();
        else return 0;
    }


    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
        this.notifyDataSetChanged();
    }

    private Dialog createDialogDelOrChange(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setTitle("Выберите действие");
        //adb.setMessage("Choose action!");
        adb.setPositiveButton("Удалить", (dialogInterface, i) -> {
            try {
                HelperFactory.getInstanse().getNotesDAO().delete(HelperFactory.getInstanse().getNotesDAO().getNoteByID(id));
            } catch (SQLException e) {
                //
            }
            ((MainActivity) context).setListNotes();
            Toast.makeText(context, "Запись удалена", Toast.LENGTH_SHORT).show();
            if (dialog != null) dialog.dismiss();
        });
        adb.setNegativeButton("Изменить", (dialogInterface, i) -> {
            dialogChange = createDialogChange(id);
            dialogChange.show();
            if (dialog != null) dialog.dismiss();
        });
        adb.setNeutralButton("Выход", (dialogInterface, i) -> {
            if (dialog != null) dialog.dismiss();
        });
        return adb.create();
    }

    private Dialog createDialogChange(int id) {
        NotesDAO notesDAO = null;
        try {
            notesDAO = HelperFactory.getInstanse().getNotesDAO();
        } catch (SQLException e) {
            //
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setTitle("Change note?");
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.dialog_note, null);
        EditText edTitle = view.findViewById(R.id.edTitle);
        EditText edBody = view.findViewById(R.id.edBody);
        Note noteForChange;
        if (notesDAO != null) {
            try {
                noteForChange = notesDAO.getNoteByID(id);
                edTitle.setText(noteForChange.getTitle());
                edBody.setText(noteForChange.getBody());
            } catch (SQLException e) {
                //
            }
        }
        adb.setView(view);
        NotesDAO finalNotesDAO = notesDAO;
        adb.setPositiveButton("Изменить", (dialogInterface, i) -> {
            Note noteToChange;
            String title = edTitle.getText().toString();
            String body = edBody.getText().toString();
            if (!title.equals("")) {
                if (finalNotesDAO != null) {
                    try {
                        noteToChange = finalNotesDAO.getNoteByID(id);
                        noteToChange.setBody(body);
                        noteToChange.setTitle(title);
                        finalNotesDAO.update(noteToChange);
                    } catch (SQLException e) {
                        //
                    }
                    ((MainActivity) context).setListNotes();
                }
                Toast.makeText(context, "Запись изменена", Toast.LENGTH_SHORT).show();
                ((MainActivity) context).setListNotes();
                if (dialogChange != null) dialogChange.dismiss();
            } else {
                Toast.makeText(context, "Заголовок не может быть пустым", Toast.LENGTH_SHORT).show();
                dialogChange = createDialogChange(id);
                dialogChange.show();
            }
        });
        adb.setNegativeButton("Отмена", (dialogInterface, i) -> {
            if (dialog != null) dialog.dismiss();
        });
        return adb.create();
    }

}
