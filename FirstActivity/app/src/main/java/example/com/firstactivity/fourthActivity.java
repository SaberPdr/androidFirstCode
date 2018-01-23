package example.com.firstactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class fourthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fourthActivity", "Task id is " + getTaskId());
        setContentView(R.layout.activity_fourth);
    }
}
