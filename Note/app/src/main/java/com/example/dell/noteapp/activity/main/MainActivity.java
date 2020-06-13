package com.example.dell.noteapp.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dell.noteapp.R;
import com.example.dell.noteapp.activity.editor.EditorActivity;
import com.example.dell.noteapp.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView{
    public static int INTENT_ADD=100;
 public static int INTENT_EDIT=200;
FloatingActionButton fab;
RecyclerView recyclerView;
SwipeRefreshLayout swipeRefresh;
MainPresenter presenter;
MainAdapter adapter;
MainAdapter.ItemClickListener itemClickListener;

List<Note> note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab=(FloatingActionButton)findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this
                        , EditorActivity.class),INTENT_ADD);
            }
        });

        presenter=new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        presenter.getData();
                    }
                }
        );

        itemClickListener=(new MainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=note.get(position).getId();
                String title=note.get(position).getTitle();
                String notes=note.get(position).getNote();
                int color=note.get(position).getColor();

                Intent intent=new Intent(getApplicationContext(),EditorActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title",title);
                intent.putExtra("note",notes);
                intent.putExtra("color",color);

                startActivityForResult(intent,INTENT_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==INTENT_ADD && requestCode== RESULT_OK)
        {
            presenter.getData();
        }
        else  if(requestCode==INTENT_EDIT && requestCode== RESULT_OK)
        {
            presenter.getData();
        }
    }

    @Override
    public void showLoading() {
     swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
       swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
     adapter=new MainAdapter(this,notes,itemClickListener);
     adapter.notifyDataSetChanged();
     recyclerView.setAdapter(adapter);

     note=notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
