package com.example.proyectododoantho;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Factura extends AppCompatActivity {
    TextView textoFactura;
    String msg="";
    String encabezado="Drive Aliments S.A";
    String correo="Correo: facturaElectronico@drivealiments.co.cr";
    Timestamp fechaEmision;
    int numeroDeFactura;
    int numeroDeRecibo;
    String numeroDeFacturaStr="Numero de Factura = ";
    String numeroDeReciboStr="Numero de Recibo = ";
    String linea="---------------------------------------------";
    String fecha="Fecha : ";
    String formatoDescription="ITEM-------------QUANTITY";
    String Total="TOTAL : ";
    String IVA="IVA : ";
    String precioProducto="Producto : ";
    String Plato,cantidad,monto;
    String localName;
    String valorProductoStr,ivaStr;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);
        try {
            Button openButton = (Button) findViewById(R.id.btnOpen);
            Button sendButton = (Button) findViewById(R.id.btnSend);
            textoFactura=findViewById(R.id.textView3);
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            Plato= extras.getString("Name");
            cantidad=extras.getString("Cantidad");
            monto=extras.getString("Monto");
            localName=extras.getString("Local");
            valorProductoStr=extras.getString("IVA");
            ivaStr=extras.getString("Producto");
            numeroDeFactura=ThreadLocalRandom.current().nextInt(0, 10000 + 1);
            numeroDeFacturaStr+=String.valueOf(numeroDeFactura);
            numeroDeRecibo= ThreadLocalRandom.current().nextInt(0, 10000 + 1);
            numeroDeReciboStr+=String.valueOf(numeroDeRecibo);
            fechaEmision= new Timestamp(System.currentTimeMillis());
            msg += encabezado;
            msg += "\n";
            msg += correo;
            msg += "\n";
            msg+=numeroDeFacturaStr;
            msg += "\n";
            msg+=numeroDeReciboStr;
            msg += "\n";
            msg+=fecha + fechaEmision.toString();
            msg += "\n";
            msg+=localName;
            msg += "\n";
            msg+=linea;
            msg += "\n";
            msg+= formatoDescription;
            msg += "\n";
            msg+=Plato + "-------------" + cantidad;
            msg += "\n";
            msg+= precioProducto + valorProductoStr + " colones";
            msg += "\n";
            msg+= IVA + ivaStr + " colones";
            msg += "\n";
            msg+=Total + monto + " colones";
            //msg += textoFactura.getText().toString();
            msg += "\n";
            Log.i("Bluetooth",msg);
            textoFactura.setText(msg);

            findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        findBT();
                        openBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sendData();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    void sendData() throws IOException {
        try {
            if(msg!=null){
                mmOutputStream.write(msg.getBytes());
                Log.i("Bluetooth",msg);
                Log.i("BLUETOOTH", "sendData: DATA SENT");

            }else{
                Toast.makeText(getApplicationContext(),"BLUETOOTH ERROR, RESTART APP",Toast.LENGTH_SHORT).show();
            }

            // tell the user data were sent
            //myLabel.setText("Data sent.");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
            Log.i("BLUETOOTH", "closeBT: BLUETOOTH CLOSED");
            Toast.makeText(getApplicationContext(),"BLUETOOTH CLOSED",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    Log.w("name",device.getName().toString());
                    if (device.getName().equals("PTP-III")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            //myLabel.setText("Bluetooth device found.");
            Log.i("BLUETOOTH", "findBT: DEVICE FOUND ");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    @SuppressLint("MissingPermission")
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //myLabel.setText("Bluetooth Opened");
            Log.i("BLUETOOTH", "openBT: BLUETOOTH OPENED ");
            Toast.makeText(getApplicationContext(),"BLUETOOTH OPENED",Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                                Log.i("BTDATA", "run: "+data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Return(View view) {
        try {
            closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(Factura.this,AlimentsMenu.class);
        startActivity(i);
    }
}