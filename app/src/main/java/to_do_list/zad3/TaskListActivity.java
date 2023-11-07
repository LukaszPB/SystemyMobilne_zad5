package to_do_list.zad3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskListActivity extends SingleFragmentActivity  {
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}