package com.fokantech.media_stream_service.server;

import lombok.SneakyThrows;

import java.util.*;

public class SYSTEM_THREAD extends Thread {
    /**
     *  SERVER INFORMATION
     */
    private static Queue<Map<String, Object>> COMPUTER_INFO = new LinkedList<Map<String, Object>>();
    private static Queue<Map<String,Object>> CPU_INFO = new LinkedList<Map<String,Object>>();
    private static Queue<Map<String,Object>> MEMORY_INFO = new LinkedList<Map<String,Object>>();
    private static Queue<List<Map<String, Object>>> DISK_INFO = new LinkedList<List<Map<String, Object>>>();
    private static Queue<Map<String,Object>> NETWORK_INFO = new LinkedList<Map<String,Object>>();
    private static SYSTEM_SIGAR_INFO SYSTEM_SIGAR_INFO = new SYSTEM_SIGAR_INFO();
    /**
     *  GET DATA
     */
    @Override
    public void run() {
        while (true){
            LIMIT_LENGHT();
            COMPUTER_INFO.add(SYSTEM_SIGAR_INFO.GET_COMPUTER_INFO());
            CPU_INFO.add(SYSTEM_SIGAR_INFO.GET_CPU_INFO());
            MEMORY_INFO.add(SYSTEM_SIGAR_INFO.GET_MEMORY_INFO());
            DISK_INFO.add(SYSTEM_SIGAR_INFO.GET_HARD_DISK_CAPACITY());
            NETWORK_INFO.add(GET_NETWORK_LIST());
        }
    }

    /**
     * handle function
     */
    private void LIMIT_LENGHT()
    {
        if (COMPUTER_INFO.size()>30){
            COMPUTER_INFO.remove();
        }
        if (CPU_INFO.size()>30){
            CPU_INFO.remove();
        }
        if (MEMORY_INFO.size()>30){
            MEMORY_INFO.remove();
        }
        if (DISK_INFO.size()>30){
            DISK_INFO.remove();
        }
        if (NETWORK_INFO.size() > 30){
            NETWORK_INFO.remove();
        }
    }

    @SneakyThrows
    private Map<String, Object> GET_NETWORK_LIST()
    {
        Map<String,Object> map = new HashMap<>();
        List<Object> objects = SYSTEM_SIGAR_INFO.ACQUISITION_OF_NETWORK_TEAFFIC();
        map.put("Receive",objects.get(0));
        map.put("Transmit",objects.get(1));
        return map;
    }
    /**
     * External interface
     */
    public Object[] GET_COMPUTER_INFO_LIST()
    {
        Object[] objects = COMPUTER_INFO.toArray();
        return objects;
    }

    public Object[] GET_CPU_INFO_LIST()
    {
        Object[] objects = CPU_INFO.toArray();
        return objects;
    }

    public Object[] GET_MEMORY_INFO_LIST()
    {
        Object[] objects = MEMORY_INFO.toArray();
        return objects;
    }

    public Object[] GET_DISK_INFO_LIST()
    {
        Object[] objects = DISK_INFO.toArray();
        return objects;
    }

    public Object[] GET_NETWORK_INFO_LIST()
    {
        Object[] objects = NETWORK_INFO.toArray();
        return objects;
    }
}
