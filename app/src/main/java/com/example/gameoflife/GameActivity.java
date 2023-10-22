package com.example.gameoflife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private List<Cell> cellList = new ArrayList<>();
    private Button AddSexuateCellButton;
    private Button AddAsexuateCellButton;
    private TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        AddSexuateCellButton = findViewById(R.id.AddSexuateCellButton);
        AddAsexuateCellButton = findViewById(R.id.AddAsexuateCellButton);
        displayText = findViewById(R.id.displayText);

        AddAsexuateCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add a new Person object to the list
                cellList.add(new Cell("Asexuate", "Full"));
                // Display the information
                displayInformation();
            }
        });


        AddSexuateCellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add a new Person object to the list
                cellList.add(new Cell("SexuateCell", "Full"));
                // Display the information
                displayInformation();
            }
        });
    }

    private void displayInformation() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cell person : cellList) {
            stringBuilder.append("Cell Type: ").append(person.getCellType()).append(", Food Status: ").append(person.getFoodStatus()).append("\n");
        }
        displayText.setText(stringBuilder.toString());
    }

}