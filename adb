#!/bin/bash

if [[ $1 == "-i" ]]
then
    
    echo Instalando adb
    apt install adb
    echo adb instalado

fi

echo Ponga su celular en modo USB DEBUGGING y conecte el celular
echo Conecto su celular[y/n]
read answer

if [[ $answer == "y"  ]]
then
    echo Conectando puerto
else
    exit
fi

echo Acepte la conexion en su celular

if [[ $PORT ]]
then
    adb reverse tcp:$PORT tcp:$PORT
    echo Puerto: $PORT conectado
else
    adb reverse tcp:5000 tcp:5000
    echo No hay puerto definido
    echo Puerto default: 5000 conectado
fi
