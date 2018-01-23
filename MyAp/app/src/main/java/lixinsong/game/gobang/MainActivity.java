/*  
 * Five In a Row. (五子棋)  
 * 这是一个简单的五子棋程序，是我自己的一个练习，贴出来跟大家分享。  
 * 希望跟大家一起多交流。 我的GoogleTalk: lixinso <at> gmail.com  
 *   
 *   
 */
//----------------------   
//TBD:AI，悔棋   
//---------------------   

package lixinsong.game.gobang;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
//这是主程序，继承自Activity，实现onCreate方法。：   


public class gobang extends Activity {
    GobangView gbv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        gbv = (GobangView)this.findViewById(R.id.gobangview);
        gbv.setTextView((TextView)this.findViewById(R.id.text));

    }