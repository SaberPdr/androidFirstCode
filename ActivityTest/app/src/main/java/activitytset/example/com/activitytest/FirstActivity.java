package activitytset.example.com.activitytest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.data;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String returnedData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnedData);
                }
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               //Toast.makeText(FirstActivity.this, "You clicked 按钮1", Toast.LENGTH_SHORT).show();
               //Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
               //Intent intent = new Intent("activitytset.example.com.activitytest.ACTION_START");
               //intent.addCategory("activitytset.example.com.activitytest.MY_CATEGORY");
               //Intent intent = new Intent(Intent.ACTION_VIEW);
               //intent.setData(Uri.parse("http://www.baidu.com"));
               //Intent intent = new Intent(Intent.ACTION_DIAL);
               //intent.setData(Uri.parse("tel:10086"));
               //String data = "Hello SecondActivity";
               Intent intent= new Intent(FirstActivity.this, SecondActivity.class);
               //intent.putExtra("extra_data",data);
               startActivityForResult(intent, 1);
               startActivity(intent);
           }
        });


        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "你点击了Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "你点击了Remove", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}
