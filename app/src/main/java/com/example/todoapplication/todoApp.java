package com.example.todoapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;

public class todoApp extends AppCompatActivity {
    protected ListView todo;
    private Button add;
    private Button delete;
    public static ArrayList<String> works = new ArrayList<>();
    public SharedPreferences database;
    public SharedPreferences.Editor databaseEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_todo_app);
        database = getSharedPreferences("saveWork",MODE_PRIVATE);
        databaseEditor = database.edit();
        getFromDatabase();
        todo=findViewById(R.id.workToDisplay);
        ArrayAdapter adapter = new ArrayAdapter(todoApp.this, android.R.layout.simple_list_item_1,works);
        todo.setAdapter(adapter);
        add=findViewById(R.id.addNote);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(todoApp.this);
                builder.setTitle("Enter your Work!");
                final EditText getInput = new EditText(todoApp.this);
                builder.setView(getInput);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String getText = getInput.getText().toString();
                        if(!getText.isEmpty()){
                            works.add((works.size()+1)+". "+getText);
                            Toast.makeText(todoApp.this, "Work Added Sucessfully!😎", Toast.LENGTH_SHORT).show();
                            saveInDatabase();
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(todoApp.this,"can't add empty work!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Discard",null);
                builder.show();
                todo.setAdapter(adapter);
            }
        });
        delete= findViewById(R.id.deleteNote);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(todoApp.this);
                builder.setTitle("Enter the Number: ");
                final EditText getNumber = new EditText(todoApp.this);
                builder.setView(getNumber);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int number = Integer.parseInt(getNumber.getText().toString());
                        if(number>works.size()){
                            Toast.makeText(todoApp.this, "Can't find the work..", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            works.remove(number-1);
                            updateArrayList();
                            Toast.makeText(todoApp.this, "Work deleted", Toast.LENGTH_LONG).show();
                            saveInDatabase();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
                todo.setAdapter(adapter);

            }
        });
    }
    public void updateArrayList(){
        ArrayList<String> updated = new ArrayList<>();
        for(int i=0; i<works.size(); i++){
            String text = works.get(i);
            String clear=text.substring(text.indexOf(".")+2);
            updated.add((updated.size()+1)+". "+clear);
        }
        works.clear();
        works.addAll(updated);
    }
    public void saveInDatabase(){
        database = getSharedPreferences("saveWork",MODE_PRIVATE);
        databaseEditor.clear().apply();
        Gson gson = new Gson();
        String json = gson.toJson(works);
        databaseEditor.putString("works",json).apply();

    }
    public void getFromDatabase(){
        database = getSharedPreferences("saveWork",MODE_PRIVATE);
        String json = database.getString("works","[]");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> loaded = new Gson().fromJson(json,type);
        if(loaded !=null){
            works.clear();
            works.addAll(loaded);

        }
        else{
            Toast.makeText(this, "Nothing ", Toast.LENGTH_SHORT).show();
        }
    }
}