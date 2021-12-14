package org.ailiartsua.mysummary1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TimePicker;


import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태

    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터

    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋

    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스

    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓

    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView7);
        Button onbutton = (Button) findViewById(R.id.button1);
        Button offbutton = (Button) findViewById(R.id.button2);
        Button lightbutton = (Button) findViewById(R.id.button3);
        Button reservebutton = (Button) findViewById(R.id.button4);
        Button connectbutton = (Button) findViewById(R.id.button5);
        Button exitbutton = (Button) findViewById(R.id.button6);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        onbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendData("A");
                Snackbar.make(textView,"전등을 켰습니다.", Snackbar.LENGTH_LONG).show();
            }
        });

        offbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendData("B");
                Snackbar.make(textView,"전등을 껐습니다.", Snackbar.LENGTH_LONG).show();
            }
        });

        lightbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent lightintent = new Intent(getApplicationContext(), LightActivity.class);
                startActivityForResult(lightintent, 101); //구분자
            }
        });

        reservebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Timeintent = new Intent(getApplicationContext(), TimeActivity.class);
                startActivityForResult(Timeintent, 102); //구분자
            }
        });

        connectbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                connectMessage();
            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                exitMessage();
            }
        });
    }

    public void exitMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료");
        builder.setMessage("종료하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(textView,"종료를 취소하셨습니다.", Snackbar.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void connectMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("연결");
        builder.setMessage("블루투스 연결을 하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
                    Snackbar.make(textView,"블루투스를 지원하지 않는 기기입니다.", Snackbar.LENGTH_LONG).show();
                }
                else { // 디바이스가 블루투스를 지원 할 때
                    if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                        selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
                    } else {
                        Snackbar.make(textView,"블루투스를 직접 켜주세요", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(textView,"블루투스 연결을 취소하였습니다.", Snackbar.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectBluetoothDevice() {
        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        // 페어링 된 디바이스의 크기를 저장
        int pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if(pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
        }
        // 페어링 되어있는 장치가 있는 경우
        else {
            // 디바이스를 선택하기 위한 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
            List<String> list = new ArrayList<>();
            // 모든 디바이스의 이름을 리스트에 추가
            for(BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }
            list.add("취소");
            // List를 CharSequence 배열로 변경
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);
            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 해당 디바이스와 연결하는 함수 호출
                    connectDevice(charSequences[which].toString());
                }
            });
            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
            builder.setCancelable(false);
            // 다이얼로그 생성
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void connectDevice(String deviceName) {
        // 페어링 된 디바이스들을 모두 탐색
        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();

            outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    void sendData(String text) {
        try{
            // 데이터 송신
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //값을 전달받은 메서드
        super.onActivityResult(requestCode, resultCode, data); // 파라미터는 각각 101, ResultOk, name = mike 을 뜻함

        if(requestCode == 101){
            int name_light = data.getIntExtra("light",0);
            Toast.makeText(getApplicationContext(),"밝기 : "+name_light+"단",Toast.LENGTH_LONG).show();
            String sname_light = name_light + "";
            sendData(sname_light);//1단이면 1보내고, 2단이면 2보내고 이런식,, String값을 보냄
        }
        else if(requestCode == 102) {
                int name_x = data.getIntExtra("time_m",0);
                int name_time = data.getIntExtra("time",0);

                 String sname_time = name_time + "";

            if(name_x == 1){
                    Toast.makeText(getApplicationContext(), "예약을 취소하였습니다.", Toast.LENGTH_LONG).show();
                }else{
                    if(name_time == 1){
                        sendData("C");
                    }
                    else if(name_time == 2){
                        sendData("D");
                    }
                    else if(name_time == 3){
                        sendData("E");
                    }
                    else if(name_time == 4){
                        sendData("F");
                    }
                    else if(name_time == 5){
                        sendData("G");
                    }
                    else if(name_time == 6){
                        sendData("H");
                    }
                    else if(name_time == 7){
                        sendData("I");
                    }
                    else if(name_time == 8){
                        sendData("J");
                    }
                    else if(name_time == 9){
                        sendData("K");
                    }
                    else if(name_time == 10){
                        sendData("L");
                    }
                    Toast.makeText(getApplicationContext(), sname_time+"분 뒤로 예약을 설정하였습니다. ", Toast.LENGTH_LONG).show();
                }
            }
    }
}