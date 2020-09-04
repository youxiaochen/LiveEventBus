package you.chen.liveeventbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import you.chen.liveeventbus.LogUtils;
import you.chen.liveeventbus.R;
import you.chen.liveeventbus.eventbus.LiveEventBus;

public class MainActivity extends AppCompatActivity {

    int position;

    Observer<String> everObserver;

    public static void lanuch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt1).setOnClickListener(view->Test1Activity.lanuch(this));
        findViewById(R.id.bt2).setOnClickListener(view->{
            position++;
            LiveEventBus.with(EventConstants.testEvent, String.class).postValue("Send Main event " + position);
            LiveEventBus.with(EventConstants.testEventForever, String.class).postValue("Send Main eventForever " + position);
            LiveEventBus.withSticky(EventConstants.testStickyEvent, String.class).postStickyValue("Send Main stickyEvent " + position);
            LiveEventBus.withSticky(EventConstants.testStickyEventForever, String.class).postStickyValue("Send Main StickyEventForever " + position);
        });

        LiveEventBus.with(EventConstants.testEvent, String.class).observe(this, string->{
            LogUtils.i(EventConstants.testEvent +" main : " + string);
        });


        everObserver = string->LogUtils.i(EventConstants.testEventForever +" main : " + string);
        LiveEventBus.with(EventConstants.testEventForever, String.class).observeForever(everObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.with(EventConstants.testEventForever, String.class).removeObserver(everObserver);
        LiveEventBus.removeStickyEvent(EventConstants.testStickyEvent);
        LiveEventBus.removeStickyEvent(EventConstants.testStickyEventForever);
    }

}
