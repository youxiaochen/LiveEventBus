package you.chen.liveeventbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import you.chen.liveeventbus.eventbus.LiveEventBus;
import you.chen.liveeventbus.LogUtils;
import you.chen.liveeventbus.R;
import you.chen.liveeventbus.databinding.ActTest3Binding;

public class Test3Activity extends AppCompatActivity implements View.OnClickListener {

    ActTest3Binding binding;

    Observer<String> everObserver, stickyEverObserver;

    public static void lanuch(Context context) {
        context.startActivity(new Intent(context, Test3Activity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_test3);
        binding.setLifecycleOwner(this);
        binding.bt1.setOnClickListener(this);
        binding.bt2.setOnClickListener(this);
        binding.bt3.setOnClickListener(this);
        binding.bt4.setOnClickListener(this);


        everObserver = string-> LogUtils.i(EventConstants.testEventForever +" Test3Activity : " + string);
        stickyEverObserver = string->LogUtils.i(EventConstants.testStickyEventForever +" Test3Activity : " + string);


        LiveEventBus.with(EventConstants.testEvent, String.class).observe(this, string->{
            LogUtils.i(EventConstants.testEvent +" Test3Activity : " + string);
        });
        LiveEventBus.withSticky(EventConstants.testStickyEvent, String.class).observe(this, string->{
            LogUtils.i(EventConstants.testStickyEvent +" Test3Activity : " + string);
        });
        LiveEventBus.with(EventConstants.testEventForever, String.class).observeForever(everObserver);
        LiveEventBus.withSticky(EventConstants.testStickyEventForever, String.class).observeForever(stickyEverObserver);

        setTitle("Test3Activity");
    }

    int position;

    @Override
    public void onClick(View v) {
        position++;
        switch (v.getId()) {
            case R.id.bt1:
                LiveEventBus.with(EventConstants.testEvent, String.class).setValue("Send Test4 Event " + position);
                break;
            case R.id.bt2:
                LiveEventBus.with(EventConstants.testEventForever, String.class).setValue("Send Test4 Event " + position);
                break;
            case R.id.bt3:
                LiveEventBus.withSticky(EventConstants.testStickyEvent, String.class).setStickyValue("Send Test4 Event " + position);
                break;
            case R.id.bt4:
                LiveEventBus.withSticky(EventConstants.testStickyEventForever, String.class).setStickyValue("Send Test4 Event " + position);
                break;
        }
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
