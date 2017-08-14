package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TaskItem> taskItems = new ArrayList<>();
    private String[] taskTest = new String[]{"wow", "test"};
    private Date dueDateTest;
    private int testInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (View) findViewById(R.id.content);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dueDateTest = cal.getTime();

        taskItems.add(new TaskItem(0, "test", taskTest, dueDateTest));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new StudyAdapter(context, taskItems);
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTask();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.recycler_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        },
                        1000);
            }
        });
    }
    public void share() {
        final AlertDialog.Builder shareDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View shareDialogView = inflater.inflate(R.layout.dialog_share, null);
        shareDialogBuilder.setView(shareDialogView);
        shareDialogBuilder.setTitle("Share");
        shareDialogBuilder.setIcon(R.drawable.ic_share_black_24dp);
        final TextInputEditText desc = (TextInputEditText) shareDialogView.findViewById(R.id.shareDialogDesc);
        shareDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Snackbar.make(view, "Share dialog was cancelled.", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                share();
                            }
                        })
                        .setDuration(6000).show();
            }
        });
        shareDialogBuilder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, desc.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_intent_value)));
            };
        });
        AlertDialog shareDialog = shareDialogBuilder.create();
        shareDialog.show();
    }
    public void newTask() {
        testInt++;
        final AlertDialog.Builder newTaskDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View newTaskDialogView = inflater.inflate(R.layout.dialog_new, null);
        newTaskDialogBuilder.setView(newTaskDialogView);
        newTaskDialogBuilder.setTitle("New Task");
        final TextInputEditText name = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogName);
        final TextInputEditText labels = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogLabels);
        final TextInputEditText datepicker = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogDate);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datepicker.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();
            }
        });
        newTaskDialogBuilder.setIcon(R.drawable.pencil);
        newTaskDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Snackbar.make(view, "New task dialog was cancelled.", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newTask();
                            }
                        })
                        .setDuration(6000).show();
            }
        });
        newTaskDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String task = name.getText().toString();
                String taskLabels = labels.getText().toString();
                // TODO(Edric): Remove this unnecessary and add more coding logic
                out.println("Task name is " + task);
                out.println("Task labels are " + taskLabels);
                Date taskDate = null;
                try {
                    taskDate = new SimpleDateFormat("dd/MM/yyyy").parse(datepicker.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final TaskItem tempItem = new TaskItem(testInt, task, taskTest, taskDate);
                taskItems.add(tempItem);
                dialogInterface.dismiss();
                Snackbar.make(view, task + " was saved to calendar.", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // TODO(Edric): Delete the task stuff and reshow the dialog
                                taskItems.remove(tempItem);
                                mAdapter.notifyItemRemoved(testInt);
                                newTask();
                            }
                        })
                        .setDuration(8000).show();
            }
        });
        AlertDialog newTaskDialog = newTaskDialogBuilder.create();
        newTaskDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent prefsIntent = new Intent(this, SettingsActivity.class);
                startActivity(prefsIntent);
                break;
            case R.id.action_about:
                Snackbar.make(view, "Coming soon!", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                share();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
