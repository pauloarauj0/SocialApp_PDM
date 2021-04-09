package com.example.socialapp.main.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialapp.R;
import com.example.socialapp.data.base.Account;
import com.example.socialapp.data.base.AppDatabase;

import java.util.Calendar;

public class Settings extends AppCompatActivity {

    private TextView viewDate;
    private DatePickerDialog.OnDateSetListener datePick;

    private EditText nameField, descField, emailField;
    private Button changeNameBtn, changeDescBtn, changeEmailBtn, changePasswordBtn, logoutBtn;
    AppDatabase database;
    private int INVALID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        database = AppDatabase.getDatabase(Settings.this);

        nameField = (EditText) findViewById(R.id.newName);
        descField = (EditText) findViewById(R.id.newDesc);
        emailField = (EditText) findViewById(R.id.newMail);

        changeNameBtn = (Button) findViewById(R.id.changeName);
        changeDescBtn = (Button) findViewById(R.id.changeDesc);
        changeEmailBtn = (Button) findViewById(R.id.changeMail);

        viewDate = (TextView) findViewById(R.id.newBirthdate);


        //metodo para trocar a data
        viewDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(Settings.this, 0, datePick, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //mudar o display para a data selecionada
        datePick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("SETTINGS", "Day/Month/Year: " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;

                MainActivity.AccAtual.setUserAniversario(date);
                viewDate.setText(date);
            }
        };

        //placeholder
        changeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change name");
                String nName = nameField.getText().toString();
                if(nName.isEmpty()){
                    Toast.makeText(Settings.this, "Insira um nome válido", Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity.AccAtual.setUserName(nName);
                    if(database.getDao().updateAccount(MainActivity.AccAtual)!=INVALID){
                        changeNameBtn.setBackgroundColor(Color.GREEN);
                        Toast.makeText(Settings.this, "Nome alterado para: "+nName, Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        //placeholder
        changeDescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change desc");
                String nDesc = descField.getText().toString();
                MainActivity.AccAtual.setUserDesc(nDesc);
                if(database.getDao().updateAccount(MainActivity.AccAtual)!=INVALID){
                    changeDescBtn.setBackgroundColor(Color.GREEN);
                    Toast.makeText(Settings.this, "Descrição alterada para: "+nDesc, Toast.LENGTH_SHORT).show();

                }
            }
        });

        //placeholder
        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change mail");
                String nEmail = emailField.getText().toString();
                if(nEmail.isEmpty()){
                    Toast.makeText(Settings.this, "Insira um email válido", Toast.LENGTH_SHORT).show();
                }
                else{

                    //conta backup
                    Account BackupAcc = backup(MainActivity.AccAtual);

                    MainActivity.AccAtual.setEmail(nEmail);

                    //ver se o email e valido para inserir
                    if(database.getDao().insertAccount(MainActivity.AccAtual)!=INVALID){
                        //apagar a conta com email antigo
                        database.getDao().deleteAccount(BackupAcc);

                        changeEmailBtn.setBackgroundColor(Color.GREEN);
                        Toast.makeText(Settings.this, "Email alterado para: "+nEmail, Toast.LENGTH_SHORT).show();

                    }

                    else{
                        MainActivity.AccAtual.setEmail(BackupAcc.getEmail());
                        Log.d("SETTINGS", "Email in use");
                        Toast.makeText(Settings.this, "Email inválido", Toast.LENGTH_SHORT).show();

                    }
                }




            }
        });




    }

    /**
     * Cria um backup da conta atual
     * @param AccAtual Conta atual
     * @return backup
     */
    private Account backup(Account AccAtual) {
        Account BackupAcc = new Account();
        BackupAcc.setEmail(AccAtual.getEmail());
        BackupAcc.setUserPass(AccAtual.getUserPass());
        BackupAcc.setUserDesc(AccAtual.getUserDesc());
        BackupAcc.setUserName(AccAtual.getUserName());
        BackupAcc.setUserAniversario(AccAtual.getUserAniversario());

        return BackupAcc;

    }

}
