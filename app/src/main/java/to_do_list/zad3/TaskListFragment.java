package to_do_list.zad3;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    public static String KEY_EXTRA_TASK_ID = "taskId";
    private boolean subtitleVisible;

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView imageView;
        private CheckBox checkBox;
        private Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task,parent,false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            imageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
        private void setupDateFieldValue(Date date) {
            Locale locale = new Locale("pl","PL");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
            dateTextView.setText(dateFormat.format(date));
        }
        public void bind(Task task) {
            this.task = task;
            nameTextView.setText(task.getName());
            setupDateFieldValue(task.getDate());
            if(task.getCategory().equals(Category.HOME)) {
                imageView.setImageResource(R.drawable.ic_house);
            }
            else {
                imageView.setImageResource(R.drawable.ic_studies);
            }
            checkBox.setChecked(task.isDone());
            if(task.isDone()) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

        }

        public CheckBox getCheckBox() { return checkBox; }
        public TextView getNameTextView() { return nameTextView; }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
    }
    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;
        public TaskAdapter(List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
            CheckBox checkBox = holder.getCheckBox();
            TextView nameTextView = holder.getNameTextView();
            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                        if(isChecked) {
                            nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                        else {
                            nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }

                    });

        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup con, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_task_list,con,false);
        recyclerView = view.findViewById(R.id.task_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_task_menu,menu);
        MenuItem subtitleItem = menu.findItem(R.string.show_subtitle);
        if(subtitleVisible) {
            subtitleItem = menu.findItem(R.string.hide_subtitle);
        }
        else {
            subtitleItem = menu.findItem(R.string.show_subtitle);
        }

    }
    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.new_task) {
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            Intent intent = new Intent(getActivity(),MainActivity.class);
            intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID,task.getId());
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if(adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
    public void updateSubtitle() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int toDoTaskCount = 0;
        for(Task task : tasks) {
            if(!task.isDone()) {
                toDoTaskCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format,toDoTaskCount);
        if(!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }
}
