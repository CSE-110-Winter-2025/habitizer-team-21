package edu.ucsd.cse110.habitizer.app.ui.tasklist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog.CreateTaskFragment;

public class CardListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentCardListBinding binding;
    private CardListAdapter adapter;

    public CardListFragment() {
        // Required empty public constructor
    }

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter with an empty list
        this.adapter = new CardListAdapter(requireContext(), new ArrayList<>());

        // Observe task changes from ViewModel
        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // Ensure mutable copy
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        this.binding = FragmentCardListBinding.inflate(inflater, container, false);


        // Set adapter to ListView
        binding.taskList.setAdapter(adapter);

        // Open dialog to add new tasks
        binding.floatingActionButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
        });

        setupMvp();

        return binding.getRoot();
    }

    private void addTask(String task, boolean append) {
        if (append) {
            activityModel.append(task);
        } else {
            activityModel.prepend(task);
        }
    }
    private void setupMvp() {
        activityModel.getRoutineButton().observe(text -> binding.routineButton.setText(text));

        binding.routineButton.setOnClickListener(v -> {
            activityModel.toggleRoutine();

            String buttonText = binding.routineButton.getText().toString();

            if ("Routine Complete".equals(buttonText)) {
                binding.routineButton.setEnabled(false);
                binding.routineButton.setBackgroundColor(android.graphics.Color.parseColor("#B0B0B0"));
            } else {
                binding.routineButton.setEnabled(true);
                int color = "End Routine".equals(buttonText) ? android.graphics.Color.parseColor("#FF6347") : android.graphics.Color.parseColor("#4CAF50");
                binding.routineButton.setBackgroundColor(color);
            }
        });
    }
}