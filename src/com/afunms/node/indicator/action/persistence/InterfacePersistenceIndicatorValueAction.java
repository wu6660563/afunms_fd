/*
 * @(#)InterfacePersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.Interfacecollectdata;

/**
 * ClassName:   InterfacePersistenceIndicatorValueAction.java
 * <p>{@link InterfacePersistenceIndicatorValueAction} Interface 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 15:24:43
 */
public class InterfacePersistenceIndicatorValueAction extends PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_interface_data_temp";

    private final static String TABLE_NAME_PERSISTENCE_PORT_STATUS =  "portstatus";

    private final static String TABLE_NAME_PERSISTENCE_ALLUTILHDX =  "allutilhdx";
    
    private final static String TABLE_NAME_PERSISTENCE_UTILHDX =  "utilhdx";

    private final static String TABLE_NAME_PERSISTENCE_UTILHDXPERC =  "utilhdxperc";

    private final static String TABLE_NAME_PERSISTENCE_DISCARDSPERC =  "discardsperc";

    private final static String TABLE_NAME_PERSISTENCE_ERRORSPERC =  "errorsperc";

    private final static String TABLE_NAME_PERSISTENCE_PACKS =  "packs";

    private final static String TABLE_NAME_PERSISTENCE_INPACKS =  "inpacks";

    private final static String TABLE_NAME_PERSISTENCE_OUTPACKS =  "outpacks";

    /**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToDB() {
        Hashtable<String, Vector<Interfacecollectdata>> interfaceHashtable = (Hashtable<String, Vector<Interfacecollectdata>>) getIndicatorValue().getValue();
        
        Vector<Interfacecollectdata> allutilhdxVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("allutilhdx");
        Vector<Interfacecollectdata> utilhdxpercVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("utilhdxperc");
        Vector<Interfacecollectdata> utilhdxVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("utilhdx");
        Vector<Interfacecollectdata> discardspercVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("discardsperc");
        Vector<Interfacecollectdata> errorspercVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("errorsperc");
        Vector<Interfacecollectdata> packsVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("packs");
        Vector<Interfacecollectdata> inpacksVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("inpacks");
        Vector<Interfacecollectdata> outpacksVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("outpacks");
        Vector<Interfacecollectdata> interfaceVector = (Vector<Interfacecollectdata>) interfaceHashtable.get("interface");

        List<String[]> list = new ArrayList<String[]>();

        // 开始设置综合流速
        List<String[]> allUtilHdxList = new ArrayList<String[]>();
        if (allutilhdxVector != null) {
            for (Interfacecollectdata allUtilHdx : allutilhdxVector) {
                list.add(createDataTempArray(allUtilHdx));
                allUtilHdxList.add(createPersistenceArray(allUtilHdx));
            }
        }

        // 开始设置带宽利用率
        List<String[]> utilHdxPercList = new ArrayList<String[]>();
        if (utilhdxpercVector != null) {
            for (Interfacecollectdata utilHdxPerc : utilhdxpercVector) {
                list.add(createDataTempArray(utilHdxPerc));
                utilHdxPercList.add(createPersistenceArray(utilHdxPerc));
            }
        }
        
        // 开始设置流速
        List<String[]> utilHdxList = new ArrayList<String[]>();
        if (utilhdxVector != null) {
            for (Interfacecollectdata utilHdx : utilhdxVector) {
                list.add(createDataTempArray(utilHdx));
                utilHdxList.add(createPersistenceArray(utilHdx));
            }
        }

        // 开始设置所有的信息
        // 端口状态信息入库
        // 开始设置流速
        List<String[]> portStatusList = new ArrayList<String[]>();
        if (interfaceVector != null) {
            for (Interfacecollectdata interfacecollectdata : interfaceVector) {
                list.add(createDataTempArray(interfacecollectdata));
                if (interfacecollectdata.getEntity().equals("ifOperStatus")) {
                    portStatusList.add(createPersistenceArray(interfacecollectdata));
                }
            }
        }
        

        // 开始设置丢包率
        List<String[]> discardspercList = new ArrayList<String[]>();
        if (discardspercVector != null) {
            for (Interfacecollectdata discardsPerc : discardspercVector) {
                list.add(createDataTempArray(discardsPerc));
                discardspercList.add(createPersistenceArray(discardsPerc));
            }
        }
        

        // 开始设置错误率
        List<String[]> errorspercList = new ArrayList<String[]>();
        if (errorspercVector != null) {
            for (Interfacecollectdata errorsPerc : errorspercVector) {
                list.add(createDataTempArray(errorsPerc));
                errorspercList.add(createPersistenceArray(errorsPerc));
            }
        }

        // 开始设置数据包
        List<String[]> packsList = new ArrayList<String[]>();
        if (packsVector != null) {
            for (Interfacecollectdata packs : packsVector) {
                list.add(createDataTempArray(packs));
                packsList.add(createPersistenceArray(packs));
            }
        }

        // 开始设置入口广播和多播数据包
        List<String[]> inPktsList = new ArrayList<String[]>();
        if (inpacksVector != null) {
            for (Interfacecollectdata inPkts : inpacksVector) {
                list.add(createDataTempArray(inPkts));
                inPktsList.add(createPersistenceArray(inPkts));
            }
        }

        // 开始设置入口广播和多播数据包
        List<String[]> outPktsList = new ArrayList<String[]>();
        if (outpacksVector != null) {
            for (Interfacecollectdata outPkts : outpacksVector) {
                list.add(createDataTempArray(outPkts));
                outPktsList.add(createPersistenceArray(outPkts));
            }
        }

        // 删除临时数据
        truncateDataTemp(TABLE_NAME_DATA_TEMP + getIPTableName(getIpAddress()));

        // 保存临时数据
        String sql = "insert into " + TABLE_NAME_DATA_TEMP + getIPTableName(getIpAddress()) + "(nodeid,ip,`type`,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(sql, list);
    
        // 保存长久数据
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_ALLUTILHDX, allUtilHdxList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_UTILHDXPERC, utilHdxPercList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_UTILHDX, utilHdxList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_PORT_STATUS, portStatusList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_DISCARDSPERC, discardspercList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_ERRORSPERC, errorspercList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_PACKS, packsList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_INPACKS, inPktsList);
        executeInterfaceToDB(TABLE_NAME_PERSISTENCE_OUTPACKS, outPktsList);

    }

    /**
     * executeInterfaceToDB:
     * <p>长久数据信息入库
     *
     * @param interfaceVector
     *
     * @since   v1.01
     */
    public void executeInterfaceToDB(String tableName, List<String[]> list) {
        String sql = "insert into "
            + tableName + getIPTableName(getIpAddress())
            + "(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime)"
            + "values(?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(sql, list);
    }

    public String[] createDataTempArray(Interfacecollectdata interfacecollectdata) {
        String[] array = new String[13];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = interfacecollectdata.getCategory();
        array[5] = interfacecollectdata.getEntity();
        array[6] = interfacecollectdata.getSubentity();
        array[7] = interfacecollectdata.getThevalue();
        array[8] = interfacecollectdata.getChname();
        array[9] = interfacecollectdata.getRestype();
        array[10] = format(interfacecollectdata.getCollecttime());
        array[11] = interfacecollectdata.getUnit();
        array[12] = interfacecollectdata.getBak();
        return array;
    }

    /**
     * createPersistenceArray:
     * <p>创建 持久数据 的数组
     *
     * @param   interfacecollectdata
     *          - {@link Interfacecollectdata}
     * @return  {@link String[]}
     *          - 持久数据 的数组
     *
     * @since   v1.01
     */
    public String[] createPersistenceArray(Interfacecollectdata interfacecollectdata) {
        String count = "0";
        if (interfacecollectdata.getCount() != null) {
            count = String.valueOf(interfacecollectdata.getCount());
        }
        String[] array = new String[11];
        array[0] = getIpAddress();
        array[1] = interfacecollectdata.getRestype();
        array[2] = interfacecollectdata.getCategory();
        array[3] = interfacecollectdata.getEntity();
        array[4] = interfacecollectdata.getSubentity();
        array[5] = interfacecollectdata.getUnit();
        array[6] = interfacecollectdata.getChname();
        array[7] = interfacecollectdata.getBak();
        array[8] = count;
        array[9] = interfacecollectdata.getThevalue();
        array[10] = format(interfacecollectdata.getCollecttime());
        return array;
    }
}

