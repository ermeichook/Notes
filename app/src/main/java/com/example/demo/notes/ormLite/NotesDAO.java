package com.example.demo.notes.ormLite;

import com.example.demo.notes.models.Note;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class NotesDAO extends BaseDaoImpl<Note, Integer> {

    NotesDAO(ConnectionSource connectionSource, Class<Note> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Note> getAllNotes() throws SQLException {
        return this.queryForAll();
    }

    public int getCountOfNotes() throws SQLException {
        return (int) this.countOf();
    }

    public boolean isNoteContanedInDBbyTitle(String name) throws SQLException {
        QueryBuilder<Note, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Note.NAME_TABLEFIELD_TITLE, name);
        PreparedQuery<Note> preparedQuery = queryBuilder.prepare();
        List<Note> list = query(preparedQuery);
        for (Note gri : list) {
            if (gri.getTitle().equals(name)) return true;
        }
        return false;
    }

    public Note getNoteByPos(int pos) throws SQLException {
        return getAllNotes().get(pos);
    }

    public Note getNoteByID(int id) throws SQLException {
        QueryBuilder<Note, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Note.NAME_TABLEFIELD_ID, id);
        PreparedQuery<Note> preparedQuery = queryBuilder.prepare();
        List<Note> list = query(preparedQuery);
        for (Note note : list) {
            if (note.getId() == id) return note;
        }
        return null;
    }

    public void deleteNoteByTitle(String name) throws SQLException {
        QueryBuilder<Note, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Note.NAME_TABLEFIELD_TITLE, name);
        PreparedQuery<Note> preparedQuery = queryBuilder.prepare();
        List<Note> list = query(preparedQuery);
        for (Note gri : list) {
            this.delete(gri);
        }
    }

    public void delAllNotes() throws SQLException {
        for (Note note : getAllNotes()) {
            this.delete(note);
        }
    }

    public List<Note> getFavoriteNotes() throws SQLException {
        QueryBuilder<Note, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Note.NAME_TABLEFIELD_ISFAVARITE, 1);
        PreparedQuery<Note> preparedQuery = queryBuilder.prepare();
        return query(preparedQuery);
    }
}