package io.github.thismj.basic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import io.github.thismj.basic.databinding.ItemMainBinding;
import io.github.thismj.basic.entity.MainEntity;
import io.github.thismj.basic.library.component.BasicActivity;
import io.github.thismj.basic.library.component.BasicDelegate;
import io.github.thismj.basic.library.utils.ViewUtil;

public class MainActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<MainEntity> entities = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            MainEntity entity = new MainEntity();
            entity.name = "Name " + i;
            entity.des = "Description " + i;
            entities.add(entity);
        }

        setListData(entities, new BasicDelegate.SimpleRecycler<MainEntity, ItemMainBinding>() {

            @Override
            public void bindItemData(ItemMainBinding bind, MainEntity model, int position) {
                ViewUtil.setText(bind.tvName, model.name);
                ViewUtil.setText(bind.tvDes, model.des);
            }

            @Override
            public void onItemClick(MainEntity model, int position) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        setToolBarTitle("主页");
    }

    @Override
    public boolean enableNavigationBack() {
        return true;
    }

    @Override
    public void onRefresh(PtrFrameLayout ptrFrameLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshComplete();
            }
        }, 3000);
    }

    @Override
    public int getPageMode() {
        return BasicDelegate.PAGE_MODE_SIMPLE_LIST;
    }

    @Override
    public int getItemLayout(int position) {
        return R.layout.item_main;
    }
}
