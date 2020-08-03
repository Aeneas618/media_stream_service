package com.fokantech.media_stream_service.server;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.fokantech.media_stream_service.configuration.CONFIGPARAM;
import com.fokantech.media_stream_service.configuration.OsCheck;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.*;
import org.hyperic.sigar.FileSystem;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SYSTEM_SIGAR_INFO {

    private static class SigarUtilHolder {
        private static final SYSTEM_SIGAR_INFO INSTANCE = new SYSTEM_SIGAR_INFO();
        private static final Sigar Sigar = new Sigar();
    }

    public SYSTEM_SIGAR_INFO() {
        try {
            String file = Resources.getResource("sigar/.sigar_shellrc").getFile();
            File classPath = new File(file).getParentFile();
            String path = System.getProperty("java.library.path");
            if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);
            System.out.println(path);
        } catch (Exception e) {
        }
    }

    public static final Sigar getInstance() {
        return SigarUtilHolder.Sigar;
    }

    public static final SYSTEM_SIGAR_INFO getSigarUtilInstance() {
        return SigarUtilHolder.INSTANCE;
    }


    /**
     * 计算机, 操作系统, 网络信息
     *
     * @return
     */
    public Map<String, Object> GET_COMPUTER_INFO()
    {

        Properties props = System.getProperties();
        List<String> ips = Lists.newArrayList();
        try {
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    String ip = inetAddr.getHostAddress();
                    if (StrUtil.containsAny(ip, ".")) {
                        ips.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            log.error("获取网络信息失败");
            e.printStackTrace();
        }
        Map<String, String> map = System.getenv();
        // 获取用户名
        String username = map.get("USERNAME");
        // 获取计算机名
        String computerName = map.get("COMPUTERNAME");
        // 获取计算机域名
        String userDomain = map.get("USERDOMAIN");
        Map<String, Object> properties = Maps.newHashMap();
        // 用户名
        properties.put("username", username);
        // 计算机名
        properties.put("computerName", computerName);
        // 计算机域名
        properties.put("userDomain", userDomain);

        // IP网络信息
        properties.put("ips", ips);
        // 操作系统信息
        //操作系统的名称
        properties.put("osName", props.getProperty("os.name"));
        //操作系统的构架
        properties.put("osArch", props.getProperty("os.arch"));
        //操作系统的版本
        properties.put("osVersion", props.getProperty("os.version"));
        //文件分隔符
        properties.put("fileSeparator", props.getProperty("file.separator"));
        //路径分隔符
        properties.put("pathSeparator", props.getProperty("path.separator"));
        //行分隔符
        properties.put("lineSeparator", props.getProperty("line.separator"));

        return properties;
    }

    /**
     * 获取cpu信息
     *
     * @return
     */
    public Map<String, Object> GET_CPU_INFO()
    {
        Map<String,Object> map = new HashMap<>();
        Map<String, Object> cpuInfo = Maps.newHashMap();
        try {
            Sigar sigar = getInstance();
            CpuPerc cpuPerc = sigar.getCpuPerc();
            CpuInfo[] cpuInfoList = sigar.getCpuInfoList();
            CpuInfo oneCpu = cpuInfoList[0];
            // cpu个数
            cpuInfo.put("cpuNum", cpuInfoList.length);
            // cpu型号
            cpuInfo.put("model", oneCpu.getModel());
            // cpu提供商
            cpuInfo.put("vendor", oneCpu.getVendor());
            // cpu频率
            cpuInfo.put("maxMhz", NumberUtil.decimalFormat("#.##", (oneCpu.getMhz() / 1024D)) + "GHz");
            // 缓存存储器大小
            cpuInfo.put("cacheSize", oneCpu.getCacheSize());
            // 空闲率
            cpuInfo.put("idle", CpuPerc.format(cpuPerc.getIdle()));
            // 错误率
            cpuInfo.put("nice", CpuPerc.format(cpuPerc.getNice()));
            // 系统使用率
            cpuInfo.put("sys", CpuPerc.format(cpuPerc.getSys()));
            // 用户使用率
            cpuInfo.put("user", CpuPerc.format(cpuPerc.getUser()));
            // 等待率
            cpuInfo.put("wait", CpuPerc.format(cpuPerc.getWait()));
            // 总使用率
            cpuInfo.put("combined", CpuPerc.format(cpuPerc.getCombined()));
        } catch (SigarException e) {
            e.printStackTrace();
        }

        return cpuInfo;

    }

    /**
     * 获取内存信息
     *
     * @return
     */
    public Map<String, Object> GET_MEMORY_INFO()
    {
        Map<String, Object> memInfo = Maps.newHashMap();
        try {
            Sigar sigar = getInstance();
            Mem mem = sigar.getMem();
            // 总内存
            memInfo.put("total", byteToM(mem.getTotal()));
            // 已使用内存
            memInfo.put("used", byteToM(mem.getUsed()));
            // 剩余内存
            memInfo.put("free", byteToM(mem.getFree()));
            // ram
            memInfo.put("ram", byteToM(mem.getRam()));
            // 实际使用
            memInfo.put("actualUsed", byteToM(mem.getActualUsed()));
            // 实际剩余
            memInfo.put("actualFree", byteToM(mem.getActualFree()));
            // 使用百分比
            memInfo.put("usedPercent", NumberUtil.decimalFormat("#.##", mem.getUsedPercent()) + "%");
            // 剩余百分比
            memInfo.put("freePercent", NumberUtil.decimalFormat("#.##", mem.getFreePercent()) + "%");

            // 交换区
            Swap swap = sigar.getSwap();
            // 交换区总
            memInfo.put("swapTotal", byteToM(swap.getTotal()));
            // 交换区已使用
            memInfo.put("swapUsed", byteToM(swap.getUsed()));
            // 交换区剩余
            memInfo.put("swapFree", byteToM(swap.getFree()));

        } catch (SigarException e) {
            e.printStackTrace();
        }

        return memInfo;
    }

    /**
     * 获取硬盘容量
     *
     * @return
     */
    public List<Map<String, Object>> GET_HARD_DISK_CAPACITY()
    {
        List<Map<String, Object>> diskList = Lists.newArrayList();
        try {
            Sigar sigar = getInstance();
            FileSystem[] fileSystemList = sigar.getFileSystemList();
            for (FileSystem fileSystem : fileSystemList) {
                Map<String, Object> disk = Maps.newHashMap();
                // 盘符名称
                disk.put("devName", fileSystem.getDevName());
                // 盘符路径
                disk.put("dirName", fileSystem.getDirName());
                // 盘符标志
                disk.put("flags", fileSystem.getFlags());
                // 盘符类型(NTFS, FAT32)
                disk.put("sysTypeName", fileSystem.getSysTypeName());
                // 盘符类型名
                disk.put("typeName", fileSystem.getTypeName());
                // 盘符文件系统类型
                disk.put("type", fileSystem.getType());
                if (fileSystem.getType() == FileSystem.TYPE_LOCAL_DISK) {
                    FileSystemUsage usage = null;
                    try {
                        usage = sigar.getFileSystemUsage(fileSystem.getDirName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("获取磁盘信息出错!dirName=" + fileSystem.getDirName());
                    }
                    if (ObjectUtil.isNotNull(usage)) {
                        //总大小
                        disk.put("total", byteToG(usage.getTotal() * 1024L));
                        //已使用
                        disk.put("used", byteToG(usage.getUsed() * 1024L));
                        //剩余
                        disk.put("free", byteToG(usage.getFree() * 1024L));
                        // 可使用
                        disk.put("avail", byteToG(usage.getAvail() * 1024L));
                        //已使用百分比
                        disk.put("usePercent", NumberUtil.formatPercent(usage.getUsePercent(), 2));
                        disk.put("reads", usage.getDiskReads());
                        disk.put("writes", usage.getDiskWrites());
                        diskList.add(disk);
                    }

                }
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return diskList;
    }

    /**
     * 把byte转换成M
     *
     * @param bytes
     * @return
     */
    public static String byteToM(long bytes) {
        String mb = NumberUtil.decimalFormat("#.###", (bytes / 1024.0 / 1024.0)) + "MB";
        return mb;
    }

    /**
     * 把byte转换成G
     *
     * @param bytes
     * @return
     */
    public static String byteToG(long bytes) {
        String mb = NumberUtil.decimalFormat("#.###", (bytes / 1024.0 / 1024.0 / 1024.0)) + "GB";
        return mb;
    }

    /**
     * Linux 网口流量采集
     * @return
     */
    public List<Object> ACQUISITION_OF_NETWORK_TEAFFIC () {
        Process pro1, pro2;
        Runtime r = Runtime.getRuntime();
        List<Object> objects = new ArrayList<>();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;int index =0;
            while ((line = in1.readLine()) != null) {
                index++;
                line = line.trim();
                if (line.startsWith(new CONFIGPARAM().defaultSetting())) {
                    String test = test(line,",");
                    String s = test1(test);
                    String regEx = "[',']+";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(s);
                    String trim = m.replaceAll("/").trim();
                    String[] split = trim.split("/");
                    inSize1 = Long.parseLong(split[1]);
                    outSize1 = Long.parseLong(split[9]);
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.error("NetUsage休眠时发生InterruptedException. " + e.getMessage());
                log.error(sw.toString());
            }
            //第二次采集流量数据
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0, outSize2 = 0;
            while ((line = in2.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(new CONFIGPARAM().defaultSetting())) {
                    String test = test(line,",");
                    String s = test1(test);
                    String regEx = "[',']+";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(s);
                    String trim = m.replaceAll("/").trim();
                    String[] split = trim.split("/");
                    inSize2 =+ Long.parseLong(split[1]);
                    outSize2 =+ Long.parseLong(split[9]);
                    break;
                }
            }
            if (inSize1 != 0 && outSize1 != 0 && inSize2 != 0 && outSize2 != 0) {
                float Receive = (float) (inSize2 - inSize1) * 8 / 1024 / 1024;
                float Transmit = (float) (outSize2 - outSize1) * 8 / 1024 / 1024;
                objects.add(new DecimalFormat(".0000").format(Receive) + "Mb");
                objects.add(new DecimalFormat(".0000").format(Transmit) + "Mb");
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("NetUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return objects;
    }


    private String test(String line,String flag){
        String regEx = "[' ']+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(line);
        return m.replaceAll(flag).trim();
    }

    private String test1(String line){
        String regEx = "['|']+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(line);
        return m.replaceAll(",").trim();
    }

}


