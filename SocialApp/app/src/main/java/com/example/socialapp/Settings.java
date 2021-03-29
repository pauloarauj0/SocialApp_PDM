package com.example.socialapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Settings extends AppCompatActivity {

    private TextView viewDate;
    private DatePickerDialog.OnDateSetListener datePick;

    private EditText nameField, descField, emailField;
    private Button changeNameBtn, changeDescBtn, changeEmailBtn, changePasswordBtn, logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                MainActivity.UserAtual.setAniversario(date);
                viewDate.setText(date);
            }
        };

        //placeholder
        changeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change name");
                String nName = nameField.getText().toString();
                if(MainActivity.UserAtual.changeName(nName)){
                    changeNameBtn.setBackgroundColor(Color.GREEN);
                }
            }
        });

        //placeholder
        changeDescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change desc");
                String nDesc = descField.getText().toString();
                if(MainActivity.UserAtual.changeDesc(nDesc))
                    changeDescBtn.setBackgroundColor(Color.GREEN);
            }
        });

        //placeholder
        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SETTINGS", "Clicked on change mail");
                String nEmail = emailField.getText().toString();

                if(MainActivity.UserAtual.changeEmail(nEmail))
                    changeEmailBtn.setBackgroundColor(Color.GREEN);
                else{
                    Log.d("SETTINGS", "Email in use");
                    Toast.makeText(Settings.this, "Email inválido", Toast.LENGTH_SHORT).show();

                }

            }
        });




    }

}
