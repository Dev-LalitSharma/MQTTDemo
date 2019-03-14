package com.mqtt.mymqttapplication.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mqtt.mymqttapplication.R;
import com.mqtt.mymqttapplication.helper.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;

    Button dataSendBtn;
    String publishTopic = "test";
    String publishMessage = "Message for test topic";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSendBtn = findViewById(R.id.dataSendBtn);

        startMqtt();
        dataSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMessage();
            }
        });

    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                Toast.makeText(MainActivity.this, mqttMessage.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }



    public void publishMessage(){

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttHelper.getMqttAndroidClient().publish(publishTopic, message);
            if(!mqttHelper.getMqttAndroidClient().isConnected()){
                addToHistory(mqttHelper.getMqttAndroidClient().getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            Log.w("Debug","Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void addToHistory(String mainText){
        Log.w("Debug","LOG: " + mainText);

    }
}