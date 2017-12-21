package nongbang.hg.nongbang;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import nongbang.hg.nongbang.StaticClass.StaticVariable;

public class APRQActivity extends AppCompatActivity {
    private Button confirm,cancel;
    private ImageButton choice;
    private View CustomView;
    private DatePicker datePicker;
    private TextView settime,setrepeat;
    private CheckBox notreminded,monday,tuesday,wednseday,thursday,friday,saturday,sunday;
    private ChekedListener chekedListener;
    private int mhour,mminute;
    private String repeat;
    private TextView remind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aprq);
        datePicker = (DatePicker) findViewById(R.id.aprq_datepicker);
        settime=(TextView)findViewById(R.id.aprq_settime);
        setrepeat=(TextView)findViewById(R.id.aprq_setrepeat);
        choice=(ImageButton)findViewById(R.id.aprq_choice);
        remind=(TextView)findViewById(R.id.aprq_remind) ;
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariable.APRTIME=mhour+":"+mminute;
                if (StaticVariable.APRTIME.compareTo("0:0")==0){
                    Toast.makeText(APRQActivity.this,"请设置时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (setrepeat.getText().toString().compareTo("设置重复")==0){
                    StaticVariable.APRQDATE=datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                    StaticVariable.APRQREPEAT = "";
                }else {
                    StaticVariable.APRQREPEAT = repeat;
                    StaticVariable.APRQDATE="";
                }
                AddAnPaiActivity.setTitle();
                APRQActivity.this.finish();
            }
        });


        this.datePicker.init(this.datePicker.getYear(), this.datePicker.getMonth(), this.datePicker.getDayOfMonth(), new OnDateChangedListenerImpl());

        chekedListener=new ChekedListener();
        setrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder=myBuilder(APRQActivity.this);
                final AlertDialog dialog=builder.show();
                notreminded=(CheckBox)CustomView.findViewById(R.id.aprq_notreminded);
                monday=(CheckBox)CustomView.findViewById(R.id.aprq_monday);
                monday.setOnCheckedChangeListener(chekedListener);
                tuesday=(CheckBox)CustomView.findViewById(R.id.aprq_tuesday);
                tuesday.setOnCheckedChangeListener(chekedListener);
                wednseday=(CheckBox)CustomView.findViewById(R.id.aprq_wednseday);
                wednseday.setOnCheckedChangeListener(chekedListener);
                thursday=(CheckBox)CustomView.findViewById(R.id.aprq_thursday);
                thursday.setOnCheckedChangeListener(chekedListener);
                friday=(CheckBox)CustomView.findViewById(R.id.aprq_friday);
                friday.setOnCheckedChangeListener(chekedListener);
                saturday=(CheckBox)CustomView.findViewById(R.id.aprq_saturday);
                saturday.setOnCheckedChangeListener(chekedListener);
                sunday=(CheckBox)CustomView.findViewById(R.id.aprq_sunday);
                sunday.setOnCheckedChangeListener(chekedListener);
                notreminded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            monday.setChecked(false);
                            tuesday.setChecked(false);
                            wednseday.setChecked(false);
                            thursday.setChecked(false);
                            friday.setChecked(false);
                            saturday.setChecked(false);
                            sunday.setChecked(false);
                        }
                    }
                });
                confirm = (Button)CustomView.findViewById(R.id.aprq_confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        repeat="";
                        if (monday.isChecked()) {
                            repeat += "一/";
                        }
                        if (tuesday.isChecked()){
                            repeat += "二/";
                        }
                        if (wednseday.isChecked()){
                            repeat += "三/";
                        }
                        if (thursday.isChecked()){
                            repeat += "四/";
                        }
                        if (friday.isChecked()){
                            repeat += "五/";
                        }
                        if (saturday.isChecked()){
                            repeat += "六/";
                        }
                        if (sunday.isChecked()){
                            repeat += "日";
                        }
                        if (notreminded.isChecked()){
                            datePicker.setEnabled(true);
                            setrepeat.setText("设置重复");
                            remind.setVisibility(View.GONE);
                        }else {
                            datePicker.setEnabled(false);
                            setrepeat.setText(repeat);
                            remind.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }
                });
                cancel = (Button)CustomView.findViewById(R.id.aprq_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog time = new TimePickerDialog(APRQActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        settime.setText(hourOfDay+"点"+minute+"分");
                        mhour=hourOfDay;
                        mminute=minute;
                    }
                }, 18, 00, true);
                time.show();
            }
        });

    }

    protected Builder myBuilder(APRQActivity aprqActivity) {
        final LayoutInflater inflater=APRQActivity.this.getLayoutInflater();
        AlertDialog.Builder builder=new AlertDialog.Builder(aprqActivity);
        CustomView=inflater.inflate(R.layout.aprq_repeat, null);
        return builder.setView(CustomView);
    }


    public class ChekedListener implements   CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if (notreminded.isChecked())
                    notreminded.setChecked(false);
            }
        }
    }

    private class OnDateChangedListenerImpl implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //APRQActivity.this.setDateTime();
        }
    }
}
