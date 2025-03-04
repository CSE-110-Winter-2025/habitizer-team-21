package edu.ucsd.cse110.habitizer.app.ui.routinelist;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class RoutineListAdapter extends ArrayAdapter<Routine> {
    private Routine currRoutine;
    public RoutineListAdapter(Context context, List<Routine> routines) {
        super(context, 0, new ArrayList<>(routines)); // Ensuring a mutable list
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemRoutineBinding binding;

        if (convertView != null) {
            binding = ListItemRoutineBinding.bind(convertView);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemRoutineBinding.inflate(layoutInflater, parent, false);
        }

        Routine routine = getItem(position);
        assert routine != null;


        binding.routine.setText(routine.name());





        binding.getRoot().setOnClickListener(v -> {
            this.currRoutine = routine;
        });



        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Routine getRoutine(){
        return currRoutine;
    }
}

