//Circuit:
 //* Ethernet shield attached to pins 10, 11, 12, 13
 //* DHT11 Sensor connected to Pin 2

 //Based on code by David A. Mellis& Tom Igoe


/*-----( Import needed libraries )-----*/
#include <SPI.h>
#include <Ethernet.h>
#include <dht.h>
#include <Wire.h>

/*-----( Declare Constants and Pin Numbers )-----*/
#define DHTPIN A0  // The Temperature/Humidity sensor

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

/*-----( Declare objects )-----*/
IPAddress ip(169,254,67,11);

// Initialize the Ethernet server library
// with the IP address and port you want to use 
// (port 80 is default for HTTP):
EthernetServer server(80);

dht DHT;  //The Sensor Object
/*-----( Declare Variables )-----*/



void setup()   /****** SETUP: RUNS ONCE ******/
{
// Open serial communications and wait for port to open:
Serial.begin(9600);
while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }


// start the Ethernet connection and the server:
Ethernet.begin(mac, ip);
server.begin();
Serial.print("server is at ");
Serial.println(Ethernet.localIP());
}//--(end setup )---


void loop()   /*----( LOOP: RUNS OVER AND OVER AGAIN )----*/
{
// listen for incoming clients
EthernetClient client = server.available();
if (client) {
Serial.println("new client");
// an http request ends with a blank line
boolean currentLineIsBlank = true;
while (client.connected()) {
if (client.available()) {
char c = client.read();
Serial.write(c);
// if you've gotten to the end of the line (received a newline
// character) and the line is blank, the http request has ended,
// so you can send a reply
if (c == '\n' &&currentLineIsBlank) {
// send a standard http response header
client.println("HTTP/1.1 200 OK");
client.println("Content-Type: text/html");
client.println("Connnection: close");
client.println();
client.println("<!DOCTYPE HTML>");
client.println("<html>");
// add a meta refresh tag, so the browser pulls again every 5 seconds:
client.println("<meta http-equiv=\"refresh\" content=\"5\">");
client.print("BHEL....!");   
client.println("<br />");    

/*----(Get sensor reading, calculate and print results )-----------------*/

int chk = DHT.read11(DHTPIN);

Serial.print("Read sensor: ");
switch (chk)
          {
case 0: 
Serial.println("OK"); 
break;
case -1: 
Serial.println("Checksum error"); 
break;
case -2: 
Serial.println("Time out error"); 
break;
default: 
Serial.println("Unknown error"); 
break;
          }  

client.print("Temperature (C): ");
client.println(DHT.temperature, 1);  
client.println("<br />");  


client.print("Humidity (%): ");
client.println(DHT.humidity, 0);  
client.println("<br />");   





/*--------( End Sensor Read )--------------------------------*/
client.println("</html>");
break;
        }
if (c == '\n') {
// you're starting a new line
currentLineIsBlank = true;
        } 
else if (c != '\r') {
// you've gotten a character on the current line
currentLineIsBlank = false;
        }
      }
    }
// give the web browser time to receive the data
delay(1);
// close the connection:
client.stop();
Serial.println("client disonnected");
  }
} // END Loop

/*-----( Declare User-written Functions )-----*/
//



