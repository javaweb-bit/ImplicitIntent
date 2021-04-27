package com.example.implicitintent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//  이벤트 연결 방법 2: 액티비티 내의 모든 위젯의 이벤트를 액티비티가 총괄 처리
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final int REQUEST_CALL_PERMISSION = 200;

    //  동적 권한 요청의 결과 콜백 메서드
    //  new String[] { Manifest.permission.CALL_PHONE },
    @Override
    public void onRequestPermissionsResult(int requestCode, //  요청 코드 requestPermissions에서 설정한 코드
                                           @NonNull String[] permissions,   //  요청 권한 문자열 배열
                                           @NonNull int[] grantResults) {   //  허가 결과
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  권한을 동의하였다
                callPhone();
            } else {
                //  권한 동의 안했음
                Toast.makeText(MainActivity.this,
                        "통화 권한이 없습니다.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  이벤트 연결 방법 1: 객체를 찾아서 개별 이벤트리스너를 등록
        View btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  CALL_PHONE 권한이 필요하므로 사용자에게 허가를 얻자(동적 권한 획득)
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    //  사용자가 권한을 허용한 상태
                    callPhone();
                } else {
                    //  사용자가 권한을 허용하지 않은 상태
                    //  권한 허용을 위한 다이얼로그를 호출
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] { Manifest.permission.CALL_PHONE },
                            REQUEST_CALL_PERMISSION);   //  요청 코드 -> 콜백으로 돌려받을 코드
                }
//                Toast.makeText(MainActivity.this,
//                        "전화를 겁니다",
//                        Toast.LENGTH_LONG)
//                        .show();
//                callPhone();
            }
        });


//        View btnCall = findViewById(R.id.btnCall);
//        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //  btnWeb, btnMap, btnShare의 Click이벤트를 총괄 처리
        int target = v.getId();

        if (target == R.id.btnWeb) {
            //  웹사이트 표시 (암시적 인텐트)
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://naver.com"));
            startActivity(intent);
        } else if (target == R.id.btnMap) {
            //  지도 표시
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:37.4927338,127.0284288"));    //  강남역
            startActivity(intent);
        } else if (target == R.id.btnShare) {
            //  타 앱의 액티비티 호출
            //  Custom Action Intent
            Intent intent = new Intent("com.example.android.action.SHARE_ACTION");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //  사용자 정의 액션을 암시적으로 호출할 때
            //      정의 액션을 수행할 앱이 설치 되지 않을 가능성 염두
            //  우리의 인텐트를 처리할 수 있는 앱이 있는지 확인
            if (intent.resolveActivity(getPackageManager()) != null) {  //  처리할 앱을 발견
                startActivity(intent);
            } else {    //  인텐트 처리할 앱이 없음
                Toast.makeText(MainActivity.this,
                        "해당 인텐트를 실행할 액티비티 없음!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void callPhone() throws SecurityException {
        //  암시적 인텐트: 타겟을 명확히 지정하는 것이 아닌
        //      하고자 하는 Action과 Data 지정
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:010-555-1234"));
        startActivity(intent);
    }
}