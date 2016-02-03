package potato.beacondemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rl;
    BluetoothAdapter adpt;
    TextView tv;
    Handler hdl;
    BluetoothGatt gatt;
    boolean b1 = false, b2 = false;
    double dis1=0, dis2=0;
    final double n = 3.0;
    functions func;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        rl = (RelativeLayout) findViewById(R.id.mainRl);
        BluetoothManager mng = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        tv = (TextView) findViewById(R.id.mainTv);
        hdl = new Handler();
        img = (ImageView) findViewById(R.id.img);

        adpt = mng.getAdapter();
        adpt.enable();

        hdl.postDelayed(r, 2000);
        adpt.startLeScan(callback);
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            adpt.stopLeScan(callback);
            check();
            adpt.startLeScan(callback);
            hdl.postDelayed(r,2000);
        }
    };

    public void check(){
        if(b1&&b2){
            String text = "dis1="+dis1+"    dis2="+dis2;
                Bundle bd = new Bundle();
                bd.putString("dt", text);
                Message msg = new Message();
                msg.setData(bd);
                msg.setTarget(handler);
                msg.sendToTarget();
        }
        b1 = false;
        b2 = false;
    }

    BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            iBeaconClass.iBeacon iBeacon = iBeaconClass.fromScanData(device,rssi,scanRecord);
            System.out.println(rssi);
            String text = device.getName() + String.valueOf(rssi)+"\n";
            double power = Math.abs(rssi)-Math.abs(iBeacon.txPower);
            power = power / (10*n);
            if (text.contains("7351")) {
                /*Bundle bd = new Bundle();
                bd.putString("dt", text);
                Message msg = new Message();
                msg.setData(bd);
                msg.setTarget(handler);
                msg.sendToTarget();*/
                dis1 = Math.pow(10 ,power);
                System.out.println("dis1:"+dis1);
                b1=true;
            }
            if (text.contains("732D")){
                dis2 = Math.pow(10 ,power);
                System.out.println("dis2:"+dis2);
                b2=true;
            }
        }

    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv.append(msg.getData().getString("dt"));
            img.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int top = (int)(func.getHeight()*(dis1/(dis1+dis2)));
            System.out.println(top);
            lp.setMargins(100,top,0,0);
            img.setLayoutParams(lp);
            //change the position of picture
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
//problem: not so frequent