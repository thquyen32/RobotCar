#include<ArduinoJson.h>
#include<WebSocketsClient.h>
#include<WiFi.h>

        StaticJsonDocument<200> jsonDoc;
        String jsonString;

  const char* ssid = "TuGiang";          
  const char* password = "0986712793";
  const char* server = "wss://192.168.1.14:3000";
  WebSocketsClient wSocket;
  HardwareSerial MySerial(2); 



  void webSocketEvent(WStype_t type, uint8_t *payload, size_t length)
  {
    switch(type)
    {
      case WStype_CONNECTED:
      Serial.printf("Connected to: %s\n", payload);
      Serial.println("Connected WSOCKET");
      jsonDoc["id"] = "2";
      serializeJson(jsonDoc,jsonString);
      wSocket.sendTXT(jsonString);
      break;
      case WStype_TEXT:
      Serial.printf("%s", payload);
      MySerial.printf("%s", payload);
      break;
      case WStype_ERROR:
      Serial.printf("Websocket loi: %s\n", payload);
      break;
      default:
      break;
    }
  }

void setup() {
  Serial.begin(9600);
  MySerial.begin(115200, SERIAL_8N1, 16, 17);
  WiFi.begin(ssid,password);
  Serial.print("Connecting ");
  while(WiFi.status() != WL_CONNECTED)
  {
    delay(1000);
    Serial.print(".");
  }
  Serial.print("\n");
  Serial.println(WiFi.localIP());
    wSocket.begin("192.168.1.14",3000,"/");
    wSocket.setReconnectInterval(5000);

  wSocket.onEvent(webSocketEvent);
}

void loop() {
  wSocket.loop();
  }

