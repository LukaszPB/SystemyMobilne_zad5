package to_do_list.zad3;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
        category = Category.HOME;
    }
    public Date getDate() { return date; }
    public boolean isDone() { return done; }
    public void setDone(boolean x) { done = x; }
    public void setName(String s) { name = s; }
    public String getName() { return name; }
    public UUID getId() { return id; }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
