package yamuna.com.locationalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yamuna on 6/15/2016.
 */
public class LogInFragment  extends Fragment implements View.OnClickListener {


    @Override
    public void onClick(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        return rootView;

    }


    public void onStart() {
        super.onStart();
        Button btnLogIn = (Button) getActivity().findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView accountName = (TextView) getActivity().findViewById(R.id.txtAccountName);
                EditText pw = (EditText) getActivity().findViewById(R.id.pwfield);

                if(accountName.getText().toString().contentEquals("abc") && pw.getText().toString().contentEquals("123")){
                    Intent intent = new Intent(getActivity(), ReminderActivity.class);
                    getActivity().startActivity(intent);
                }else{
                    accountName.setText("");
                    pw.setText("");
                    Toast.makeText(getActivity().getBaseContext(),"Wrong Detail enter again OR create new ", Toast.LENGTH_LONG).show();
                }
            }
        });


        Button btnCreate = (Button) getActivity().findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView accountName = (TextView) getActivity().findViewById(R.id.txtNewAccouontName);
                EditText pw = (EditText) getActivity().findViewById(R.id.newPwField);
                EditText pwConfirm = (EditText) getActivity().findViewById(R.id.newConfirmPwField);

                DBHandler dbHandler=new DBHandler(getActivity().getBaseContext());
                if(pw.getText().toString().contentEquals(pwConfirm.getText().toString())){
                    dbHandler.insertAccount(accountName.getText().toString(),pw.getText().toString());
                    Toast.makeText(getActivity().getBaseContext(),"Added", Toast.LENGTH_LONG).show();
                    accountName.setText("");
                    pw.setText("");
                    pwConfirm.setText("");
                }else{
                    Toast.makeText(getActivity().getBaseContext(),"Wrong Detail", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
