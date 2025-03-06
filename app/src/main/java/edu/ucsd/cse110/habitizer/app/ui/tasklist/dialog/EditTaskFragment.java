package edu.ucsd.cse110.habitizer.app.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.R;

public class EditTaskFragment extends DialogFragment {
    public interface TaskEditListener{
        void onRename (String newName);
        void onDelete();
    }

    private TaskEditListener listener;
    private String taskName;
    private int taskId;
    private EditText editTextTaskName;

    public EditTaskFragment(String taskName, int taskId, TaskEditListener listener){
        this.taskName = taskName;
        this.taskId = taskId;
        this.listener = listener;
    }

    @NonNull
    @Override

    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_edit_task_name,null);

        editTextTaskName = view.findViewById(R.id.edit_task_name);
        editTextTaskName.setText(taskName);

        builder.setView(view)
                .setTitle("Edit Task")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTaskName = editTextTaskName.getText().toString().trim();
                        if (!TextUtils.isEmpty(newTaskName)){
                            listener.onRename(newTaskName);
                        } else {
                            Toast.makeText(getActivity(),"Can't be empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDeleteConfirmationDialog();
                    }
                });
            return builder.create();
        }

        private void showDeleteConfirmationDialog(){
        ConfirmDeleteTaskDialogFragment deleteDialog = ConfirmDeleteTaskDialogFragment.newInstance(taskId);
        deleteDialog.show(getParentFragmentManager(), "ConfirmDeleteTaskDialog");
    }
}
