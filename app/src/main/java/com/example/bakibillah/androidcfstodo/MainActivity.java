package com.example.bakibillah.androidcfstodo;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bakibillah.androidcfstodo.Adapter.ListItemAdapter;
import com.example.bakibillah.androidcfstodo.Model.ToDo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    List<ToDo> toDoList = new ArrayList<>();

    FirebaseFirestore db;
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    public MaterialEditText title;
    public MaterialEditText description;
    public boolean isupdate = false;
    public  String idUpdate = "";


    ListItemAdapter adapter;
    android.app.AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int firestore
        db = FirebaseFirestore.getInstance();
        dialog = new SpotsDialog(this);
        title =(MaterialEditText)findViewById(R.id.title);
        description =(MaterialEditText)findViewById(R.id.description);
        fab =(FloatingActionButton)findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isupdate){
                    setData(title.getText().toString(),description.getText().toString());
                }
                else {
                    UpdateData(title.getText().toString(),description.getText().toString());
                    isupdate = !isupdate; //reset flag
                }

            }
        });

        listItem = (RecyclerView)findViewById(R.id.list_todo);
        listItem.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData(); // load firestore data

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("DELETE")){
            deleteItem(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection("ToDoList").document(toDoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    private void UpdateData(String title, String description) {
        db.collection("ToDoList").document(idUpdate)
                .update("title",title,"description",description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Updated!",Toast.LENGTH_LONG).show();
                    }
                });

        //Realtime Updaet Refresh data;
        db.collection("ToDOList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String title, String description) {
        String  id = UUID.randomUUID().toString(); // new UUID
        Map<String,Object> todo = new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("description",description);

        db.collection("ToDOList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData();
            }
        });

    }

    private void loadData() {
        dialog.show();
        if(toDoList.size()>0)
            toDoList.clear(); // remove old value

        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            ToDo todo = new ToDo(doc.getString("id"),
                                                doc.getString("title"),
                                                doc.getString("description"));
                            toDoList.add(todo);
                        }
                        adapter = new ListItemAdapter(MainActivity.this,toDoList);
                        listItem.setAdapter(adapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }


}
