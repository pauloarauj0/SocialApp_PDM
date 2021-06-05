
package com.example.socialapp.bluetooth;

/**
 * Constantes usadas por BluetoothChatService e a UI
 */
public interface Constants {

    // tipos de mensagens do handler de BluetoothChatService
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
    int MESSAGE_POST = 6;

    // nomes das chaves para o handler de BluetoothChatService
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";

    //caracteres especiais para identificacao de mensagens

    char CHAR_POST = '@';
    char CHAR_NAME = '#';
    String STR_SEPARATOR = " " ;

}
