package io.github.thismj.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.thismj.basic.databinding.ItemMainBinding;
import io.github.thismj.basic.entity.MainEntity;
import io.github.thismj.basic.library.component.BasicActivity;

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

        setListData(entities);

        setToolBarTitle("主页");
    }

    @Override
    public int getActivityMode() {
        return ACTIVITY_MODE_SIMPLE_LIST;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_main;
    }

    @Override
    public <E> void bindItemData(View itemView, E entity, int position) {
        super.bindItemData(itemView, entity, position);

        ItemMainBinding bind = getBindView(itemView);

        bind.tvName.setText(((MainEntity) entity).name);
        bind.tvDes.setText(((MainEntity) entity).des);

        bind.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    public int getTransitionMode() {
        return PENDING_TRANSITION_ZOOM;
    }
}
