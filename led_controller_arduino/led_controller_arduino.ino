#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "https://led-controller-b8fea.firebaseio.com"
#define FIREBASE_AUTH "gSM1zijL09ojSezRzhJgh9XxGNJueWLzIunRQWUb"
#define WIFI_SSID "Cương lương khô (Đít to)"
#define WIFI_PASSWORD "khongcopass"
#define LIGHT 2 // define pint 2 for sensor

//Define FirebaseESP8266 data object
FirebaseData firebaseData;
FirebaseJson json;

void setup() {
  Serial.begin(115200);// setup Serial Monitor to display information
  pinMode(LIGHT, INPUT_PULLUP);// define pin as Input  sensor
  pinMode(D1, OUTPUT);
  digitalWrite(D1 , HIGH);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  Firebase.setwriteSizeLimit(firebaseData, "tiny");

}

void loop() {

  int light;
  int lMode = 0;
  int maxValueToTurnOn = 750;

  //Lay du lieu che do den
  if (Firebase.getInt(firebaseData, "/Mode", lMode)) {
  }
  else {
  }
  //Lay du lieu gia tri cam bien lon nhat de bat den
  if (Firebase.getInt(firebaseData, "/MaxValueToTurnOn", maxValueToTurnOn)) {
  }
  else {
  }
  Serial.print("Max: ");
  Serial.println(maxValueToTurnOn);

  //Lay gia tri cam bien
  light = analogRead(A0);

  Serial.print("\nMode: ");
  Serial.print(lMode);

  //Ghi gia tri cam bien len firebase
  Firebase.setInt(firebaseData, "/LightSensor", light);

  //Neu che do = 1 thi bat den
  if (lMode == 1) {
    digitalWrite(D1 , HIGH);
  }

  //Neu che do = 0 thi tat den
  else if (lMode == 0) {
    digitalWrite(D1 , LOW);
  }

  //Neu che do = 2 thì so sanh gia tri cam bien voi gia tri lon nhat de bat den
  else {
    //gia tri cam bien >= max thì bat den
    if (light >= maxValueToTurnOn) {
      digitalWrite(D1 , HIGH);
    }
    //gia tri cam bien < max thì tat den
    else {
      digitalWrite(D1 , LOW);
    }
  }

  delay(10);
}
