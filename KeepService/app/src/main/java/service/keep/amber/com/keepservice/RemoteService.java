package service.keep.amber.com.keepservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by punisher on 2017/5/15.
 */

public class RemoteService extends Service {
    private Mybinder  mybinder;
    private  Myconn  myconn;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mybinder=new Mybinder();
        if (myconn==null){
            myconn=new Myconn();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.bindService(new Intent(RemoteService.this,LocalService.class),myconn, Context.BIND_IMPORTANT);
    }


    class Mybinder extends ProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "RemoteService";
        }
    }

    class  Myconn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("info","本地服务连接成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(RemoteService.this,"本地服务被杀死",Toast.LENGTH_LONG).show();
            //启动本地服务
            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
            //绑定本地服务
            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),myconn,Context.BIND_IMPORTANT);
        }
    }
}
