package edu.ucsd.cse110.habitizer.app.ui.routinelist;


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
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineListBinding;
import edu.ucsd.cse110.habitizer.app.ui.routinelist.RoutineListAdapter;

public class RoutineListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentRoutineListBinding binding;
    private RoutineListAdapter adapter;

    public RoutineListFragment() {
        // Required empty public constructor
    }

    public static RoutineListFragment newInstance() {
        RoutineListFragment fragment = new RoutineListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        this.adapter = new RoutineListAdapter(requireContext(), new ArrayList<>());

        // Observe task changes from ViewModel
        activityModel.getOrderedRoutines().observe(routines -> {
            if (routines == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(routines)); // Ensure mutable copy
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
        this.binding = FragmentRoutineListBinding.inflate(inflater, container, false);


        binding.routineList.setAdapter(adapter);

        // Open dialog to add new tasks
        binding.floatingActionButton.setOnClickListener(v -> {
            /*var dialogFragment = CreateRoutineFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateRoutineDialogFragment");*/
        });

        setupMvp();

        return binding.getRoot();
    }

    private void setupMvp() {

    }

}
