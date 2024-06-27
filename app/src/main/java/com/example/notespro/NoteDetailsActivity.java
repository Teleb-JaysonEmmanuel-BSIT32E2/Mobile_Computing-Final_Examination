package com.example.notespro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton btnSaveNote;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteBtn;
    //TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.txtTitleNote);
        contentEditText = findViewById(R.id.txtContentText);
        btnSaveNote = findViewById(R.id.btnSave);
        pageTitleTextView = findViewById(R.id.title_page);
        deleteBtn = findViewById(R.id.btnDelete);

        //receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteBtn.setVisibility(View.VISIBLE);
        }

        btnSaveNote.setOnClickListener((v) -> saveNote());
        deleteBtn.setOnClickListener((v) -> deleteNoteFromFireBase());

    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Please fill out the title field");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFireBase(note);
    }

    void saveNoteToFireBase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //it will update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            // it will create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // note added
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }
                else{
                    // failed note added
                    Utility.showToast(NoteDetailsActivity.this, "Failed adding notes");
                }
            }
        });
    }

    void deleteNoteFromFireBase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // successfully deleted
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    finish();
                }
                else{
                    // failed note added
                    Utility.showToast(NoteDetailsActivity.this, "Failed deleting notes");
                }
            }
        });
    }
}