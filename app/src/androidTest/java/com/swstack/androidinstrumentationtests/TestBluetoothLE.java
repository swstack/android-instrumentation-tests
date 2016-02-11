package com.swstack.androidinstrumentationtests;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestBluetoothLE extends AndroidTestCase {

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testBleApi() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);

        BluetoothAdapter.getDefaultAdapter().startLeScan(new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                System.out.println("onLeScan callback fired!!");

                if (device.getName().equals("TESTBEAN")) {

                    BluetoothGatt gattServer = device.connectGatt(getContext(), false, new BluetoothGattCallback() {
                        @Override
                        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                            System.out.println("onConnectionStateChange: " + newState);
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

                }
            }
        });

        latch.await(120, TimeUnit.SECONDS);
    }
}
