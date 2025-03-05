package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogRenameRoutineBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class RenameRoutineFragment extends DialogFragment{
    private MainViewModel activityModel;
    private FragmentDialogRenameRoutineBinding binding;
    private Routine routine;
    private RenameRoutineListener listener;

    public interface RenameRoutineListener {
        void onRoutineRenamed(Routine updatedRoutine);
    }

    public RenameRoutineFragment(Routine routine){
        this.routine = routine;
    }

    public static RenameRoutineFragment newInstance(Routine routine) {
        return new RenameRoutineFragment(routine);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof RenameRoutineListener) {
            listener = (RenameRoutineListener) parentFragment;
        } else {
            throw new RuntimeException("Parent fragment must implement RenameRoutineListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.binding = FragmentDialogRenameRoutineBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Routine Information")
                .setView(binding.getRoot()) //
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        routine.rename(binding.routineName.getText().toString());
        if (listener != null) {
            listener.onRoutineRenamed(routine);
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}
