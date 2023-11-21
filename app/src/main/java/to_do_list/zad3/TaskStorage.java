package to_do_list.zad3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private final List<Task> tasks;
    private TaskStorage() {
        tasks = new ArrayList<>();
        for(int i=0;i<150;i++) {
            Task task = new Task();
            task.setName("Pilne zadanie numer " + i);
            task.setDone(i%3 == 0);
            tasks.add(task);
            if(i%3==0) {
                task.setCategory(Category.STUDIES);
            }
            else {
                task.setCategory(Category.HOME);
            }
        }
    }

    public static TaskStorage getInstance() {
        return taskStorage;
    }
    public List<Task> getTasks() { return tasks; }
    public Task getTask(UUID id) {
        for(Task t : tasks) {
            if(t.getId().equals(id))
                return t;
        }
        return null;
    }
    public void addTask(Task task) {
        tasks.add(task);
    }
}
