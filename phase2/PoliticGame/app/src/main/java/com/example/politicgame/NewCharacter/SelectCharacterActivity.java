package com.example.politicgame.NewCharacter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.politicgame.Character.GameCharacter;
import com.example.politicgame.Character.UserTools.UserAccount;
import com.example.politicgame.GameActivity;
import com.example.politicgame.GameMode.GameModeActivity;
import com.example.politicgame.Application.PoliticGameApp;
import com.example.politicgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCharacterActivity extends GameActivity {
  final private int THIRDCHARACTERSCORE = 100;
  final private int FOURTHCHARACTERSCORE = 200;
  final private int FIFTHCHARACTERSCORE = 300;

  private int currCharacter;
  private GameCharacter selectedCharacter;
  private Drawable highlight;
  private PoliticGameApp app;
  private ImageView charAButton;
  private ImageView charBButton;
  private ImageView charCButton;
  private ImageView charDButton;
  private ImageView charEButton;
  private TextView totalScoreText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (PoliticGameApp) getApplication();
    setContentView(R.layout.activity_select_character);
    setTitle("Select Game Character");
    currCharacter = 0;
    highlight = getResources().getDrawable(R.drawable.highlight);

    charAButton = findViewById(R.id.imageButton);
    charBButton = findViewById(R.id.imageButton2);
    charCButton = findViewById(R.id.imageButton3);
    charDButton = findViewById(R.id.imageButton4);
    charEButton = findViewById(R.id.imageButton5);
    totalScoreText = findViewById(R.id.score_count);
    final TextView inputName = findViewById(R.id.name_input);
    final Button submitName = findViewById(R.id.submit_name);
    final Button backButton = findViewById(R.id.backButton);
    final TextView error_name = findViewById(R.id.error_name);
    final TextView error_select = findViewById(R.id.error_character);
    // Character A is selected

    setCharacterWheel();

    submitName.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            UserAccount user = app.getCurrentUser();
            String name = inputName.getText().toString();

            boolean isUnique = !user.isDuplicate(name);

            if (currCharacter != 0 && (!name.equals("") && isUnique)) {
              characterSet(name);
            }

            if (currCharacter == 0) {
              error_select.setText("Swipe and tap the icons to select a character");
            } else {
              error_select.setText("");
            }

            if (name.equals("")) {
              Log.i("error_name","Enter a character name");
              error_name.setText("Enter a character name");
            }
            else if (!isUnique){
              Log.i("error_name","Character name already exists for this user");
              error_name.setText("Character name already exists for this user");
            }
            else {
              Log.i("error_name","Empty Error String");
              error_name.setText("");
            }
          }
        });

    backButton.setOnClickListener(
        new View.OnClickListener() {
          public void onClick(View v) {
            openMainMenu();
            //app.getCurrentUser().setCurrentCharacter(selectedCharacter);
          }
        });
  }

  private void setCharacterWheel(){
    UserAccount user = app.getCurrentUser();
    int totalScore = user.getTotalScore();

    ArrayList<ImageView> charArray = new ArrayList<>();
    charArray.add(charAButton);
    charArray.add(charBButton);
    this.setListener(charAButton, charArray, 1);
    this.setListener(charBButton, charArray, 2);

    String scoreMsg = "Total Score: " + totalScore;
    totalScoreText.setText(scoreMsg);

    if (totalScore >= THIRDCHARACTERSCORE){
      charArray.add(charCButton);
      this.setListener(charCButton, charArray, 3);
    } else {
      charCButton.setImageResource(R.drawable.character_3_lock);
    }

    if (totalScore >= FOURTHCHARACTERSCORE){
      charArray.add(charDButton);
      this.setListener(charDButton, charArray, 4);
    } else {
      charDButton.setImageResource(R.drawable.character_4_lock);
    }

    if (totalScore >= FIFTHCHARACTERSCORE){
      charArray.add(charEButton);
      this.setListener(charEButton, charArray, 5);
    } else {
      charEButton.setImageResource(R.drawable.character_5_lock);
    }
  }


  private void setListener(
          final ImageView charButton, final ArrayList<ImageView> charList, final int i) {
    charButton.setOnClickListener(
            new View.OnClickListener() {
              public void onClick(View v) {
                currCharacter = i;
                selectedCharacter = new GameCharacter(i);
                for (ImageView view : charList) {
                  view.setBackground(null);
                }
                charButton.setBackground(highlight);
              }
            });
  }

  public void characterSet(String name) {
    UserAccount user = app.getCurrentUser();
    JSONObject newChar = selectedCharacter.getJsonChar(name);
    String charName = newChar.keys().next();
    try {
      JSONObject charInfo = newChar.getJSONObject(charName);
      charInfo.getJSONObject("LEVEL1").put("complete", false);
      charInfo.getJSONObject("LEVEL2").put("complete", false);
      charInfo.getJSONObject("LEVEL3").put("complete", false);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    user.addCharArray(newChar);
    System.out.println("Saved!!!");
    user.saveToDb();

    // Sets current characters' name
    app.setCurrentCharacter(name);

    startGameModeSelection();
  }

  public void startGameModeSelection() {
    Intent gameModeSelectIntent = new Intent(this, GameModeActivity.class);
    startActivity(gameModeSelectIntent);
    finish();
  }
}
