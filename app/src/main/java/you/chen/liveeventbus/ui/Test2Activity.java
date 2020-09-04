package you.chen.liveeventbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import you.chen.liveeventbus.LogUtils;
import you.chen.liveeventbus.R;
import you.chen.liveeventbus.databinding.ActTest2Binding;
import you.chen.liveeventbus.eventbus.LiveEventBus;

public class Test2Activity extends AppCompatActivity {

    ActTest2Binding binding;

    Observer<String> everObserver, stickyEverObserver;

    public static void lanuch(Context context) {
        context.startActivity(new Intent(context, Test2Activity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_test2);
        binding.bt.setOnClickListener(view->Test3Activity.lanuch(this));
        binding.setLifecycleOwner(this);
        everObserver = string-> LogUtils.i(EventConstants.testEventForever +" Test2Activity : " + string);
        stickyEverObserver = string->LogUtils.i(EventConstants.testStickyEventForever +" Test2Activity : " + string);


        LiveEventBus.with(EventConstants.testEvent, String.class).observe(this, string->{
            LogUtils.i(EventConstants.testEvent +" Test2Activity : " + string);
        });
        LiveEventBus.withSticky(EventConstants.testStickyEvent, String.class).observe(this, string->{
            LogUtils.i(EventConstants.testStickyEvent +" Test2Activity : " + string);
        });
        LiveEventBus.with(EventConstants.testEventForever, String.class).observeForever(everObserver);
        LiveEventBus.withSticky(EventConstants.testStickyEventForever, String.class).observeForever(stickyEverObserver);

        setTitle("Test2Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.with(EventConstants.testEventForever, String.class).removeObserver(everObserver);
        LiveEventBus.withSticky(EventConstants.testStickyEventForever, String.class).removeObserver(stickyEverObserver);

        binding.unbind();
        binding = null;
    }
}
