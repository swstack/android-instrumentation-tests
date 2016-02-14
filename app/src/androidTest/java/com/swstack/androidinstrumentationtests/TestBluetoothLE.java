package com.swstack.androidinstrumentationtests;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestBluetoothLE extends AndroidTestCase {

    public static final int STATE_DISCONNECTED  = 0;
    public static final int STATE_CONNECTING    = 1;
    public static final int STATE_CONNECTED     = 2;
    public static final int STATE_DISCONNECTING = 3;

    public static final String targetDevice = "TESTBEAN";

    protected void setUp() {
    }

    protected void tearDown() {
    }

    private BluetoothDevice findDevice(String name) throws Exception {
        /**
         * Begin BLE discovery and return a BluetoothDevice object with `name`
         */

        final CountDownLatch latch = new CountDownLatch(1);
        final List<BluetoothDevice> devices = new ArrayList<>();
        BluetoothAdapter.getDefaultAdapter().startLeScan(new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                String name = device.getName();
                if (name != null && name.equals(targetDevice)) {
                    System.out.println("Found device: " + targetDevice);
                    devices.add(device);
                    latch.countDown();
                }
            }
        });

        latch.await(20, TimeUnit.SECONDS);
        if (devices.isEmpty()) {
            throw new Exception("Couldn't find device: " + name);
        }
        return devices.get(0);
    }

    public void testBleApi() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        BluetoothDevice device = findDevice(targetDevice);
        boolean reconnect = true;

        final BluetoothGatt gattServer = device.connectGatt(getContext(), reconnect, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                System.out.println("onConnectionStateChange: " + newState);

                switch (newState) {

                    case STATE_CONNECTING:
                        System.out.println("Device connecting");
                        break;

                    case STATE_CONNECTED:
                        System.out.println("Device connected");
                        latch.countDown();
                        break;

                    case STATE_DISCONNECTING:
                        System.out.println("Device disconnecting");
                        break;

                    case STATE_DISCONNECTED:
                        System.out.println("Device disconnected");
                        break;
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                System.out.println("onServicesDiscovered");
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                System.out.println("onCharacteristicRead");
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                System.out.println("onCharacteristicWrite");
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                System.out.println("onCharacteristicChanged");
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                System.out.println("onDescriptorRead");
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                System.out.println("onDescriptorWrite");
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                System.out.println("onReadRemoteRssi");
            }
        });

        latch.await(20, TimeUnit.SECONDS);  // Wait for connection
        gattServer.disconnect();
    }
}
