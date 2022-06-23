package com.rocky.viewstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.rocky.viewstudy.anim.anim02.v2.MessageDragBubbleView;
import com.rocky.viewstudy.anim.anim02.v2.linstenner.DragViewDisappearListener;
import com.rocky.viewstudy.anim.anim03.LoadingView;
import com.rocky.viewstudy.view.view02.QQStepView;
import com.rocky.viewstudy.view.view05.LetterSideBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_05);
        // testView02();
        //testView05();
        // testVG08();
//        Child child = new Child(11111);
//        child.getChildA();
        // testAnim01();
        //testAnim02();
        // testAnim03();
       // testAnim04();
    }

    private void testAnim04() {

    }

    private void testAnim03() {
        LoadingView loadingView = findViewById(R.id.loadView);
        //loadingView.disappear();
        loadingView.postDelayed(loadingView::disappear, 5000);
        findViewById(R.id.iv).setOnClickListener(
                v -> {
                    Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void testAnim02() {
        MessageDragBubbleView.attach(findViewById(R.id.tv), new DragViewDisappearListener() {

            @Override
            public void dismiss() {
                Toast.makeText(MainActivity.this, "onDismiss----->--->", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void testAnim01() {
        findViewById(R.id.btn).setOnClickListener(v -> {
            startActivity(new Intent(this, SecondActivity.class));
        });
    }

    private void testVG08() {
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RVAdapter());
    }

    private void testView05() {
        LetterSideBar letterBar = findViewById(R.id.letterBar);
        TextView tvLetter = findViewById(R.id.tvLetter);
        letterBar.setChangeListener(letter -> {
            // Toast.makeText(this, letter, Toast.LENGTH_SHORT).show();
            //System.out.println("select letter " + letter);
            if (TextUtils.isEmpty(letter)) {
                tvLetter.setVisibility(View.GONE);
            } else {
                tvLetter.setVisibility(View.VISIBLE);
            }
            tvLetter.setText(letter);
        });
    }

    private void testView02() {
        QQStepView viewO2 = findViewById(R.id.qqView02);
        viewO2.setStepMax(4000);
        //属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();
            viewO2.setCurrentStep((int) value);
        });
        valueAnimator.start();
    }


    class RVAdapter extends RecyclerView.Adapter<RVAdapter.Holder> {


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.tv.setText("pos----->" + position);
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "pos--->" + position, Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView tv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }
    }
}