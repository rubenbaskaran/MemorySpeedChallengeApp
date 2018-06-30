package rubenbaskaran.com.memoryspeedchallenge.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.GameAlgorithm;
import rubenbaskaran.com.memoryspeedchallenge.BusinessLogic.LevelingSystem;
import rubenbaskaran.com.memoryspeedchallenge.R;

public class GameActivity extends Activity
{
    //region Properties
    View root;
    ArrayList<Integer> route;
    Button startGameBtn;
    int currentLevel, routeLength, startPosition, score;
    String currentRank;
    long intervalTime;
    int minimumScore, counter;
    TextView scoreTextView, counterTextView;
    Boolean GameIsActive = false;
    //endregion

    //region Constructors
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        root = LayoutInflater.from(this).inflate(R.layout.activity_game, null);
        setContentView(root);
        startGameBtn = findViewById(R.id.startGameBtn);
        EnableGridButtons(false);

        scoreTextView = findViewById(R.id.scoreTextView);
        counterTextView = findViewById(R.id.counterTextView);

        Bundle bundle = getIntent().getExtras();
        currentLevel = bundle.getInt("currentLevel");
        currentRank = bundle.getString("currentRank");
    }
    //endregion

    public void StartNewGame(View view)
    {
        score = 0;
        GameIsActive = true;
        scoreTextView.setText(String.valueOf(score));
        StartCounter();
        StartNewRound();
    }

    public void HandleUserInput(View view)
    {
        if (Integer.parseInt(view.getTag().toString()) == route.get(0))
        {
            route.remove(0);
            view.setBackground(getDrawable(R.drawable.grid_button_right_answer));
            view.setEnabled(false);
            if (route.isEmpty())
            {
                Toast.makeText(this, "Congratulations! Level completed!", Toast.LENGTH_SHORT).show();
                EnableGridButtons(false);
                startGameBtn.setEnabled(true);
                score += 100;
                scoreTextView.setText(String.valueOf(score));
                GoToNextLevel();
            }
        }
        else
        {
            view.setBackground(getDrawable(R.drawable.grid_button_wrong_answer));
            Toast.makeText(this, "Sorry! Level failed!", Toast.LENGTH_SHORT).show();
            EnableGridButtons(false);
            startGameBtn.setEnabled(true);
            GoToNextLevel();
        }
    }

    private void GoToNextLevel()
    {
        if (GameIsActive)
        {
            StartNewRound();
        }
        else
        {
            Toast.makeText(this, "Game has ended", Toast.LENGTH_LONG).show();
//            if (score > minimumScore)
//            {
//                // TODO: Increment currentLevel SharedPreference
//                // TODO: Open ScoreboardActivity
//            }
//            else
//            {
//                // TODO: Open HomeActivity
//            }
        }
    }

    //region AsyncTasks
    private class AsyncCounter extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            GameIsActive = true;
            counter = 30;
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            publishProgress();
            while (counter > 0)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCounter");
                }
                counter--;
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            counterTextView.setText(String.valueOf(counter));
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            GameIsActive = false;
        }
    }

    private class AsyncCreateRoute extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            PrepareInterface();
            SetDifficulty(currentLevel);
            GenerateRoute(routeLength);
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            for (int item : route)
            {
                Object[] output = {true, item};
                publishProgress(output);
                try
                {
                    Thread.sleep(intervalTime);
                }
                catch (InterruptedException e)
                {
                    Log.e("Error", "Error in AsyncCreateRoute");
                }
                output[0] = false;
                publishProgress(output);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
            Button btn = root.findViewWithTag(String.valueOf(values[1]));

            if (((boolean) values[0]))
            {
                btn.setBackgroundColor(Color.BLACK);
            }
            else
            {
                btn.setBackground(getDrawable(R.drawable.grid_button_background));
            }
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            EnableGridButtons(true);
            View _startPosition = root.findViewWithTag(String.valueOf(startPosition));
            HandleUserInput(_startPosition);
        }
    }
    //endregion

    //region Helper Methods
    private void StartCounter()
    {
        AsyncCounter asyncCounter = new AsyncCounter();
        asyncCounter.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void StartNewRound()
    {
        AsyncCreateRoute asyncCreateRoute = new AsyncCreateRoute();
        asyncCreateRoute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void SetDifficulty(int currentLevel)
    {
        routeLength = LevelingSystem.GetRouteLength(currentLevel);
        intervalTime = LevelingSystem.GetIntervalTime(currentLevel);
    }

    private void GenerateRoute(int routeLength)
    {
        GameAlgorithm gameAlgorithm = new GameAlgorithm();
        route = gameAlgorithm.GenerateRoute(routeLength);
        startPosition = route.get(0);
    }

    private void PrepareInterface()
    {
        ResetGridButtonsColor();
        startGameBtn.setEnabled(false);
        EnableGridButtons(false);
    }

    private void EnableGridButtons(boolean input)
    {
        root.findViewWithTag("1").setEnabled(input);
        root.findViewWithTag("2").setEnabled(input);
        root.findViewWithTag("3").setEnabled(input);
        root.findViewWithTag("4").setEnabled(input);
        root.findViewWithTag("5").setEnabled(input);
        root.findViewWithTag("6").setEnabled(input);
        root.findViewWithTag("7").setEnabled(input);
        root.findViewWithTag("8").setEnabled(input);
        root.findViewWithTag("9").setEnabled(input);
        root.findViewWithTag("10").setEnabled(input);
        root.findViewWithTag("11").setEnabled(input);
        root.findViewWithTag("12").setEnabled(input);
        root.findViewWithTag("13").setEnabled(input);
        root.findViewWithTag("14").setEnabled(input);
        root.findViewWithTag("15").setEnabled(input);
        root.findViewWithTag("16").setEnabled(input);
        root.findViewWithTag("17").setEnabled(input);
        root.findViewWithTag("18").setEnabled(input);
        root.findViewWithTag("19").setEnabled(input);
        root.findViewWithTag("20").setEnabled(input);
        root.findViewWithTag("21").setEnabled(input);
        root.findViewWithTag("22").setEnabled(input);
        root.findViewWithTag("23").setEnabled(input);
        root.findViewWithTag("24").setEnabled(input);
        root.findViewWithTag("25").setEnabled(input);
    }

    private void ResetGridButtonsColor()
    {
        root.findViewWithTag("1").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("2").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("3").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("4").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("5").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("6").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("7").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("8").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("9").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("10").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("11").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("12").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("13").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("14").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("15").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("16").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("17").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("18").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("19").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("20").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("21").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("22").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("23").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("24").setBackground(getDrawable(R.drawable.grid_button_background));
        root.findViewWithTag("25").setBackground(getDrawable(R.drawable.grid_button_background));
    }
    //endregion
}