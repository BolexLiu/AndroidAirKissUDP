package com.bolex.androidairkissudp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg = (TextView) findViewById(R.id.msg);
        new Thread(new MyUDP()).start();
    }
class MyUDP implements Runnable{
    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(7879);
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("创建异常");
        }

        //2、创建数据包，用于接收内容。

        while (socket != null) {
            byte[] buf = new byte[1024];
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //3、接收数据
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("异常");
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msg.setText("来自消息:"+packet.getAddress().getHostAddress() + ":" + packet.getPort()+"\n"+new String(packet.getData(), 0, packet.getLength()));
                }
            });
           // Log.d("UDP",packet.getAddress().getHostAddress() + ":" + packet.getPort());
            //System.out.println(packet.getData().toString());
            //以上语句打印信息错误，因为getData()返回byte[]类型数据，直接toString会将之序列化，而不是提取字符。应该使用以下方法：

        }
        //4、关闭连接。
        socket.close();

    }
}
}

